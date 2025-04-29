package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.command.ArgumentValueCount;

import java.util.List;

/**
 * {@link Parser} for boolean values.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Slf4j
public final class BooleanParser implements Parser<Boolean> {
    /**
     * Possible values for a false argument.
     */
    private static final List<String> FALSE_VALUES = List.of("f", "false", "n", "no");
    /**
     * Possible values for a true argument.
     */
    private static final List<String> TRUE_VALUES = List.of("t", "true", "y", "yes");
    /**
     * Valid boolean values message.
     */
    private static final String VALID_VALUES_MSG = "Valid values are false " + FALSE_VALUES + " or true " + TRUE_VALUES;

    /**
     * Hide constructor.
     */
    /* default */ BooleanParser() {
    }

    @NonNull
    @Override
    public ArgumentValueCount getValueCount() {
        return ArgumentValueCount.ZERO_OR_ONE;
    }

    @Override
    public @NonNull ParseResult<Boolean> parse(@NonNull final String value) {
        log.trace("Parsing {} as boolean", value);

        if (value.isEmpty()) {
            // No value was passed, but the optional flag is present meaning true
            return new ParseResult<>(true, true, "");
        }

        final String lowercase = value.toLowerCase();
        final boolean isFalse = FALSE_VALUES.contains(lowercase);
        final boolean isTrue = TRUE_VALUES.contains(lowercase);

        if (!isFalse && !isTrue) {
            return new ParseResult<>(null, false, "Boolean required but got " + value + ". " + VALID_VALUES_MSG);
        }

        return new ParseResult<>(isTrue, true, "");
    }
}
