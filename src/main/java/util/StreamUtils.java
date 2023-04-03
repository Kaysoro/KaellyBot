package util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StreamUtils {

    public static <T> Stream<T> toStream(Iterator<T> iterator){
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }
}
