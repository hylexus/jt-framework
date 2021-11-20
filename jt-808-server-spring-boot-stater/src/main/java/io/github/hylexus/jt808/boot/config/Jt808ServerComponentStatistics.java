package io.github.hylexus.jt808.boot.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;
import io.github.hylexus.jt808.converter.impl.CustomReflectionBasedRequestMsgBodyConverter;
import io.github.hylexus.jt808.dispatcher.CommandSender;
import io.github.hylexus.jt808.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.ext.TerminalValidator;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.CustomReflectionBasedRequestMsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.HandlerMethod;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt808.queue.RequestMsgQueueListener;
import io.github.hylexus.jt808.session.Jt808SessionManager;
import io.github.hylexus.jt808.session.Jt808SessionManagerEventListener;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.RequestMsgBodyConverterMapping;
import io.github.hylexus.jt808.support.netty.Jt808ServerNettyConfigure;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.hylexus.jt.utils.CommonUtils.*;
import static java.util.stream.Collectors.groupingBy;
import static org.springframework.boot.ansi.AnsiColor.BRIGHT_BLACK;

/**
 * @author hylexus
 * Created At 2019-08-27 9:25 下午
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
    private final RequestMsgBodyConverterMapping msgConverterMapping;
    private final MsgHandlerMapping msgHandlerMapping;
    private final MsgTypeParser msgTypeParser;
    private final Set<Class<?>> classSet = Sets.newLinkedHashSet(
            Lists.newArrayList(
                    Jt808SessionManagerEventListener.class,
                    Jt808SessionManager.class,
                    BytesEncoder.class,
                    MsgTypeParser.class,
                    TerminalValidator.class,
                    AuthCodeValidator.class,
                    RequestMsgDispatcher.class,
                    RequestMsgQueue.class,
                    RequestMsgQueueListener.class,
                    //Jt808ServerConfigure.class,
                    Jt808ServerNettyConfigure.class,
                    ResponseMsgBodyConverter.class,
                    HandlerMethodArgumentResolver.class,
                    CommandSender.class
            ));
    @Setter
    private ApplicationContext applicationContext;

    public Jt808ServerComponentStatistics(MsgTypeParser msgTypeParser, RequestMsgBodyConverterMapping msgConverterMapping,
                                          MsgHandlerMapping msgHandlerMapping) {
        this.msgTypeParser = msgTypeParser;
        this.msgConverterMapping = msgConverterMapping;
        this.msgHandlerMapping = msgHandlerMapping;
    }

    @Override
    public void run(String... args) {

        StringBuilder stringBuilder = new StringBuilder();

        appendBannerPrefix(stringBuilder);

        detectConvertAndHandlerMappings(1, stringBuilder);

        stringBuilder
                .append(END_OF_LINE).append(line(2, "Other Components:")).append(END_OF_LINE)
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append(END_OF_LINE);
        for (Class<?> cls : classSet) {
            stringBuilder.append(String.format("%1$-36s", cls.getSimpleName()))
                    .append("|  ")
                    .append(formatClassName(applicationContext.getBean(cls), false, false))
                    .append(END_OF_LINE);
        }
        stringBuilder
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append(END_OF_LINE);

        appendBannerSuffix(stringBuilder);

        log.info(stringBuilder.toString());
    }

    private void detectConvertAndHandlerMappings(int no, StringBuilder stringBuilder) {
        stringBuilder.append(line(no, "RequestMsgBodyConverter and MsgHandler MappingInfo:")).append(END_OF_LINE);
        stringBuilder.append(String.format("%1$-35s\t|  %2$-91s  |  %3$-30s\n", "MsgId (MsgDesc)  [Version]", "MsgConverter<RequestMsgBody>", "MsgHandler"));

        stringBuilder.append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------").append(END_OF_LINE);

        final Map<MsgType, List<MsgConverterAndHandlerMappingInfo>> mappings = initMappingInfo();

        mappings.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(MsgType::getMsgId)))
                .forEach(entry -> {
                    entry.getValue().stream().sorted(Comparator.comparing(e -> e.getVersion().getVersionBit()))
                            .forEach(mappingInfo -> {
                                MsgType msgType = entry.getKey();
                                String content = String.format(
                                        "%1$-30s\t|  %2$-125s  |  %3$-30s\n",
                                        formatMsgType(msgType) + " [" + mappingInfo.getVersion().getShortDesc() + "]",
                                        detectMsgConverter(mappingInfo),
                                        detectHandlerMethod(mappingInfo.getHandler(), msgType, mappingInfo.getVersion())
                                );
                                stringBuilder.append(content);
                            });
                });
        stringBuilder
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------").append(END_OF_LINE);

    }

    private String detectMsgConverter(MsgConverterAndHandlerMappingInfo mappingInfo) {
        return formatClassName(mappingInfo.getConverter(), false, true) + formatEntityClassName(mappingInfo);
    }

    private String detectHandlerMethod(MsgHandler<?> handler, MsgType msgType, Jt808ProtocolVersion version) {
        String prefix = "-";

        if (handler instanceof CustomReflectionBasedRequestMsgHandler) {
            final HandlerMethod handlerMethod = ((CustomReflectionBasedRequestMsgHandler) handler).getHandlerMethod(msgType, version);
            if (handlerMethod == null) {
                return "";
            }
            if (containsBean(handlerMethod.getBeanInstance())) {
                prefix = "+";
            }
            AnsiColor color = detectColor(handler.getClass());
            return prefix + formatClassName(handlerMethod.getBeanInstance())
                   + AnsiOutput.toString(color, "#" + handlerMethod.getMethod().getName());
        }

        if (containsBean(handler)) {
            prefix = "+";
        }
        return prefix + formatClassName(handler);
    }

    private boolean containsBean(Object handler) {
        if (handler == null) {
            return false;
        }
        String[] names = applicationContext.getBeanNamesForType(handler.getClass());
        return names != null && names.length != 0;
    }

    private String formatEntityClassName(MsgConverterAndHandlerMappingInfo mappingInfo) {
        final Class<?> entityClass = mappingInfo.getEntityClass();
        if (entityClass == null) {
            return formatEntityClass(DEPRECATED_COMPONENT_COLOR, "UNKNOWN");
        }

        final AnsiColor color = detectColor(entityClass);
        return formatEntityClass(color, entityClass.getSimpleName());
    }

    private String formatEntityClass(AnsiColor color, String simpleName) {
        return AnsiOutput.toString(DEPRECATED_COMPONENT_COLOR, "<", color, simpleName, DEPRECATED_COMPONENT_COLOR, ">");
    }

    private void appendBannerSuffix(StringBuilder stringBuilder) {
        stringBuilder.append(END_OF_LINE)
                .append(AnsiOutput.toString(SERVER_BANNER_COLOR, ">>|<< Jt808-Server-component-Statistics >>|<<\n"));
    }

    private void appendBannerPrefix(StringBuilder stringBuilder) {
        stringBuilder
                .append(END_OF_LINE)
                .append(END_OF_LINE)
                .append(AnsiOutput.toString(SERVER_BANNER_COLOR, ">>|<< Jt808-Server-component-Statistics >>|<<"))
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
        if (isDeprecatedClass(userClass)) {
            return DEPRECATED_COMPONENT_COLOR;
        }
        if (isBuiltinComponent(userClass)) {
            return BUILTIN_COMPONENT_COLOR;
        }

        return CUSTOM_COMPONENT_COLOR;
    }

    private String formatClassName(Object instance) {
        return formatClassName(instance, true, false);
    }

    private String formatClassName(Object instance, boolean shortenClassName, boolean simpleName) {
        if (instance == null) {
            return AnsiOutput.toString(UNKNOWN_COMPONENT_TYPE_COLOR, "(U) NULL");
        }
        Class<?> userClass = ClassUtils.getUserClass(instance);
        AnsiColor color = detectColor(userClass);
        return AnsiOutput.toString(
                color,
                simpleName
                        ? componentPrefix(color) + userClass.getSimpleName()
                        : (
                        shortenClassName
                                ? componentPrefix(color) + shortClassName(userClass)
                                : componentPrefix(color) + userClass.getName()
                )
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

    private Map<MsgType, List<MsgConverterAndHandlerMappingInfo>> initMappingInfo() {
        return msgConverterMapping.getMsgConverterMappings().entrySet().stream()
                .flatMap(entry -> {
                    final Integer msgId = entry.getKey();
                    final MsgType msgType = msgTypeParser.parseMsgType(msgId)
                            .orElseThrow(() -> new JtIllegalArgumentException("Can not parse msgType with msgId : " + msgId));

                    final Map<Jt808ProtocolVersion, RequestMsgBodyConverter<? extends RequestMsgBody>> entryValue = entry.getValue();
                    return entryValue.entrySet().stream().map(e -> {
                        final RequestMsgBodyConverter<? extends RequestMsgBody> converter = e.getValue();
                        final Jt808ProtocolVersion version = e.getKey();
                        final MsgConverterAndHandlerMappingInfo info = new MsgConverterAndHandlerMappingInfo();
                        info.setType(msgType);
                        final Class<?> entityClass = detectRequestMsgEntityClass(converter, msgType, version);
                        info.setConverter(converter);
                        info.setEntityClass(entityClass);
                        info.setVersion(version);
                        msgHandlerMapping.getHandler(msgType, version).ifPresent(info::setHandler);
                        return info;
                    });
                }).collect(groupingBy(MsgConverterAndHandlerMappingInfo::getType));
    }

    private Class<?> detectRequestMsgEntityClass(RequestMsgBodyConverter<?> converter, MsgType msgType, Jt808ProtocolVersion version) {
        if (converter instanceof CustomReflectionBasedRequestMsgBodyConverter) {
            return ((CustomReflectionBasedRequestMsgBodyConverter) converter).getConverter(msgType.getMsgId(), version).orElse(null);
        }

        final Class<?> cls = converter.getClass();

        Type[] genericInterfaces = cls.getGenericInterfaces();
        if (genericInterfaces != null && genericInterfaces.length > 0) {
            Type type = genericInterfaces[0];
            if (type instanceof ParameterizedType) {

                Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                return tryGetGenericType(arguments);
            }
        }

        Type superclass = cls.getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) superclass).getActualTypeArguments();
            return tryGetGenericType(arguments);
        }

        return null;
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


    @Data
    @Accessors(chain = true)
    private static class MsgConverterAndHandlerMappingInfo {
        private MsgType type;
        private RequestMsgBodyConverter<?> converter;
        private MsgHandler<?> handler;
        private Class<?> entityClass;
        private Jt808ProtocolVersion version;
    }

}
