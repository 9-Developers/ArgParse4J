package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.command.ArgumentValueCount;

/**
 * {@link Parser} for long values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class LongParser implements Parser<Long> {
    /**
     * Hide constructor.
     */
    /* default */ LongParser() {
    }

    @NonNull
    @Override
    public ArgumentValueCount getValueCount() {
        return ArgumentValueCount.ONE;
    }

    @Override
    public @NonNull ParseResult<Long> parse(@NonNull final String value) {
        log.trace("Parsing {} as long", value);

        try {
            return new ParseResult<>(Long.parseLong(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Long required but got " + value);
        }
    }
}
