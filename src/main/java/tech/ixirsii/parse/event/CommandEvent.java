package tech.ixirsii.parse.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;
import java.util.Map;

// TODO: Consider making this internal and exposing a different event type.
/**
 * Command event.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@Builder
@Getter
public class CommandEvent {
    /**
     * Map of argument names to events.
     */
    @Singular
    private final Map<String, ArgumentEvent<?>> events;
    /**
     * List of unrecognized arguments.
     */
    @Singular("unrecognized")
    private final List<String> unrecognized;

    /**
     * Get parsed argument value.
     *
     * @param name Argument name.
     * @param <T> Argument type.
     * @return Parsed argument value or {@code null} if argument is not present.
     * @throws ClassCastException if the argument is not of the expected type.
     * @throws IllegalArgumentException if the argument is not recognized.
     */
    public <T> T get(@NonNull final String name) {
        final ArgumentEvent<?> event = events.get(name);

        if (event == null) {
            throw new IllegalArgumentException("Unrecognized argument " + name);
        } else {
            return ((ArgumentEvent<T>) event).parsedValue();
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("{")
                .append(System.lineSeparator());

        for (final Map.Entry<String, ArgumentEvent<?>> event : events.entrySet()) {
            final ArgumentEvent<?> value = event.getValue();

            builder.append("\t");

            if (!value.isSuccess()) {
                builder.append(value.name())
                        .append("=\"")
                        .append(value.value())
                        .append("\": ")
                        .append(value.errorMessage())
                        .append(System.lineSeparator());
            } else {
                builder.append(value.name())
                        .append("=\"")
                        .append(value.value())
                        .append("\"")
                        .append(System.lineSeparator());
            }
        }

        return builder.append("\tUnrecognized arguments: ")
                .append(unrecognized)
                .append(System.lineSeparator())
                .append("}")
                .toString();
    }
}
