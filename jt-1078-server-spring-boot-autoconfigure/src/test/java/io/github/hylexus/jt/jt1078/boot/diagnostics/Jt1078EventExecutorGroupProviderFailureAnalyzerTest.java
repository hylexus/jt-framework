package io.github.hylexus.jt.jt1078.boot.diagnostics;

import io.github.hylexus.jt.netty.JtEventExecutorGroupProvider;
import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.lang.reflect.Method;
import java.util.List;

import static io.github.hylexus.jt.jt1078.Jt1078ProtocolConstant.BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class Jt1078EventExecutorGroupProviderFailureAnalyzerTest {

    @Test
    void shouldAnalyzeLegacyExecutorBean() throws Exception {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton(
                BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP,
                mock(EventExecutorGroup.class)
        );
        final Jt1078EventExecutorGroupProviderFailureAnalyzer analyzer =
                new Jt1078EventExecutorGroupProviderFailureAnalyzer(beanFactory);

        final FailureAnalysis analysis = analyzer.analyze(this.failure("jt1078Provider"));

        final String lineSeparator = System.lineSeparator();
        assertNotNull(analysis);
        assertTrue(analysis.getDescription().contains(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP));
        assertTrue(analysis.getDescription().contains(EventExecutorGroup.class.getName()));
        assertTrue(analysis.getDescription().contains("auto-configuration to back off"));
        assertTrue(analysis.getDescription().contains(
                "legacy executor contract:" + lineSeparator
                        + "    Bean type: "
        ));
        assertTrue(analysis.getDescription().contains(
                "    Legacy type: " + EventExecutorGroup.class.getName() + lineSeparator + lineSeparator
                        + "Since jt-framework 3.0.0, this bean name must identify:" + lineSeparator
                        + "    " + JtEventExecutorGroupProvider.class.getName() + lineSeparator + lineSeparator
        ));
        assertTrue(analysis.getAction().contains(JtEventExecutorGroupProvider.class.getName()));
        assertTrue(analysis.getAction().contains(
                "Keep the existing bean name:" + lineSeparator
                        + "    " + BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP + lineSeparator + lineSeparator
                        + "Change the @Bean method's return type to:" + lineSeparator
                        + "    " + JtEventExecutorGroupProvider.class.getName() + lineSeparator + lineSeparator
                        + "Wrap the executor with:" + lineSeparator
        ));
        assertTrue(analysis.getAction().contains(
                "Migration details:" + lineSeparator
                        + "    https://github.com/hylexus/jt-framework/issues/101"
        ));
    }

    @Test
    void shouldIgnoreUnrelatedProviderFailure() throws Exception {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("otherProvider", mock(EventExecutorGroup.class));
        final Jt1078EventExecutorGroupProviderFailureAnalyzer analyzer =
                new Jt1078EventExecutorGroupProviderFailureAnalyzer(beanFactory);

        assertNull(analyzer.analyze(this.failure("otherProvider")));
    }

    @Test
    void shouldIgnoreSupportedNameWithNonLegacyBeanType() throws Exception {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP, new Object());
        final Jt1078EventExecutorGroupProviderFailureAnalyzer analyzer =
                new Jt1078EventExecutorGroupProviderFailureAnalyzer(beanFactory);

        assertNull(analyzer.analyze(this.failure("jt1078Provider")));
    }

    @Test
    void shouldBeRegisteredAsFailureAnalyzer() {
        final List<String> analyzerNames = SpringFactoriesLoader.loadFactoryNames(
                FailureAnalyzer.class,
                getClass().getClassLoader()
        );

        assertTrue(analyzerNames.contains(Jt1078EventExecutorGroupProviderFailureAnalyzer.class.getName()));
    }

    private UnsatisfiedDependencyException failure(String injectionMethodName) throws Exception {
        final Method method = InjectionPoints.class.getDeclaredMethod(
                injectionMethodName,
                JtEventExecutorGroupProvider.class
        );
        final InjectionPoint injectionPoint = new InjectionPoint(new MethodParameter(method, 0));
        return new UnsatisfiedDependencyException(
                "test",
                "testBean",
                injectionPoint,
                new NoSuchBeanDefinitionException(JtEventExecutorGroupProvider.class)
        );
    }

    static class InjectionPoints {

        void jt1078Provider(
                @Qualifier(BEAN_NAME_1078_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
                JtEventExecutorGroupProvider provider) {
        }

        void otherProvider(@Qualifier("otherProvider") JtEventExecutorGroupProvider provider) {
        }
    }
}
