package tech.ixirsii.parse.command;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.internal.ArgumentEvent;
import tech.ixirsii.parse.internal.InternalEvent;
import tech.ixirsii.parse.parser.Parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.SequencedSet;

/**
 * Command class.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class Command {
    /**
     * GNU prefix.
     */
    public static final String GNU_PREFIX = "--";
    /**
     * POSIX prefix.
     */
    public static final String POSIX_PREFIX = "-";

    /**
     * About message for help text.
     */
    @NonNull
    private final String about;
    /**
     * GNU long options.
     */
    @NonNull
    private final SequencedMap<String, OptionalArgument<?>> longOptions;
    /**
     * Command name.
     */
    @NonNull
    private final String name;
    /**
     * Required (positional) arguments.
     */
    @NonNull
    private final SequencedMap<String, PositionalArgument<?>> positionalArguments;
    /**
     * POSIX short options.
     */
    @NonNull
    private final SequencedMap<Character, OptionalArgument<?>> shortOptions;
    /**
     * Usage message.
     */
    @NonNull
    private final String usage;

    /**
     * Constructor.
     *
     * @param name                Command name.
     * @param about               About message for help text.
     * @param usage               Usage message.
     * @param optionalArguments   Optional arguments.
     * @param positionalArguments Required (positional) arguments.
     */
    public Command(
            @NonNull final String name,
            @NonNull final String about,
            @NonNull final String usage,
            @NonNull final Collection<OptionalArgument<?>> optionalArguments,
            @NonNull final Collection<PositionalArgument<?>> positionalArguments) {
        final OptionalArgument<Boolean> help = new OptionalArgument<>(
                "help",
                'h',
                "Show this help message",
                Parser.BOOLEAN_PARSER);
        final SequencedMap<String, OptionalArgument<?>> longOptionMap =
                LinkedHashMap.newLinkedHashMap(optionalArguments.size() + 1);
        final SequencedMap<String, PositionalArgument<?>> positionalArgumentMap =
                LinkedHashMap.newLinkedHashMap(positionalArguments.size());
        final SequencedMap<Character, OptionalArgument<?>> shortOptionMap =
                LinkedHashMap.newLinkedHashMap(optionalArguments.size() + 1);

        longOptionMap.put(help.getName(), help);
        shortOptionMap.put(help.getShortOption(), help);

        for (final OptionalArgument<?> optionalArgument : optionalArguments) {
            longOptionMap.put(optionalArgument.getName(), optionalArgument);
            shortOptionMap.put(optionalArgument.getShortOption(), optionalArgument);
        }

        for (final PositionalArgument<?> positionalArgument : positionalArguments) {
            positionalArgumentMap.put(positionalArgument.getName(), positionalArgument);
        }

        this.about = about;
        this.longOptions = Collections.unmodifiableSequencedMap(longOptionMap);
        this.name = name;
        this.positionalArguments = Collections.unmodifiableSequencedMap(positionalArgumentMap);
        this.shortOptions = Collections.unmodifiableSequencedMap(shortOptionMap);
        this.usage = usage;
    }

    /**
     * Parse input that has already been split, such as JVM command line arguments.
     *
     * @param arguments Split input.
     * @return New internal.
     */
    public CommandResult parse(@NonNull final List<String> arguments) {
        log.trace("Parsing arguments {}", arguments);

        final InternalEvent.InternalEventBuilder builder = InternalEvent.builder();
        final Map<Integer, List<Argument<?>>> argumentMap = HashMap.newHashMap(arguments.size());
        final SequencedSet<PositionalArgument<?>> unmatchedArguments =
                new LinkedHashSet<>(positionalArguments.values());

        // Build the argument map, mapping argument indices to arguments.
        for (int i = 0; i < arguments.size(); ++i) {
            final String argument = arguments.get(i);

            if (argument.equals(GNU_PREFIX) || argument.equals(POSIX_PREFIX)) {
                builder.unrecognized(argument);
            } else if (argument.startsWith(GNU_PREFIX)) {
                final String longOption = argument.substring(GNU_PREFIX.length());
                final OptionalArgument<?> optionalArgument = longOptions.get(longOption);

                if (optionalArgument == null) {
                    builder.unrecognized(argument);
                } else {
                    argumentMap.put(i, Collections.singletonList(optionalArgument));
                }
            } else if (argument.startsWith(POSIX_PREFIX)) {
                final String flags = argument.substring(POSIX_PREFIX.length());
                final List<Argument<?>> matchedShortOptions = new ArrayList<>(flags.length());

                for (final char flag : flags.toCharArray()) {
                    final OptionalArgument<?> optionalArgument = shortOptions.get(flag);

                    if (optionalArgument == null) {
                        builder.unrecognized(String.valueOf(flag));
                    } else {
                        matchedShortOptions.add(optionalArgument);
                    }
                }

                argumentMap.put(i, matchedShortOptions);
            } else {
                // Argument is named or positional
                final PositionalArgument<?> positionalArgument = positionalArguments.get(argument);

                if (positionalArgument != null) {
                    argumentMap.put(i, Collections.singletonList(positionalArgument));
                    unmatchedArguments.remove(positionalArgument);
                }
            }
        }

        // Parse mapped arguments
        for (int i = 0; i < arguments.size(); ++i) {
            final String argument = arguments.get(i);

            if (argumentMap.containsKey(i)) {
                final List<Argument<?>> mappedArguments = argumentMap.get(i);

                if (mappedArguments.size() > 1) {
                    for (final Argument<?> mappedArgument : mappedArguments) {
                        builder.event(mappedArgument.getName(), mappedArgument.parse(argument, ""));
                    }
                } else {
                    final Argument<?> mappedArgument = mappedArguments.getFirst();
                    final String value;

                    if (i + 1 >= arguments.size() || argumentMap.containsKey(i + 1)) {
                        value = "";
                    } else {
                        // Skip processing the next argument as it's the value for this argument
                        ++i;
                        value = arguments.get(i);
                    }

                    builder.event(mappedArgument.getName(), mappedArgument.parse(argument, value));
                }
            } else if (unmatchedArguments.isEmpty()) {
                builder.unrecognized(argument);
            } else {
                final Argument<?> positionalArgument = unmatchedArguments.removeFirst();

                builder.event(positionalArgument.getName(), positionalArgument.parse("", argument));
            }
        }

        final InternalEvent internalEvent = builder.build();

        log.debug("Parsed command internal {}", internalEvent);

        if (isValid(internalEvent)) {
            return new CommandResult(internalEvent.toCommandEvent(), true, "");
        } else {
            return new CommandResult(internalEvent.toCommandEvent(), false, getErrorMessage(internalEvent));
        }
    }

    /* ******************************************** Override methods ******************************************** */

    /**
     * Convert event to string.
     *
     * @param commandEvent Internal command event to convert.
     * @return String representation of the event.
     */
    private String getErrorMessage(@NonNull final InternalEvent commandEvent) {
        final StringBuilder builder = new StringBuilder("{")
                .append(System.lineSeparator());
        final Map<String, ArgumentEvent<?>> events = commandEvent.events();

        for (final Map.Entry<String, ArgumentEvent<?>> event : events.entrySet()) {
            final ArgumentEvent<?> value = event.getValue();

            builder.append("    ");

            if (!value.isSuccess()) {
                builder.append(value.name())
                        .append("=\"")
                        .append(value.value())
                        .append("\": ")
                        .append(value.errorMessage())
                        .append(System.lineSeparator());
            } else {
                builder.append(value.name())
                        .append("=\"")
                        .append(value.value())
                        .append("\"")
                        .append(System.lineSeparator());
            }
        }

        for (final String positionalArgument : positionalArguments.sequencedKeySet()) {
            if (!events.containsKey(positionalArgument)) {
                builder.append("    ")
                        .append(positionalArgument)
                        .append(" is missing but is required")
                        .append(System.lineSeparator());
            }
        }

        if (!commandEvent.unrecognized().isEmpty()) {
            builder.append("    Unrecognized arguments: ")
                    .append(commandEvent.unrecognized())
                    .append(System.lineSeparator());
        }

        return builder.append("}").toString();
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder(name)
                .append(System.lineSeparator())
                .append(about)
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("Usage:")
                .append(System.lineSeparator())
                .append(usage)
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("Options:")
                .append(System.lineSeparator());

        for (final OptionalArgument<?> option : longOptions.sequencedValues()) {
            stringBuilder.append(option).append(System.lineSeparator());
        }

        stringBuilder.append(System.lineSeparator())
                .append("Arguments:")
                .append(System.lineSeparator());

        for (final PositionalArgument<?> argument : positionalArguments.sequencedValues()) {
            stringBuilder.append(argument).append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    /* **************************************** Private utility methods ***************************************** */

    /**
     * Check if the internal is valid.
     *
     * @param commandEvent Command internal to check.
     * @return {@code true} if the internal is valid, otherwise {@code false}.
     */
    private boolean isValid(@NonNull final InternalEvent commandEvent) {
        log.trace("Validating command internal");

        if (!commandEvent.unrecognized().isEmpty()) {
            log.debug("Command internal contains unrecognized arguments");
            return false;
        }

        for (final ArgumentEvent<?> argumentEvent : commandEvent.events().values()) {
            if (!argumentEvent.isSuccess()) {
                log.debug("Command internal failed to parse some arguments");
                return false;
            }
        }

        for (final String argument : positionalArguments.keySet()) {
            if (!commandEvent.events().containsKey(argument)) {
                log.debug("Command internal does not contain required positional argument {}", argument);

                return false;
            }
        }

        return true;
    }
}
