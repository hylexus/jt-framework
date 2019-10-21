package io.github.hylexus.jt.data.converter;

/**
 * @author hylexus
 * Created At 2019-10-19 9:59 下午
 */
@FunctionalInterface
public interface DataTypeConverter<S, T> {

    /**
     * source ==> target
     *
     * @param source source type
     * @return target type
     */
    T convertTo(S source);

}
