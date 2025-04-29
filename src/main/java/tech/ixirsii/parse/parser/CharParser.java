package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.command.ArgumentValueCount;

/**
 * {@link Parser} for character values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class CharParser implements Parser<Character> {
    /**
     * Hide constructor.
     */
    /* default */ CharParser() {
    }

    @NonNull
    @Override
    public ArgumentValueCount getValueCount() {
        return ArgumentValueCount.ONE;
    }

    @Override
    public @NonNull ParseResult<Character> parse(@NonNull final String value) {
        log.trace("Parsing {} as char", value);

        if (value.length() != 1) {
            return new ParseResult<>(null, false, "Character required but got " + value);
        }

        return new ParseResult<>(value.charAt(0), true, "");
    }
}
