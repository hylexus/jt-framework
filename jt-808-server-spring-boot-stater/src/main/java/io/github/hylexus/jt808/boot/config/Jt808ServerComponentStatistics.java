package io.github.hylexus.jt808.boot.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hylexus.jt.data.msg.MsgType;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.converter.RequestMsgBodyConverter;
import io.github.hylexus.jt808.converter.ResponseMsgBodyConverter;
import io.github.hylexus.jt808.converter.impl.CustomReflectionBasedRequestMsgBodyConverter;
import io.github.hylexus.jt808.dispatcher.RequestMsgDispatcher;
import io.github.hylexus.jt808.ext.AuthCodeValidator;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.CustomReflectionBasedRequestMsgHandler;
import io.github.hylexus.jt808.handler.impl.reflection.HandlerMethod;
import io.github.hylexus.jt808.handler.impl.reflection.argument.resolver.HandlerMethodArgumentResolver;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.queue.RequestMsgQueue;
import io.github.hylexus.jt808.queue.RequestMsgQueueListener;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import io.github.hylexus.jt808.support.RequestMsgBodyConverterMapping;
import io.github.hylexus.jt808.support.netty.Jt808ServerConfigure;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static io.github.hylexus.jt.utils.CommonUtils.*;
import static io.github.hylexus.jt808.boot.config.Jt808ServerAutoConfigure.*;
import static java.util.stream.Collectors.toMap;
import static org.springframework.boot.ansi.AnsiColor.BRIGHT_BLACK;

/**
 * @author hylexus
 * Created At 2019-08-27 9:25 下午
 */
@Slf4j
public class Jt808ServerComponentStatistics implements CommandLineRunner, ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    private static final String END_OF_LINE = "\n";

    private RequestMsgBodyConverterMapping msgConverterMapping;

    private MsgHandlerMapping msgHandlerMapping;

    private MsgTypeParser msgTypeParser;

    public Jt808ServerComponentStatistics(MsgTypeParser msgTypeParser, RequestMsgBodyConverterMapping msgConverterMapping, MsgHandlerMapping msgHandlerMapping) {
        this.msgTypeParser = msgTypeParser;
        this.msgConverterMapping = msgConverterMapping;
        this.msgHandlerMapping = msgHandlerMapping;
    }

    private final Set<Class<?>> classSet = Sets.newLinkedHashSet(
            Lists.newArrayList(
                    BytesEncoder.class,
                    MsgTypeParser.class,
                    AuthCodeValidator.class,
                    RequestMsgDispatcher.class,
                    RequestMsgQueue.class,
                    RequestMsgQueueListener.class,
                    Jt808ServerConfigure.class,
                    ResponseMsgBodyConverter.class,
                    HandlerMethodArgumentResolver.class
            ));

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
                    .append("|\t")
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
        stringBuilder.append(String.format("%1$-35s\t|\t%2$-92s|\t%3$-64s%n", "MsgId (MsgDesc)", "MsgConverter", "MsgHandler"));
        stringBuilder.append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
        ;
        stringBuilder.append(END_OF_LINE);

        final Map<MsgType, MsgConverterAndHandlerMappingInfo> mappings = initMappingInfo();

        mappings.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(MsgType::getMsgId)))
                .forEach(entry -> {
                    MsgType msgType = entry.getKey();
                    MsgConverterAndHandlerMappingInfo mappingInfo = entry.getValue();
                    String content = String.format(
                            "%1$-30s\t|\t%2$-125s\t|\t%3$-300s\n",
                            formatMsgType(msgType),
                            detectMsgConverter(mappingInfo),
                            detectHandlerMethod(mappingInfo.getHandler(), msgType)
                    );
                    stringBuilder.append(content);
                });
        stringBuilder
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------")
                .append("----------------------------------------------------------------------").append(END_OF_LINE);

    }

    private String detectMsgConverter(MsgConverterAndHandlerMappingInfo mappingInfo) {
        return formatClassName(mappingInfo.getConverter(), false, true) + formatEntityClassName(mappingInfo);
    }

    private String detectHandlerMethod(MsgHandler<?> handler, MsgType msgType) {
        if (handler instanceof CustomReflectionBasedRequestMsgHandler) {
            Map<MsgType, HandlerMethod> handlerMethodMapping = ((CustomReflectionBasedRequestMsgHandler) handler).getHandlerMethodMapping();
            HandlerMethod handlerMethod = handlerMethodMapping.get(msgType);
            if (handlerMethod == null) {
                return "";
            }
            AnsiColor color = detectColor(handler.getClass());
            return formatClassName(handlerMethod.getBeanInstance())
                    + AnsiOutput.toString(color, "#" + handlerMethod.getMethod().getName());
        }

        return formatClassName(handler);
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

    private Map<MsgType, MsgConverterAndHandlerMappingInfo> initMappingInfo() {
        final Map<Integer, MsgHandler<? extends RequestMsgBody>> handlerMappings = msgHandlerMapping.getHandlerMappings();

        Map<MsgType, MsgConverterAndHandlerMappingInfo> mappings = msgConverterMapping.getMsgConverterMappings().entrySet().stream()
                .map(entry -> {
                    MsgConverterAndHandlerMappingInfo info = new MsgConverterAndHandlerMappingInfo();
                    MsgType msgType = msgTypeParser.parseMsgType(entry.getKey())
                            .orElseThrow(() -> new JtIllegalArgumentException("Can not parse msgType with msgId : " + entry.getKey()));
                    info.setType(msgType);
                    RequestMsgBodyConverter<?> converter = entry.getValue();
                    Class<?> entityClass = detectRequestMsgEntityClass(converter, msgType);
                    info.setEntityClass(entityClass);
                    info.setConverter(converter);

                    MsgHandler<?> msgHandler = handlerMappings.get(msgType.getMsgId());
                    info.setHandler(msgHandler);
                    return info;
                }).collect(toMap(MsgConverterAndHandlerMappingInfo::getType, Function.identity()));

        handlerMappings.forEach((msgId, msgHandler) -> {
            MsgType msgType = msgTypeParser.parseMsgType(msgId)
                    .orElseThrow(() -> new JtIllegalArgumentException("Can not parse msgType with  msgId : " + msgId));
            MsgConverterAndHandlerMappingInfo info = mappings.getOrDefault(msgType, new MsgConverterAndHandlerMappingInfo());
            if (info.getHandler() == null) {
                info.setHandler(msgHandler);
                mappings.put(msgType, info);
            }
        });
        return mappings;
    }

    private Class<?> detectRequestMsgEntityClass(RequestMsgBodyConverter<?> converter, MsgType msgType) {
        if (converter instanceof CustomReflectionBasedRequestMsgBodyConverter) {
            Map<Integer, Class<? extends RequestMsgBody>> mapping = ((CustomReflectionBasedRequestMsgBodyConverter) converter).getMsgBodyMapping();
            return mapping.get(msgType.getMsgId());
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
        return String.format("%s (%s)", HexStringUtils.int2HexString(msgType.getMsgId(), 4, true), msgType.getDesc());
    }


    @Data
    @Accessors(chain = true)
    private static class MsgConverterAndHandlerMappingInfo {
        private MsgType type;
        private RequestMsgBodyConverter<?> converter;
        private MsgHandler<?> handler;
        private Class<?> entityClass;
    }

    //    public static void main(String[] args) {
    //        String[] headers = new String[]{"MsgId (MsgDesc)", "RequestMsgConverter", "MsgHandler"};
    //        String[][] data = new String[][]{
    //            {"123", "Alfred Alan", "aalan@gmail.comxxxxxxxxxxxxxxxxxxxxxxxxxxxx"},
    //            {"223", "Alison Smart", "asmart@gmail.com"},
    //            {"256", "Ben Bessel", "benb@outlook.com"},
    //            {"374", "John Roberts", "johnrob@company.com"},
    //        };
    //
    //        String str = ASCIITable.fromData(headers, data).toString();
    //        System.out.println(str);
    //    }
}
