package tech.ixirsii.parse.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tech.ixirsii.parse.event.ArgumentEvent;
import tech.ixirsii.parse.parser.ParseResult;
import tech.ixirsii.parse.parser.Parser;

/**
 * Base class for arguments.
 *
 * @author Ryan Porterfield
 * @param <T> Argument type.
 * @since 1.0.0
 */
@RequiredArgsConstructor
public abstract sealed class Argument<T> implements Comparable<Argument<T>>
        permits OptionalArgument, PositionalArgument {
    /**
     * Length of columns in the help message.
     */
    private static final int COLUMN_WIDTH = 24;

    /**
     * About message for help text.
     */
    @Getter(AccessLevel.PACKAGE)
    @NonNull
    private final String about;
    /**
     * Argument name.
     */
    @NonNull
    @Getter(AccessLevel.PACKAGE)
    private final String name;
    /**
     * Function which parses argument strings into values.
     */
    @NonNull
    private final Parser<T> parser;

    /* ******************************************** Override methods ******************************************** */

    @Override
    public int compareTo(@NonNull final Argument<T> other) {
        return name.compareTo(other.name);
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof Argument<?> argument) {
            return name.equals(argument.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /* *************************************** Protected utility methods **************************************** */

    /**
     * Get space between option and help text.
     *
     * @param length How long the option text is.
     * @return Space between the option name and help text.
     */
    protected String getSpace(final int length) {
        final int spaceLength = COLUMN_WIDTH - length;

        if (spaceLength < 2) {
            // Always return at least 1 space
            return " ";
        } else {
            return " ".repeat(spaceLength);
        }
    }

    /**
     * Parse argument.
     *
     * @param name  Optional argument name if passed.
     * @param value Argument value.
     * @return New argument event.
     */
    protected ArgumentEvent<T> parse(@NonNull final String name, @NonNull final String value) {
        final ParseResult<T> result = parser.parse(value);

        if (result.isSuccess()) {
            return new ArgumentEvent<>(name, value, result.value(), true, "");
        } else {
            return new ArgumentEvent<>(name, value, null, false, result.errorMessage());
        }
    }
}
