package io.github.hylexus.jt.core.registry;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Stream;

public class SimpleInMemoryRegistryStore<I, E> implements RegistryStore<I, E> {
    private final ConcurrentMap<I, E> map;

    public SimpleInMemoryRegistryStore() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public E put(I id, E e) {
        return this.map.put(id, e);
    }

    @Override
    public E computeIfAbsent(I id, Function<I, E> mappingFn) {
        return this.map.computeIfAbsent(id, mappingFn);
    }

    @Override
    public E putIfAbsent(I id, E value) {
        return this.map.putIfAbsent(id, value);
    }

    @Override
    public Optional<E> find(I id) {
        return Optional.ofNullable(this.map.get(id));
    }

    @Override
    public Optional<E> remove(I id) {
        return Optional.ofNullable(this.map.remove(id));
    }

    @Override
    public Optional<E> replace(I id, E newEntry) {
        return Optional.ofNullable(this.map.replace(id, newEntry));
    }

    @Override
    public Stream<E> values() {
        return this.map.values().stream();
    }
}
