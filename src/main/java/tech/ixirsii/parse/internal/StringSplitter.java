package tech.ixirsii.parse.internal;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for splitting strings.
 */
public final class StringSplitter {
    /**
     * Characters which can delineate a string.
     */
    private static final List<Character> QUOTE_CHARACTERS = List.of('"', '\'', '`');

    /**
     * Hide utility class constructor.
     */
    private StringSplitter() {
    }

    /**
     * Split an argument which may be in the form {@code name=value}.
     *
     * <ol>
     *     <li><i>--option=value</i> Where option "option" equals "value"</li>
     *     <li><i>--option=string=containing</i> Where option "option" equals "string=containing"</li>
     *     <li><i>--option string=containing</i> Where option "option" equals "string=containing"</li>
     *     <li><i>argument=value</i> Where argument "argument" equals "value"</li>
     *     <li><i>argument=string=containing</i> Where the argument "argument" equals "string=containing"</li>
     *     <li><i>string=containing</i> Where the positional argument equals "string=containing"</li>
     * </ol>
     *
     * @param argument Command argument.
     * @return List of split arguments.
     */
    public static List<String> splitArgument(@NonNull final String argument) {
        if (argument.contains("=")) {
            final List<String> result = new ArrayList<>();
            boolean isQuote = false;
            char quoteChar = 0;

            for (int i = 0; i < argument.length(); i++) {
                final char c = argument.charAt(i);

                if (!isQuote && c == '=') {
                    return List.of(argument.substring(0, i), argument.substring(i + 1));
                } else if (!isQuote && QUOTE_CHARACTERS.contains(c)) {
                    isQuote = true;
                    quoteChar = c;
                } else if (isQuote && c == quoteChar) {
                    isQuote = false;
                }
            }

            return result;
        } else {
            return Collections.singletonList(argument);
        }
    }
}
