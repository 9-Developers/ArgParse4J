package tech.ixirsii.parse.parser;

import lombok.NonNull;

/**
 * {@link Parser} for integer values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
public final class IntParser implements Parser<Integer> {
    /**
     * Hide constructor.
     */
    /* default */ IntParser() {
    }

    @Override
    public ParseResult<Integer> parse(@NonNull final String value) {
        try {
            return new ParseResult<>(Integer.parseInt(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Integer required but got " + value);
        }
    }
}
