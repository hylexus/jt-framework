package io.github.hylexus.jt.jt808.boot.diagnostics;

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

import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static io.github.hylexus.jt.jt808.JtProtocolConstant.BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class Jt808EventExecutorGroupProviderFailureAnalyzerTest {

    @Test
    void shouldAnalyzeLegacyInstructionExecutorBean() throws Exception {
        final FailureAnalysis analysis = this.analyzeLegacyBean(
                BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP,
                "instructionProvider"
        );

        this.assertMigrationAnalysis(analysis, BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP);
    }

    @Test
    void shouldAnalyzeLegacyAttachmentExecutorBean() throws Exception {
        final FailureAnalysis analysis = this.analyzeLegacyBean(
                BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP,
                "attachmentProvider"
        );

        this.assertMigrationAnalysis(analysis, BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP);
    }

    @Test
    void shouldIgnoreUnrelatedProviderFailure() throws Exception {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("otherProvider", mock(EventExecutorGroup.class));
        final Jt808EventExecutorGroupProviderFailureAnalyzer analyzer =
                new Jt808EventExecutorGroupProviderFailureAnalyzer(beanFactory);

        assertNull(analyzer.analyze(this.failure("otherProvider")));
    }

    @Test
    void shouldIgnoreSupportedNameWithNonLegacyBeanType() throws Exception {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP, new Object());
        final Jt808EventExecutorGroupProviderFailureAnalyzer analyzer =
                new Jt808EventExecutorGroupProviderFailureAnalyzer(beanFactory);

        assertNull(analyzer.analyze(this.failure("instructionProvider")));
    }

    @Test
    void shouldIgnoreFailureForAnotherMissingType() throws Exception {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP, mock(EventExecutorGroup.class));
        final Jt808EventExecutorGroupProviderFailureAnalyzer analyzer =
                new Jt808EventExecutorGroupProviderFailureAnalyzer(beanFactory);
        final UnsatisfiedDependencyException failure = this.failure(
                "instructionProvider",
                new NoSuchBeanDefinitionException(String.class)
        );

        assertNull(analyzer.analyze(failure));
    }

    @Test
    void shouldBeRegisteredAsFailureAnalyzer() {
        final List<String> analyzerNames = SpringFactoriesLoader.loadFactoryNames(
                FailureAnalyzer.class,
                getClass().getClassLoader()
        );

        assertTrue(analyzerNames.contains(Jt808EventExecutorGroupProviderFailureAnalyzer.class.getName()));
    }

    private FailureAnalysis analyzeLegacyBean(String beanName, String injectionMethodName) throws Exception {
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton(beanName, mock(EventExecutorGroup.class));
        final Jt808EventExecutorGroupProviderFailureAnalyzer analyzer =
                new Jt808EventExecutorGroupProviderFailureAnalyzer(beanFactory);

        return analyzer.analyze(this.failure(injectionMethodName));
    }

    private UnsatisfiedDependencyException failure(String injectionMethodName) throws Exception {
        return this.failure(
                injectionMethodName,
                new NoSuchBeanDefinitionException(JtEventExecutorGroupProvider.class)
        );
    }

    private UnsatisfiedDependencyException failure(
            String injectionMethodName,
            NoSuchBeanDefinitionException cause) throws Exception {
        final Method method = InjectionPoints.class.getDeclaredMethod(
                injectionMethodName,
                JtEventExecutorGroupProvider.class
        );
        final InjectionPoint injectionPoint = new InjectionPoint(new MethodParameter(method, 0));
        return new UnsatisfiedDependencyException("test", "testBean", injectionPoint, cause);
    }

    private void assertMigrationAnalysis(FailureAnalysis analysis, String beanName) {
        final String lineSeparator = System.lineSeparator();
        assertNotNull(analysis);
        assertTrue(analysis.getDescription().contains(beanName));
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
                        + "    " + beanName + lineSeparator + lineSeparator
                        + "Change the @Bean method's return type to:" + lineSeparator
                        + "    " + JtEventExecutorGroupProvider.class.getName() + lineSeparator + lineSeparator
                        + "Wrap the executor with:" + lineSeparator
        ));
        assertTrue(analysis.getAction().contains(
                "Migration details:" + lineSeparator
                        + "    https://github.com/hylexus/jt-framework/issues/101"
        ));
        assertSame(NoSuchBeanDefinitionException.class, analysis.getCause().getClass());
        assertEquals(JtEventExecutorGroupProvider.class,
                ((NoSuchBeanDefinitionException) analysis.getCause()).getBeanType());
    }

    static class InjectionPoints {

        void instructionProvider(
                @Qualifier(BEAN_NAME_JT808_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
                JtEventExecutorGroupProvider provider) {
        }

        void attachmentProvider(
                @Qualifier(BEAN_NAME_JT808_ATTACHMENT_MSG_PROCESSOR_EVENT_EXECUTOR_GROUP)
                JtEventExecutorGroupProvider provider) {
        }

        void otherProvider(@Qualifier("otherProvider") JtEventExecutorGroupProvider provider) {
        }
    }
}
