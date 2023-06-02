package io.github.hylexus.jt.jt1078.spec;

import io.github.hylexus.jt.core.OrderedComponent;

import javax.annotation.Nullable;

public interface Jt1078SessionEventListener extends OrderedComponent {


    default void onSessionAdd(@Nullable Jt1078Session session) {
    }

    default void onSessionRemove(@Nullable Jt1078Session session) {
    }

    default void onSessionClose(@Nullable Jt1078Session session, Jt1078SessionCloseReason closeReason) {
    }

}
