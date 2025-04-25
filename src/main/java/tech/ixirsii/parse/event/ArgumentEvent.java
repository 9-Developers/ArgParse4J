package tech.ixirsii.parse.event;

import lombok.NonNull;

/**
 * Argument event.
 *
 * @author Ryan Porterfield
 * @param name Optional name of argument or empty string.
 * @param value Argument value.
 * @param parsedValue Parsed value if successful, otherwise {@code null}.
 * @param isSuccess Was parsing successful?
 * @param errorMessage Error message if parsing was not successful, otherwise empty string.
 * @param <T> Type of parsed value.
 * @since 1.0.0
 */
public record ArgumentEvent<T>(
        @NonNull String name,
        @NonNull String value,
        T parsedValue,
        boolean isSuccess,
        @NonNull String errorMessage) {
}
