package tech.ixirsii.parse.parser;

import lombok.NonNull;

/**
 * Result of parsing an argument.
 *
 * @author Ryan Porterfield
 * @param value Parsed value (if successful) otherwise {@code null}.
 * @param isSuccess Was parsing successful?
 * @param errorMessage Error message if parsing was not successful, otherwise empty string.
 * @param <T> Type of parsed value.
 * @since 1.0.0
 */
public record ParseResult<T>(T value, boolean isSuccess, @NonNull String errorMessage) {
}
