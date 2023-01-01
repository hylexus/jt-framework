package io.github.hylexus.jt.jt1078.spec;

import lombok.NoArgsConstructor;

import java.util.Objects;

public interface Jt1078SubscriptionSelector {

    String sim();

    Short channelNumber();

    Jt1078PayloadType sourceType();

    Jt1078SubscriptionType targetType();

    @Override
    int hashCode();

    @Override
    boolean equals(Object another);

    @Override
    String toString();

    static Jt1078SubscriptionSelectorBuilder newBuilder() {
        return new DefaultJt1078SubscriptionSelector();
    }

    interface Jt1078SubscriptionSelectorBuilder {
        Jt1078SubscriptionSelectorBuilder sim(String sim);

        Jt1078SubscriptionSelectorBuilder channelNumber(Short channel);

        Jt1078SubscriptionSelectorBuilder sourceType(Jt1078PayloadType sourceType);

        Jt1078SubscriptionSelectorBuilder targetType(Jt1078SubscriptionType targetType);

        Jt1078SubscriptionSelector build();
    }

    @NoArgsConstructor
    class DefaultJt1078SubscriptionSelector implements Jt1078SubscriptionSelector, Jt1078SubscriptionSelectorBuilder {
        private String sim;
        private Short channelNumber;
        private Jt1078PayloadType sourceType;
        private Jt1078SubscriptionType targetType;

        public DefaultJt1078SubscriptionSelector(String sim, Short channelNumber, Jt1078PayloadType sourceType, Jt1078SubscriptionType targetType) {
            this.sim = sim;
            this.channelNumber = channelNumber;
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        @Override
        public Jt1078SubscriptionSelectorBuilder sim(String sim) {
            this.sim = sim;
            return this;
        }

        @Override
        public String sim() {
            return this.sim;
        }

        @Override
        public Jt1078SubscriptionSelectorBuilder channelNumber(Short channel) {
            this.channelNumber = channel;
            return this;
        }

        @Override
        public Short channelNumber() {
            return this.channelNumber;
        }

        @Override
        public Jt1078SubscriptionSelectorBuilder sourceType(Jt1078PayloadType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        @Override
        public Jt1078PayloadType sourceType() {
            return this.sourceType;
        }

        @Override
        public Jt1078SubscriptionSelectorBuilder targetType(Jt1078SubscriptionType targetType) {
            this.targetType = targetType;
            return this;
        }

        @Override
        public Jt1078SubscriptionType targetType() {
            return this.targetType;
        }

        @Override
        public Jt1078SubscriptionSelector build() {
            return new DefaultJt1078SubscriptionSelector(this.sim, this.channelNumber, this.sourceType, this.targetType);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final DefaultJt1078SubscriptionSelector that = (DefaultJt1078SubscriptionSelector) o;
            return Objects.equals(sim, that.sim)
                    && Objects.equals(channelNumber, that.channelNumber)
                    // TODO value and code
                    && Objects.equals(sourceType, that.sourceType)
                    && Objects.equals(targetType, that.targetType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sim, channelNumber, sourceType, targetType);
        }

        @Override
        public String toString() {
            return "Selector{"
                    + "sim='" + sim + '\''
                    + ", channelNumber=" + channelNumber
                    + ", sourceType=" + sourceType
                    + ", targetType=" + targetType
                    + '}';
        }
    }
}