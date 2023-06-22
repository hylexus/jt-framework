package io.github.hylexus.jt.jt1078.spec;

import lombok.Getter;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.UUID;

public interface Jt1078Subscriber<S extends Jt1078Subscription> {

    /**
     * 这个 uuid 可以用来从外部关闭当前订阅
     *
     * @return uuid
     */
    String id();

    /**
     * @return 当前订阅的数据流
     */
    Flux<S> dataStream();

    @Getter
    class SubscriberKey {
        private final String sim;
        private final short channel;

        public static SubscriberKey of(Jt1078Request request) {
            return new SubscriberKey(request.sim(), request.channelNumber());
        }

        public static SubscriberKey of(String sim, short channel) {
            return new SubscriberKey(sim, channel);
        }

        public static String ofUuid(String sim, short channel) {
            return prefix(sim, channel) + UUID.randomUUID().toString().replaceAll("-", "");
        }

        public static String prefix(String sim, short channel) {
            return sim + "_" + channel + "_";
        }

        public SubscriberKey(String sim, short channel) {
            this.sim = sim;
            this.channel = channel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final SubscriberKey that = (SubscriberKey) o;
            return channel == that.channel && Objects.equals(sim, that.sim);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sim, channel);
        }

        @Override
        public String toString() {
            return "SubscriberKey{"
                    + "'" + sim + '\''
                    + "/" + channel
                    + '}';
        }
    }

}
