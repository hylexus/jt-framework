package io.github.hylexus.jt.core.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageableVo<T> {
    private long total;
    private List<T> records;

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
