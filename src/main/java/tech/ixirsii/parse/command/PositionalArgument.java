package tech.ixirsii.parse.command;

import lombok.NonNull;
import tech.ixirsii.parse.parser.Parser;

/**
 * Optional argument.
 *
 * @author Ryan Porterfield
 * @param <T> Argument type.
 * @since 1.0.0
 */
public final class PositionalArgument<T> extends Argument<T> {
    /**
     * Constructor.
     *
     * @param name        Argument name.
     * @param about       About message for help text.
     * @param parser      Function which parses argument strings into values.
     */
    public PositionalArgument(
            @NonNull final String name,
            @NonNull final String about,
            @NonNull final Parser<T> parser) {
        super(about, name, parser);
    }

    /* **************************************** Public override methods ***************************************** */

    @Override
    public String toString() {
        return getName() + getSpace(getName().length()) + getAbout();
    }

    /* *************************************** Protected override methods *************************************** */

    @Override
    protected @NonNull ArgumentValueCount getValueCount() {
        return ArgumentValueCount.ONE;
    }
}
