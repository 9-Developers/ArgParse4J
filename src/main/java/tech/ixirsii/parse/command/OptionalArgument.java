package tech.ixirsii.parse.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import tech.ixirsii.parse.parser.Parser;

/**
 * Optional argument.
 *
 * @author Ryan Porterfield
 * @param <T> Argument type.
 * @since 1.0.0
 */
public final class OptionalArgument<T> extends Argument<T> {
    /**
     * POSIX short option.
     */
    @Getter(AccessLevel.PACKAGE)
    private final char shortOption;

    /**
     * Constructor.
     *
     * @param name        Argument name.
     * @param shortOption POSIX short option.
     * @param about       About message for help text.
     * @param parser      Function which parses argument strings into values.
     */
    public OptionalArgument(
            @NonNull final String name,
            final char shortOption,
            @NonNull final String about,
            @NonNull final Parser<T> parser) {
        super(about, name, parser);

        this.shortOption = shortOption;
    }

    @Override
    public String toString() {
        final String option = Command.POSIX_PREFIX + shortOption + ", " + Command.GNU_PREFIX + getName();
        final String space = getSpace(option.length());

        return option + space + getAbout();
    }
}
