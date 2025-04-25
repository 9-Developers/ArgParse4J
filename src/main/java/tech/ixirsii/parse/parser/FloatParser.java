package tech.ixirsii.parse.parser;

import lombok.NonNull;

/**
 * {@link Parser} for float values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
public final class FloatParser implements Parser<Float> {
    /**
     * Hide constructor.
     */
    /* default */ FloatParser() {
    }

    @Override
    public ParseResult<Float> parse(@NonNull final String value) {
        try {
            return new ParseResult<>(Float.parseFloat(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Float required but got " + value);
        }
    }
}
