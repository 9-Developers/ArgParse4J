package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link Parser} for double values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class DoubleParser implements Parser<Double> {
    /**
     * Hide constructor.
     */
    /* default */ DoubleParser() {
    }

    @Override
    public ParseResult<Double> parse(@NonNull final String value) {
        log.trace("Parsing {} as double", value);

        try {
            return new ParseResult<>(Double.parseDouble(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Double required but got " + value);
        }
    }
}
