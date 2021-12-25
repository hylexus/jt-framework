package io.github.hylexus.jt.jt808.spec.session;

/**
 * @author hylexus
 */
@FunctionalInterface
public interface Jt808FlowIdGeneratorFactory {

    Jt808FlowIdGenerator create();


    class DefaultJt808FlowIdGeneratorFactory implements Jt808FlowIdGeneratorFactory {

        @Override
        public Jt808FlowIdGenerator create() {
            return new DefaultJt808FlowIdGenerator();
        }
    }
}
