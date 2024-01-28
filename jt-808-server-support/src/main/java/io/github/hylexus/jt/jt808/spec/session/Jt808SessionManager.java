package io.github.hylexus.jt.jt808.spec.session;

import java.util.List;

/**
 * Session管理器。
 * <p>
 * 如有必要，你可以自定义该类的实现，并注入到Spring容器中以替换默认实现。
 * <p>
 * Created At 2020-06-20 15:53
 *
 * @author hylexus
 */
public interface Jt808SessionManager extends InternalJt808SessionManager {

    Jt808SessionManager addListener(Jt808SessionEventListener listener);

    List<Jt808SessionEventListener> getListeners();
}
