package tech.ixirsii.parse.parser;

import lombok.NonNull;

/**
 * Parser interface.
 *
 * @author Ryan Porterfield
 * @param <T> Parser return type.
 * @since 1.0.0
 */
@FunctionalInterface
public interface Parser<T> {
    /**
     * Boolean parser.
     */
    BooleanParser BOOLEAN_PARSER = new BooleanParser();
    /**
     * Byte parser.
     */
    ByteParser BYTE_PARSER = new ByteParser();
    /**
     * Char parser.
     */
    CharParser CHAR_PARSER = new CharParser();
    /**
     * Double parser.
     */
    DoubleParser DOUBLE_PARSER = new DoubleParser();
    /**
     * Float parser.
     */
    FloatParser FLOAT_PARSER = new FloatParser();
    /**
     * Integer parser.
     */
    IntParser INT_PARSER = new IntParser();
    /**
     * Long parser.
     */
    LongParser LONG_PARSER = new LongParser();
    /**
     * Short parser.
     */
    ShortParser SHORT_PARSER = new ShortParser();
    /**
     * String parser.
     */
    StringParser STRING_PARSER = new StringParser();

    /**
     * Parse input.
     *
     * @param value Argument value.
     * @return Parsed value.
     */
    ParseResult<T> parse(@NonNull String value);
}
