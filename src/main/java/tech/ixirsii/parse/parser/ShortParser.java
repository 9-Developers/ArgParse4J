package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link Parser} for short values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class ShortParser implements Parser<Short> {
    /**
     * Hide constructor.
     */
    /* default */ ShortParser() {
    }

    @Override
    public ParseResult<Short> parse(@NonNull final String value) {
        log.trace("Parsing {} as short", value);

        try {
            return new ParseResult<>(Short.parseShort(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Short required but got " + value);
        }
    }
}
