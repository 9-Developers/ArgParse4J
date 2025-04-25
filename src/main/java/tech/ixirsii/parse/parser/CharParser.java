package tech.ixirsii.parse.parser;

import lombok.NonNull;

/**
 * {@link Parser} for character values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
public final class CharParser implements Parser<Character> {
    /**
     * Hide constructor.
     */
    /* default */ CharParser() {
    }

    @Override
    public ParseResult<Character> parse(@NonNull final String value) {
        if (value.length() != 1) {
            return new ParseResult<>(null, false, "Character required but got " + value);
        }

        return new ParseResult<>(value.charAt(0), true, "");
    }
}
