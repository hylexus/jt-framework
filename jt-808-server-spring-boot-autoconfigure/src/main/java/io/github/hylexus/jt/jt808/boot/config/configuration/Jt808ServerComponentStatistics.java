package io.github.hylexus.jt.jt808.boot.config.configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.*;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.codec.*;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.dispatcher.handler.Jt808RequestHandlerReporter;
import io.github.hylexus.jt.jt808.support.netty.Jt808ServerNettyConfigure;
import io.github.hylexus.jt.jt808.support.netty.Jt808TerminalHeatBeatHandler;
import io.github.hylexus.jt.utils.CommonUtils;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.hylexus.jt.utils.CommonUtils.isBuiltinComponent;
import static org.springframework.boot.ansi.AnsiColor.BRIGHT_BLACK;

/**
 * @author hylexus
 */
@Slf4j
@Order(200)
public class Jt808ServerComponentStatistics implements CommandLineRunner, ApplicationContextAware {

    public static final AnsiColor SERVER_BANNER_COLOR = AnsiColor.BRIGHT_BLUE;
    public static final AnsiColor BUILTIN_COMPONENT_COLOR = AnsiColor.BRIGHT_CYAN;
    public static final AnsiColor CUSTOM_COMPONENT_COLOR = AnsiColor.GREEN;
    public static final AnsiColor DEPRECATED_COMPONENT_COLOR = AnsiColor.RED;
    public static final AnsiColor UNKNOWN_COMPONENT_TYPE_COLOR = AnsiColor.BRIGHT_RED;

    private static final String END_OF_LINE = "\n";
    private final Set<Class<?>> classSet = Sets.newLinkedHashSet(
            Lists.newArrayList(
                    Jt808MsgTypeParser.class,
                    Jt808ProtocolVersionDetectorRegistry.class,
                    Jt808MsgDecoder.class,
                    Jt808MsgEncoder.class,
                    Jt808MsgBytesProcessor.class,
                    Jt808CommandSender.class,
                    Jt808SessionManager.class,
                    Jt808RequestSubPackageStorage.class,
                    Jt808ResponseSubPackageStorage.class,
                    Jt808FieldDeserializerRegistry.class,
                    Jt808FieldSerializerRegistry.class,
                    Jt808TerminalHeatBeatHandler.class,
                    Jt808ServerNettyConfigure.class,
                    //Jt808RequestMsgQueue.class,
                    Jt808RequestMsgQueueListener.class,
                    Jt808MsgEncryptionHandler.class
            )
    );
    @Setter
    private ApplicationContext applicationContext;

    public Jt808ServerComponentStatistics() {
    }

    @Override
    public void run(String... args) {

        final StringBuilder stringBuilder = new StringBuilder();

        appendBannerPrefix(stringBuilder);
        stringBuilder.append(END_OF_LINE);
        stringBuilder.append(line(1, "RequestHandler MappingInfo:")).append(END_OF_LINE);
        appendRequestHandlerMappingInfo(stringBuilder);
        stringBuilder
                .append(END_OF_LINE).append(line(2, "Configurable Components:")).append(END_OF_LINE)
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append(END_OF_LINE);

        for (Class<?> cls : classSet) {
            stringBuilder.append(String.format("%1$-36s", cls.getSimpleName()))
                    .append("|  ")
                    .append(formatClassName(applicationContext.getBean(cls)))
                    .append(END_OF_LINE);
        }
        stringBuilder
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append(END_OF_LINE);

        appendBannerSuffix(stringBuilder);

        log.info(stringBuilder.toString());
    }

    private void appendRequestHandlerMappingInfo(StringBuilder stringBuilder) {
        stringBuilder.append(String.format("%1$-30s\t|\t%2$s\n", "MsgId [Version]  (MsgDesc)", "RequestHandler"));
        stringBuilder.append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------").append(END_OF_LINE);

        final Map<Integer, Map<Jt808ProtocolVersion, Jt808RequestHandlerReporter.RequestMappingReporter>> map = new HashMap<>();

        applicationContext.getBeansOfType(Jt808RequestHandlerReporter.class)
                .values()
                .stream()
                .flatMap(Jt808RequestHandlerReporter::report)
                .forEach(reporter -> {
                    final int msgId = reporter.getMsgType().getMsgId();
                    final Map<Jt808ProtocolVersion, Jt808RequestHandlerReporter.RequestMappingReporter> msgIdMapping
                            = map.computeIfAbsent(msgId, integer -> new HashMap<>());

                    final Jt808RequestHandlerReporter.RequestMappingReporter old = msgIdMapping.computeIfAbsent(reporter.getVersion(), k -> reporter);
                    if (old != reporter && old.shouldBeReplacedBy(reporter)) {
                        msgIdMapping.put(reporter.getVersion(), reporter);
                    }
                });

        map.values().stream().flatMap(entry -> entry.values().stream())
                .sorted(Comparator
                        .comparing((Jt808RequestHandlerReporter.RequestMappingReporter it) -> it.getMsgType().getMsgId())
                        .thenComparing((Jt808RequestHandlerReporter.RequestMappingReporter it) -> it.getVersion().getShortDesc())
                ).forEach(reporter -> {
                    final String msgDesc = String.format("%s [%-4s]  (%s) ",
                            HexStringUtils.int2HexString(reporter.getMsgType().getMsgId(), 4),
                            reporter.getVersion().getShortDesc(), reporter.getMsgType().getDesc()

                    );
                    final String handlerDesc = formatHandlerName(reporter.getHandlerMethod());
                    stringBuilder.append(String.format("%1$-30s\t| %2$-120s", msgDesc, handlerDesc)).append(END_OF_LINE);
                });
        stringBuilder.append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------").append(END_OF_LINE);
    }

    private void appendBannerSuffix(StringBuilder stringBuilder) {
        stringBuilder.append(END_OF_LINE)
                .append(AnsiOutput.toString(SERVER_BANNER_COLOR, ">>|<< Jt808-Server-Component-Statistics >>|<<\n"));
    }

    private void appendBannerPrefix(StringBuilder stringBuilder) {
        stringBuilder
                .append(END_OF_LINE)
                .append(END_OF_LINE)
                .append(AnsiOutput.toString(SERVER_BANNER_COLOR, ">>|<< Jt808-Server-Component-Statistics >>|<<"))
                .append(END_OF_LINE);
        stringBuilder.append(AnsiOutput.toString(BUILTIN_COMPONENT_COLOR, "[(B)uiltin-Component]"))
                .append(AnsiOutput.toString(CUSTOM_COMPONENT_COLOR, " [(C)ustom-Component] "))
                .append(AnsiOutput.toString(DEPRECATED_COMPONENT_COLOR, "[(D)eprecated-Component] "))
                .append(AnsiOutput.toString(UNKNOWN_COMPONENT_TYPE_COLOR, "[(U)nknown-Component]"))
                .append(END_OF_LINE);
    }

    private String line(int no, String content) {
        return AnsiOutput.toString(BRIGHT_BLACK, ">|< ", no, ". ", AnsiColor.DEFAULT, content);
    }

    private AnsiColor detectColor(Class<?> cls) {
        Class<?> userClass = ClassUtils.getUserClass(cls);
        if (CommonUtils.isDeprecatedComponent(userClass)) {
            return DEPRECATED_COMPONENT_COLOR;
        }
        if (isBuiltinComponent(userClass)) {
            return BUILTIN_COMPONENT_COLOR;
        }

        return CUSTOM_COMPONENT_COLOR;
    }

    private AnsiColor detectColor(Method method) {
        if (CommonUtils.isDeprecatedComponent(method)) {
            return DEPRECATED_COMPONENT_COLOR;
        }
        if (isBuiltinComponent(method)) {
            return BUILTIN_COMPONENT_COLOR;
        }

        return CUSTOM_COMPONENT_COLOR;
    }

    private String formatClassName(Object instance) {
        if (instance == null) {
            return AnsiOutput.toString(UNKNOWN_COMPONENT_TYPE_COLOR, "(U) NULL");
        }
        final Class<?> userClass = ClassUtils.getUserClass(instance);
        final AnsiColor color = detectColor(userClass);
        return AnsiOutput.toString(color, componentPrefix(color) + userClass.getName());
    }

    private String formatHandlerName(Method method) {
        if (method == null) {
            return AnsiOutput.toString(UNKNOWN_COMPONENT_TYPE_COLOR, "(U) NULL");
        }
        final Class<?> userClass = method.getDeclaringClass();
        final AnsiColor handlerColor = detectColor(userClass);
        final String paramList;
        if (method.getParameters() != null) {
            paramList = Stream.of(method.getParameters())
                    .map(parameter -> {
                        final AnsiColor color = detectColor(parameter.getType());
                        // Generic in param ...
                        final Type parameterizedType = parameter.getParameterizedType();
                        if (parameterizedType instanceof ParameterizedType && ((ParameterizedType) parameterizedType).getActualTypeArguments() != null) {
                            final String paramInfo = Stream.of(((ParameterizedType) parameterizedType).getActualTypeArguments())
                                    .map(actualTypeArgument -> (Class<?>) actualTypeArgument)
                                    .map(actualTypeArgument -> {
                                        final AnsiColor genericColor = detectColor(actualTypeArgument);
                                        return AnsiOutput.toString(genericColor, actualTypeArgument.getSimpleName(), AnsiColor.DEFAULT);
                                    }).collect(Collectors.joining(", ", "<", ">"));

                            return AnsiOutput.toString(color, parameter.getType().getSimpleName(), paramInfo, AnsiColor.DEFAULT);
                        }
                        return AnsiOutput.toString(color, parameter.getType().getSimpleName(), AnsiColor.DEFAULT);
                    }).collect(Collectors.joining(", "));
        } else {
            paramList = "";
        }
        return AnsiOutput.toString(
                handlerColor,
                componentPrefix(handlerColor) + userClass.getSimpleName() + "#" + method.getName() + "(" + paramList + ")"
        );
    }

    private String componentPrefix(AnsiColor color) {
        if (color == BUILTIN_COMPONENT_COLOR) {
            return "(B) ";
        }
        if (color == CUSTOM_COMPONENT_COLOR) {
            return "(C) ";
        }
        if (color == DEPRECATED_COMPONENT_COLOR) {
            return "(D) ";
        }
        if (color == UNKNOWN_COMPONENT_TYPE_COLOR) {
            return "(U) ";
        }

        return "";
    }

    private Class<?> tryGetGenericType(Type[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return null;
        }
        // ignore WildcardType
        // ?, ? extends Number, or ? super Integer
        if (arguments[0] instanceof WildcardType) {
            return null;
        }
        return arguments[0] instanceof Class ? (Class<?>) arguments[0] : null;
    }

    private String formatMsgType(MsgType msgType) {
        return String.format("%s (%s)", HexStringUtils.int2HexString(msgType.getMsgId(), 4, true), msgType.getDesc()).toUpperCase();
    }
}
