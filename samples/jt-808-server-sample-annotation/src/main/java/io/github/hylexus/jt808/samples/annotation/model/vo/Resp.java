package io.github.hylexus.jt808.samples.annotation.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Resp<T> {
    private final int code;
    private final String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    public Resp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <E> Resp<E> of(RespCode code, E data) {
        return new Resp<>(code.code(), code.msg(), data);
    }

    public static <E> Resp<E> failure(RespCode code) {
        return failure(code, null);
    }

    public static <E> Resp<E> failure(RespCode code, String msg) {
        return new Resp<>(code.code(), msg == null ? code.msg() : msg, null);
    }

    public static <E> Resp<E> success(E data) {
        return of(DefaultRespCode.SUCCESS, data);
    }

    public static <E> Resp<E> empty() {
        return empty(null);
    }

    public static <E> Resp<E> empty(String msg) {
        return new Resp<>(DefaultRespCode.EMPTY_DATA.code(), msg == null ? DefaultRespCode.EMPTY_DATA.msg() : msg, null);
    }
}
