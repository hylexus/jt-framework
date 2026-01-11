package io.github.hylexus.jt.jt808.support.dispatcher.handler.reflection;

import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.support.annotation.msg.DrivenBy;
import io.github.hylexus.jt.jt808.support.annotation.msg.req.Jt808RequestBody;
import io.github.hylexus.jt.utils.JtAnnotationUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * @author hylexus
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(of = {"index", "parameterType"})
public class MethodParameter {
    private int index;
    private Class<?> parameterType;
    private List<Class<?>> genericType;
    private Method method;

    DrivenBy drivenBy;

    public MethodParameter() {
    }

    public void afterInit() {
        if (Jt808RequestEntity.class.isAssignableFrom(this.parameterType)) {
            final Class<?> first = this.genericType.get(0);
            final Jt808RequestBody annotation = JtAnnotationUtils.getMergedAnnotation(first, Jt808RequestBody.class);
            if (annotation != null) {
                this.drivenBy = annotation.drivenBy();
            }
        } else if (JtAnnotationUtils.getMergedAnnotation(this.parameterType, Jt808RequestBody.class) != null) {
            final Jt808RequestBody annotation = JtAnnotationUtils.getMergedAnnotation(this.parameterType, Jt808RequestBody.class);
            this.drivenBy = Objects.requireNonNull(annotation).drivenBy();
        }

        if (drivenBy == null) {
            drivenBy = PLACEHOLDER_JT_808_REQUEST_BODY.drivenBy();
        }
    }

    public @NonNull DrivenBy getDrivenBy() {
        return drivenBy;
    }

    static final Jt808RequestBody PLACEHOLDER_JT_808_REQUEST_BODY = PlaceHolder.class.getAnnotation(Jt808RequestBody.class);

    @Jt808RequestBody(drivenBy = @DrivenBy(DrivenBy.Type.DEFAULT))
    private static class PlaceHolder {
    }

    public boolean drivenByXtreamCodec() {
        return drivenBy.value() == DrivenBy.Type.XTREAM_CODEC;
    }

}
