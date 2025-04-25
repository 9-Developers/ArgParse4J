package tech.ixirsii.parse.parser;

import lombok.NonNull;

/**
 * {@link Parser} for byte values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
public final class ByteParser implements Parser<Byte> {
    /**
     * Hide constructor.
     */
    /* default */ ByteParser() {
    }

    @Override
    public ParseResult<Byte> parse(@NonNull final String value) {
        try {
            return new ParseResult<>(Byte.parseByte(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Byte required but got " + value);
        }
    }
}
