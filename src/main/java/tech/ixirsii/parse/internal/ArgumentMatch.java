package tech.ixirsii.parse.internal;

import lombok.NonNull;
import tech.ixirsii.parse.command.Argument;

/**
 * Result of trying to match an argument by name.
 *
 * @author Ryan Porterfield
 * @param argument Argument name.
 * @param match Argument if found, otherwise {@code null}.
 * @since 1.0.0
 */
public record ArgumentMatch(@NonNull String argument, Argument<?> match) {
}
