package tech.ixirsii.parse.parser;

import lombok.NonNull;
import tech.ixirsii.parse.command.ArgumentValueCount;

/**
 * Parser interface.
 *
 * @author Ryan Porterfield
 * @param <T> Parser return type.
 * @since 1.0.0
 */
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
     * How many arguments does this parser accept?
     *
     * @return Argument value count.
     */
    @NonNull
    ArgumentValueCount getValueCount();

    /**
     * Parse input.
     *
     * @param value Argument value.
     * @return Parsed value.
     */
    @NonNull
    ParseResult<T> parse(@NonNull String value);
}
