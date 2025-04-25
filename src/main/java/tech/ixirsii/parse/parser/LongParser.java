package tech.ixirsii.parse.parser;

import lombok.NonNull;

/**
 * {@link Parser} for long values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
public final class LongParser implements Parser<Long> {
    /**
     * Hide constructor.
     */
    /* default */ LongParser() {
    }

    @Override
    public ParseResult<Long> parse(@NonNull final String value) {
        try {
            return new ParseResult<>(Long.parseLong(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Long required but got " + value);
        }
    }
}
