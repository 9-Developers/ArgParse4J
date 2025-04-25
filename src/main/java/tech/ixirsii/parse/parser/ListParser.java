package tech.ixirsii.parse.parser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
public final class ListParser<T> implements Parser<List<T>> {
    /**
     * Individual parser.
     */
    private final Parser<T> parser;

    @Override
    public ParseResult<List<T>> parse(@NonNull final String value) {
        return new ParseResult<>(Collections.emptyList(), true, "");
    }
}
