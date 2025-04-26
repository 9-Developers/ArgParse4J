package tech.ixirsii.parse.internal;

import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Map;

/**
 * Internal command event builder.
 *
 * @author Ryan Porterfield
 * @param events       Map of argument names to events.
 * @param unrecognized List of unrecognized arguments.
 * @since 1.0.0
 */
@Builder
public record InternalEvent(
        @Singular Map<String, ArgumentEvent<?>> events,
        @Singular("unrecognized") List<String> unrecognized) {
    /**
     * Convert to {@link tech.ixirsii.parse.event.CommandEvent}.
     *
     * @return new {@link tech.ixirsii.parse.event.CommandEvent}.
     */
    public tech.ixirsii.parse.event.CommandEvent toCommandEvent() {
        return new tech.ixirsii.parse.event.CommandEvent(events);
    }
}
