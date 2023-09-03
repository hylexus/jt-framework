package io.github.hylexus.jt.core.model.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Resp<T> {

    private int code;

    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Resp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean success() {
        return this.getCode() == DefaultRespCode.SUCCESS.code();
    }

    public static <E> Resp<E> success(E data) {
        return of(DefaultRespCode.SUCCESS, data);
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

    public static <E> Resp<E> parameterError(String msg) {
        return new Resp<>(DefaultRespCode.PARAMETER_ERROR.code(), msg == null ? DefaultRespCode.PARAMETER_ERROR.msg() : msg, null);
    }

    public static <E> Resp<E> notFound() {
        return notFound(null);
    }

    public static <E> Resp<E> notFound(String msg) {
        return new Resp<>(DefaultRespCode.NOT_FOUND.code(), msg == null ? DefaultRespCode.NOT_FOUND.msg() : msg, null);
    }


    public static <E> boolean isFailure(Resp<E> resp) {
        return !isSuccess(resp);
    }

    public static <E> boolean isSuccess(Resp<E> resp) {
        return resp != null && resp.getCode() == DefaultRespCode.SUCCESS.code();
    }
}
