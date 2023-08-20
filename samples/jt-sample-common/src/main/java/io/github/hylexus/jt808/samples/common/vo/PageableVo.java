package io.github.hylexus.jt808.samples.common.vo;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PageableVo<T> {
    private final long total;
    private final List<T> records;

    private PageableVo(long total, List<T> records) {
        this.total = total;
        this.records = records;
    }

    public static <E> PageableVo<E> of(long total, List<E> records) {
        return new PageableVo<>(total, records);
    }

    public static <E> PageableVo<E> empty() {
        return new PageableVo<>(0L, new ArrayList<E>());
    }
}
