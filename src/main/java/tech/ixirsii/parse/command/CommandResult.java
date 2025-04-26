package tech.ixirsii.parse.command;

import lombok.NonNull;
import tech.ixirsii.parse.event.CommandEvent;

/**
 * Command result.
 *
 * @author Ryan Porterfield
 * @param event Command internal.
 * @param isSuccess Was the command successful?
 * @param errorMessage Error message if the command was not successful, otherwise empty string.
 * @since 1.0.0
 */
public record CommandResult(@NonNull CommandEvent event, boolean isSuccess, @NonNull String errorMessage) {
}
