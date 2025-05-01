package tech.ixirsii.parse.internal;

import lombok.NonNull;
import tech.ixirsii.parse.command.Argument;

/**
 * Pair of argument and (optional) value.
 *
 * @author Ryan Porterfield
 * @param argument Argument.
 * @param value Value if present, otherwise empty string.
 * @since 1.0.0
 */
public record ArgumentPair(@NonNull Argument<?> argument, @NonNull String value) {
}
