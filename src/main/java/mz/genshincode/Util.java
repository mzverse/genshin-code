package mz.genshincode;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Util
{
    public static <T> T cast(Object value)
    {
        //noinspection unchecked
        return (T) value;
    }

    public static <T> void consume(T value, Consumer<T> consumer)
    {
        consumer.accept(value);
    }

    public static <T, E extends Throwable> T valueThrow(E throwable) throws E
    {
        throw throwable;
    }

    public static <T> Iterable<T> iterable(Stream<T> stream)
    {
        return iterable(stream.iterator());
    }
    public static <T> Iterable<T> iterable(Iterator<T> iterator)
    {
        return () -> iterator;
    }
}
