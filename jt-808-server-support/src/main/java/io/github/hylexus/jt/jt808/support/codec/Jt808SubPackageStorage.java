package io.github.hylexus.jt.jt808.support.codec;

import io.github.hylexus.jt.core.OrderedComponent;
import io.github.hylexus.jt.jt808.spec.Jt808SubPackageRequest;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808SubPackageStorage.LOG_PREFIX;

/**
 * @author hylexus
 */
public interface Jt808SubPackageStorage {

    Optional<ByteBuf> getAllSubPackages(Jt808SubPackageRequest request);

    void storeSubPackage(Jt808SubPackageRequest request);

    void addListener(SubPackageEventListener listener);

    interface SubPackageEventListener extends OrderedComponent {
        void onEvent(Event event);
    }

    @Slf4j
    class DefaultDebuggingSubPackageEventListener implements SubPackageEventListener {
        @Override
        public void onEvent(Event event) {
            log.info("{} {} {}", LOG_PREFIX, event.data(), event.eventType().desc());
        }
    }

    @Data
    @Accessors(chain = true, fluent = true)
    @AllArgsConstructor
    @NoArgsConstructor
    class Event {
        EventType eventType;
        Jt808SubPackageRequest.Jt808SubPackage data;
    }

    interface EventType {
        String desc();

        EventType PACKAGE_ARRIVED = () -> "PACKAGE_ARRIVED";
        EventType PACKAGE_EXPIRED = () -> "PACKAGE_EXPIRED";
        EventType PACKAGE_CONSUMED = () -> "PACKAGE_CONSUMED";
    }

}
