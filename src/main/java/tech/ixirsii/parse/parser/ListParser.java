package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.command.ArgumentValueCount;

import java.util.Collections;
import java.util.List;

/**
 * List parser.
 *
 * @author Ryan Porterfield
 * @param <T> Type of list.
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public final class ListParser<T> implements Parser<List<T>> {
    /**
     * Individual parser.
     */
    private final Parser<T> parser;

    @NonNull
    @Override
    public ArgumentValueCount getValueCount() {
        return ArgumentValueCount.ONE;
    }

    @Override
    public @NonNull ParseResult<List<T>> parse(@NonNull final String value) {
        log.trace("Parsing {} as list", value);

        return new ParseResult<>(Collections.emptyList(), true, "");
    }
}
