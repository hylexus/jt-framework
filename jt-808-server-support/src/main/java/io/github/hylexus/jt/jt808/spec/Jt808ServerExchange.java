package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author hylexus
 */
@BuiltinComponent
public interface Jt808ServerExchange {

    Jt808Request request();

    Jt808Response response();

    Jt808Session session();

    Map<String, Object> getAttributes();

    default Jt808ServerExchange setAttribute(String key, Object value) {
        getAttributes().put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    default <T> T getAttribute(String name) {
        return (T) getAttributes().get(name);
    }

    default <T> T getRequiredAttribute(String name) {
        T value = getAttribute(name);
        Objects.requireNonNull(value, () -> "Required attribute '" + name + "' is missing");
        return value;
    }

    @SuppressWarnings("unchecked")
    default <T> T getAttributeOrDefault(String name, T defaultValue) {
        return (T) getAttributes().getOrDefault(name, defaultValue);
    }

    @SuppressWarnings("unchecked")
    default <T> T getAttributeOrDefault(String name, Supplier<T> supplier) {
        return (T) getAttributes().getOrDefault(name, supplier.get());
    }

}
