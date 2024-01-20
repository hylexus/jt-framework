package io.github.hylexus.jt.jt808.support.extension.attachment;

import io.github.hylexus.jt.jt808.spec.session.InternalJt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionEventListener;

import java.util.List;

public interface AttachmentJt808SessionManager extends InternalJt808SessionManager {

    AttachmentJt808SessionManager addListener(Jt808SessionEventListener listener);

    List<Jt808SessionEventListener> getListeners();
}
