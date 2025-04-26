package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link Parser} for string values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class StringParser implements Parser<String> {
    /**
     * Hide constructor.
     */
    /* default */ StringParser() {
    }

    @Override
    public ParseResult<String> parse(@NonNull final String value) {
        log.trace("Parsing {} as string", value);

        return new ParseResult<>(value, true, "");
    }
}
