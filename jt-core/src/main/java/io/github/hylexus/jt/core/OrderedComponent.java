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

    int EXCEPTION_HANDLER_CUSTOMER_INTERFACE_BASED = -100;
    int EXCEPTION_HANDLER_CUSTOMER_ANNOTATION_BASED = 0;
    int EXCEPTION_HANDLER_BUILTIN_INTERFACE_BASED = 100;
    int EXCEPTION_HANDLER_BUILTIN_ANNOTATION_BASED = 200;

    default int getOrder() {
        return DEFAULT_ORDER;
    }

}
