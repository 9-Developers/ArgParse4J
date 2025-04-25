package tech.ixirsii.parse.parser;

import lombok.NonNull;

/**
 * {@link Parser} for string values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
public final class StringParser implements Parser<String> {
    /**
     * Hide constructor.
     */
    /* default */ StringParser() {
    }

    @Override
    public ParseResult<String> parse(@NonNull final String value) {
        return new ParseResult<>(value, true, "");
    }
}
