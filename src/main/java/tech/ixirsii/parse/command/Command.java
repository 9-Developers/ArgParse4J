package tech.ixirsii.parse.command;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.internal.ArgumentEvent;
import tech.ixirsii.parse.internal.ArgumentMatch;
import tech.ixirsii.parse.internal.ArgumentPair;
import tech.ixirsii.parse.internal.InternalEvent;
import tech.ixirsii.parse.parser.Parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;

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
    private final List<PositionalArgument<?>> positionalArguments;
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
        final SequencedMap<Character, OptionalArgument<?>> shortOptionMap =
                LinkedHashMap.newLinkedHashMap(optionalArguments.size() + 1);

        longOptionMap.put(help.getName(), help);
        shortOptionMap.put(help.getShortOption(), help);

        for (final OptionalArgument<?> optionalArgument : optionalArguments) {
            longOptionMap.put(optionalArgument.getName(), optionalArgument);
            shortOptionMap.put(optionalArgument.getShortOption(), optionalArgument);
        }

        this.about = about;
        this.longOptions = Collections.unmodifiableSequencedMap(longOptionMap);
        this.name = name;
        this.positionalArguments = List.copyOf(positionalArguments);
        this.shortOptions = Collections.unmodifiableSequencedMap(shortOptionMap);
        this.usage = usage;
    }

    public CommandResult parse2(@NonNull final List<String> arguments) {
        log.trace("Parsing arguments {}", arguments);

        final InternalEvent.InternalEventBuilder builder = InternalEvent.builder();
        final Map<Integer, List<Argument<?>>> matchedArguments = HashMap.newHashMap(arguments.size());

        for (int i = 0; i < arguments.size(); ++i) {
            final String argument = arguments.get(i);
            final List<ArgumentMatch> argumentMatches = getArgument(argument);

            if (argumentMatches.size() == 1) {
                final ArgumentMatch argumentMatch = argumentMatches.getFirst();

                if (argumentMatch.match() == null) {
                    builder.unrecognized(argument);
                } else {
                    matchedArguments.put(i, Collections.singletonList(argumentMatch.match()));
                }
            } else if (!argumentMatches.isEmpty()) {
                final List<Argument<?>> matchedShortOptions = new ArrayList<>(argumentMatches.size());

                for (final ArgumentMatch argumentMatch : argumentMatches) {
                    if (argumentMatch.match() == null) {
                        builder.unrecognized(argumentMatch.argument());
                    } else {
                        matchedShortOptions.add(argumentMatch.match());
                    }
                }

                matchedArguments.put(i, matchedShortOptions);
            }
        }

        int j = 0;
        for (int i = 0; i < arguments.size(); ++i) {
            final String argument = arguments.get(i);

            if (matchedArguments.containsKey(i)) {
                final Argument<?> argument = matchedArguments.get(i);

                if (argument.getValueCount() == ArgumentValueCount.ONE) {

                }
            } else if (j < positionalArguments.size()) {
                final PositionalArgument<?> positionalArgument = positionalArguments.get(j);

                builder.event(positionalArgument.getName(), positionalArgument.parse("", argument));
                ++j;
            } else {
                builder.unrecognized(argument);
            }
        }
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
        final List<ArgumentPair> argumentsWithValues = new ArrayList<>(arguments.size());
        final List<PositionalArgument<?>> unmatchedArguments = new ArrayList<>(positionalArguments);

        // Build the argument map, mapping argument indices to arguments.
        for (int i = 0; i < arguments.size(); ++i) {
            // TODO: Split argument
            final String argument = arguments.get(i);
            final List<ArgumentMatch> argumentMatches = getArgument(argument);

            if (argumentMatches.isEmpty()) {
                if (!unmatchedArguments.isEmpty()) {
                    final Argument<?> positionalArgument = unmatchedArguments.removeFirst();
                    argumentsWithValues.add(new ArgumentPair(positionalArgument, argument));
                } else {
                    builder.unrecognized(argument);
                }
            } else if (argumentMatches.size() == 1) {
                final ArgumentMatch argumentMatch = argumentMatches.getFirst();

                if (argumentMatch.match() != null) {
                    final Argument<?> mappedArgument = argumentMatch.match();

                    if (mappedArgument.getValueCount() == ArgumentValueCount.ZERO || i >= arguments.size() - 1) {
                        argumentsWithValues.add(new ArgumentPair(mappedArgument, ""));
                    } else if (mappedArgument.getValueCount() == ArgumentValueCount.ZERO_OR_ONE) {
                        // TODO: this
                    } else {
                        // TODO: Split argument
                        final String nextArgument = arguments.get(i + 1);
                        final List<ArgumentMatch> nextArgumentMatches = getArgument(nextArgument);

                        if (nextArgumentMatches.size() == 1) {
                            final ArgumentMatch nextArgumentMatch = nextArgumentMatches.getFirst();

                            if (nextArgumentMatch.match() == null) {
                                // The next argument is actually the value for this argument, skip processing
                                ++i;
                                argumentsWithValues.add(new ArgumentPair(mappedArgument, nextArgument));
                            } else {
                                argumentsWithValues.add(new ArgumentPair(mappedArgument, ""));
                            }
                        }
                    }
                }
            } else {
                for (final ArgumentMatch argumentMatch : argumentMatches) {
                    if (argumentMatch.match() == null) {
                        builder.unrecognized(argumentMatch.argument());
                    } else {
                        argumentsWithValues.add(new ArgumentPair(argumentMatch.match(), ""));
                    }
                }
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

        for (final PositionalArgument<?> positionalArgument : positionalArguments) {
            if (!events.containsKey(positionalArgument.getName())) {
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

        for (final PositionalArgument<?> argument : positionalArguments) {
            stringBuilder.append(argument).append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    /* **************************************** Private utility methods ***************************************** */

    private List<ArgumentMatch> getArgument(@NonNull final String argument) {
        if (argument.equals(GNU_PREFIX) || argument.equals(POSIX_PREFIX)) {
            return Collections.singletonList(new ArgumentMatch(argument, null));
        } else if (argument.startsWith(GNU_PREFIX)) {
            final String longOption = argument.substring(GNU_PREFIX.length());
            final OptionalArgument<?> optionalArgument = longOptions.get(longOption);

            return Collections.singletonList(new ArgumentMatch(argument, optionalArgument));
        } else if (argument.startsWith(POSIX_PREFIX)) {
            final String flags = argument.substring(POSIX_PREFIX.length());
            final List<ArgumentMatch> matchedShortOptions = new ArrayList<>(flags.length());

            for (final char flag : flags.toCharArray()) {
                final OptionalArgument<?> optionalArgument = shortOptions.get(flag);

                matchedShortOptions.add(new ArgumentMatch("-" + flag, optionalArgument));
            }

            return matchedShortOptions;
        } else {
            return Collections.emptyList();
        }
    }

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

        for (final PositionalArgument<?> argument : positionalArguments) {
            if (!commandEvent.events().containsKey(argument.getName())) {
                log.debug("Command internal does not contain required positional argument {}", argument);

                return false;
            }
        }

        return true;
    }
}
