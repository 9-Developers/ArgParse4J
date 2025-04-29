package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.command.ArgumentValueCount;

/**
 * {@link Parser} for byte values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class ByteParser implements Parser<Byte> {
    /**
     * Hide constructor.
     */
    /* default */ ByteParser() {
    }

    @NonNull
    @Override
    public ArgumentValueCount getValueCount() {
        return ArgumentValueCount.ONE;
    }

    @Override
    public @NonNull ParseResult<Byte> parse(@NonNull final String value) {
        log.trace("Parsing {} as byte", value);

        try {
            return new ParseResult<>(Byte.parseByte(value), true, "");
        } catch (final NumberFormatException e) {
            return new ParseResult<>(null, false, "Byte required but got " + value);
        }
    }
}
