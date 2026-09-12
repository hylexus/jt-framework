package io.github.hylexus.jt.jt808.boot.diagnostics;

import io.github.hylexus.jt.netty.DefaultJtEventExecutorGroupProvider;
import io.github.hylexus.jt.netty.JtEventExecutorGroupProvider;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;

/**
 * 分析由旧版 808 消息处理线程池 Bean 导致的 Provider 注入失败。
 *
 * @author Codex
 * @author hylexus
 * @see <a href="https://github.com/hylexus/jt-framework/issues/101">issues#101</a>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Jt808EventExecutorGroupProviderFailureAnalyzer extends AbstractFailureAnalyzer<NoSuchBeanDefinitionException> {
    private static final String ISSUE_URL = "https://github.com/hylexus/jt-framework/issues/101";
    private static final Set<String> SUPPORTED_BEAN_NAMES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP,
            BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP
    )));

    private final ConfigurableListableBeanFactory beanFactory;

    public Jt808EventExecutorGroupProviderFailureAnalyzer(BeanFactory beanFactory) {
        Assert.isInstanceOf(ConfigurableListableBeanFactory.class, beanFactory);
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, NoSuchBeanDefinitionException cause) {
        if (!JtEventExecutorGroupProvider.class.equals(cause.getBeanType())) {
            return null;
        }

        final String beanName = this.findQualifiedBeanName(rootFailure);
        final Class<?> beanType = this.findLegacyBeanType(beanName);
        if (beanType == null) {
            return null;
        }

        final String description = String.format(
                "Bean '%s' uses the legacy executor contract:%n"
                        + "    Bean type: %s%n"
                        + "    Legacy type: %s%n%n"
                        + "Since jt-framework 3.0.0, this bean name must identify:%n"
                        + "    %s%n%n"
                        + "The existing bean caused the default Provider auto-configuration to back off,%n"
                        + "but it cannot satisfy the Netty configuration injection point.",
                beanName,
                beanType.getName(),
                EventExecutorGroup.class.getName(),
                JtEventExecutorGroupProvider.class.getName()
        );
        final String action = String.format(
                "Keep the existing bean name:%n"
                        + "    %s%n%n"
                        + "Change the @Bean method's return type to:%n"
                        + "    %s%n%n"
                        + "Wrap the executor with:%n"
                        + "    %s%n%n"
                        + "Migration details:%n"
                        + "    %s",
                beanName,
                JtEventExecutorGroupProvider.class.getName(),
                DefaultJtEventExecutorGroupProvider.class.getName(),
                ISSUE_URL
        );
        return new FailureAnalysis(description, action, cause);
    }

    private String findQualifiedBeanName(Throwable failure) {
        Throwable current = failure;
        while (current != null) {
            if (current instanceof UnsatisfiedDependencyException) {
                final InjectionPoint injectionPoint = ((UnsatisfiedDependencyException) current).getInjectionPoint();
                if (injectionPoint != null && JtEventExecutorGroupProvider.class.equals(injectionPoint.getDeclaredType())) {
                    final Qualifier qualifier = injectionPoint.getAnnotation(Qualifier.class);
                    if (qualifier != null && SUPPORTED_BEAN_NAMES.contains(qualifier.value())) {
                        return qualifier.value();
                    }
                }
            }
            current = current.getCause();
        }
        return null;
    }

    private Class<?> findLegacyBeanType(String beanName) {
        if (beanName == null || !this.beanFactory.containsBean(beanName)) {
            return null;
        }
        try {
            final Class<?> beanType = this.beanFactory.getType(beanName, false);
            return beanType != null && EventExecutorGroup.class.isAssignableFrom(beanType) ? beanType : null;
        } catch (BeansException ignored) {
            return null;
        }
    }
}
