package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.command.ArgumentValueCount;

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

    @NonNull
    @Override
    public ArgumentValueCount getValueCount() {
        return ArgumentValueCount.ONE;
    }

    @Override
    public @NonNull ParseResult<String> parse(@NonNull final String value) {
        log.trace("Parsing {} as string", value);

        return new ParseResult<>(value, true, "");
    }
}
