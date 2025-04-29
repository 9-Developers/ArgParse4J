package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.command.ArgumentValueCount;

/**
 * {@link Parser} for float values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class FloatParser implements Parser<Float> {
    /**
     * Hide constructor.
     */
    /* default */ FloatParser() {
    }

    @NonNull
    @Override
    public ArgumentValueCount getValueCount() {
        return ArgumentValueCount.ONE;
    }

    @Override
    public @NonNull ParseResult<Float> parse(@NonNull final String value) {
        log.trace("Parsing {} as flaot", value);

        try {
            return new ParseResult<>(Float.parseFloat(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Float required but got " + value);
        }
    }
}
