package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.command.ArgumentValueCount;

/**
 * {@link Parser} for integer values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class IntParser implements Parser<Integer> {
    /**
     * Hide constructor.
     */
    /* default */ IntParser() {
    }

    @NonNull
    @Override
    public ArgumentValueCount getValueCount() {
        return ArgumentValueCount.ONE;
    }

    @Override
    public @NonNull ParseResult<Integer> parse(@NonNull final String value) {
        log.trace("Parsing {} as int", value);

        try {
            return new ParseResult<>(Integer.parseInt(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Integer required but got " + value);
        }
    }
}
