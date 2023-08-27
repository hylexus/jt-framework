package io.github.hylexus.jt.core.registry;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface RegistryStore<I, E> {
    E put(I id, E e);

    E computeIfAbsent(I id, Function<I, E> mappingFn);

    E putIfAbsent(I id, E value);

    Optional<E> find(I id);

    Optional<E> remove(I id);

    Optional<E> replace(I id, E newEntry);

    Stream<E> values();

}
