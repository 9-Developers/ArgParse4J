package tech.ixirsii.parse.event;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.ixirsii.parse.internal.ArgumentEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Command event.
 *
 * @author Ryan Porterfield
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class CommandEvent {
    /**
     * Argument events.
     */
    private final Map<String, ArgumentEvent<?>> events;

    /**
     * Get parsed argument value.
     *
     * @param name Argument name.
     * @param type Argument type.
     * @param <T> Argument type.
     * @return Parsed argument value or {@code null} if argument is not present.
     * @throws ClassCastException if the argument is not of the expected type.
     */
    public <T> T get(@NonNull final String name, @NonNull final Class<T> type) {
        log.trace("Getting {} as {}", name, type);

        final ArgumentEvent<?> event = events.get(name);

        if (event == null) {
            return null;
        } else {
            return type.cast(event.parsedValue());
        }
    }

    /**
     * Get parsed argument value.
     *
     * @param name Argument name.
     * @param defaultValue Default value to return if {@code name} is not present.
     * @param type Argument type.
     * @param <T> Argument type.
     * @return Parsed argument value or {@code null} if argument is not present.
     * @throws ClassCastException if the argument is not of the expected type.
     */
    public <T> T getOrDefault(@NonNull final String name, @NonNull final T defaultValue, @NonNull final Class<T> type) {
        log.trace("Getting {} as {} or default", name, type);

        final ArgumentEvent<?> event = events.get(name);

        if (event == null) {
            return defaultValue;
        } else {
            return type.cast(event.parsedValue());
        }
    }

    /**
     * Get parsed argument value list.
     *
     * @param name Argument name.
     * @param type Argument type.
     * @param <T> Argument type.
     * @return Parsed argument value or {@code null} if argument is not present.
     * @throws ClassCastException if the argument is not of the expected type.
     */
    public <T> List<T> getList(@NonNull final String name, @NonNull final Class<T> type) {
        log.trace("Getting {} as list of {}", name, type);

        final ArgumentEvent<?> event = events.get(name);

        if (event == null) {
            return Collections.emptyList();
        } else if (event.parsedValue() instanceof List<?> genericList) {
            final List<T> result = new ArrayList<>(genericList.size());

            for (final Object value : genericList) {
                result.add(type.cast(value));
            }

            return result;
        } else {
            throw new ClassCastException("Argument " + name + " is a "
                    + event.parsedValue().getClass().getSimpleName()
                    + " not a list");
        }
    }
}
