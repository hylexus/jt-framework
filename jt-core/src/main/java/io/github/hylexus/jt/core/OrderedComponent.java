package io.github.hylexus.jt.core;

/**
 * @author hylexus
 */
public interface OrderedComponent {

    int DEFAULT_ORDER = 0;

    int ANNOTATION_BASED_DEV_COMPONENT_ORDER = 100;

    int BUILTIN_COMPONENT_ORDER = 200;

    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;


    default int getOrder() {
        return DEFAULT_ORDER;
    }

}
