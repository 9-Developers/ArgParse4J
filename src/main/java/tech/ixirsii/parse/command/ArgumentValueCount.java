package tech.ixirsii.parse.command;

/**
 * How many values an argument consumes.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
public enum ArgumentValueCount {
    /**
     * Argument does not consume any values (boolean/flag-only).
     */
    ZERO,
    /**
     * Argument can consume zero or one value.
     */
    ZERO_OR_ONE,
    /**
     * Argument can consume any number of values.
     */
    ZERO_OR_MORE,
    /**
     * Argument consumes exactly one value.
     */
    ONE,
    /**
     * Argument consumes one or more values.
     */
    ONE_OR_MORE,
}
