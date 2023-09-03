package io.github.hylexus.jt.core.registry;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public interface RegistryStore<I, E> {
    E put(I id, E e);

    default E compute(I id, BiFunction<I, E, E> remappingFn) {
        final E e = this.find(id).orElse(null);
        final E applied = remappingFn.apply(id, e);
        this.put(id, applied);
        return applied;
    }

    E computeIfAbsent(I id, Function<I, E> mappingFn);

    E putIfAbsent(I id, E value);

    Optional<E> find(I id);

    Optional<E> remove(I id);

    Optional<E> replace(I id, E newEntry);

    Stream<E> values();

}
