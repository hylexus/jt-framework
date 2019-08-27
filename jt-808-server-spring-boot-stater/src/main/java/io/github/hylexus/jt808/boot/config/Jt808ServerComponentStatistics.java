package io.github.hylexus.jt808.boot.config;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.annotation.DebugOnly;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.converter.MsgConverter;
import io.github.hylexus.jt808.converter.MsgTypeParser;
import io.github.hylexus.jt808.handler.MsgHandler;
import io.github.hylexus.jt808.msg.MsgType;
import io.github.hylexus.jt808.support.MsgConverterMapping;
import io.github.hylexus.jt808.support.MsgHandlerMapping;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.function.Function;

import static io.github.hylexus.jt808.boot.config.Jt808ServerAutoConfigure.*;
import static java.util.stream.Collectors.toMap;
import static org.springframework.boot.ansi.AnsiColor.BRIGHT_BLACK;

/**
 * @author hylexus
 * Created At 2019-08-27 9:25 下午
 */
@Slf4j
public class Jt808ServerComponentStatistics implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final String END_OF_LINE = "\n";

    @Autowired
    private MsgConverterMapping msgConverterMapping;

    @Autowired
    private MsgHandlerMapping msgHandlerMapping;

    @Autowired
    private MsgTypeParser msgTypeParser;

    @Override
    public void run(String... args) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();

        appendBannerPrefix(stringBuilder);

        detectConvertAndHandlerMappings(stringBuilder);
        detectMsgTypeParser(stringBuilder);

        appendBannerSuffix(stringBuilder);

        log.info(stringBuilder.toString());
    }

    private void detectConvertAndHandlerMappings(StringBuilder stringBuilder) {
        stringBuilder.append(line(1, "MsgConvert and MsgHandler MappingInfo:")).append(END_OF_LINE);
        stringBuilder.append(String.format("%1$-35s\t|\t%2$-56s|\t%3$-64s\n", "MsgId (MsgDesc)", "MsgConverter", "MsgHandler"));
        stringBuilder.append("----------------------------------------------------------------------");
        stringBuilder.append("----------------------------------------------------------------------");
        stringBuilder.append(END_OF_LINE);

        Map<MsgType, MsgConverterAndHandlerMappingInfo> mappings = initMappingInfo();
        mappings.forEach((msgType, mappingInfo) -> {
            String content = String.format("%1$-30s\t|\t%2$-64s\t|\t%3$-64s\n", formatMsgType(msgType),
                    formatClassName(mappingInfo.getConverter()),
                    formatClassName(mappingInfo.getHandler()));
            stringBuilder.append(content);
        });
    }

    private void detectMsgTypeParser(StringBuilder stringBuilder) {
        stringBuilder.append(END_OF_LINE).append(line(2, "MsgTypeParser -> ")).append(formatClassName(msgTypeParser, false));
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

    private boolean isDeprecatedClass(Class<?> userClass) {
        return userClass.isAnnotationPresent(Deprecated.class)
                || userClass.isAnnotationPresent(DebugOnly.class)
                || io.github.hylexus.jt.common.DebugOnly.class.isAssignableFrom(userClass);
    }

    private boolean isBuiltinComponent(Class<?> userClass) {
        return userClass.isAnnotationPresent(BuiltinComponent.class)
                || io.github.hylexus.jt.common.BuiltinComponent.class.isAssignableFrom(userClass);
    }

    private String formatClassName(Object instance) {
        return formatClassName(instance, true);
    }

    private String formatClassName(Object instance, boolean shortenClassName) {
        if (instance == null) {
            return AnsiOutput.toString(UNKNOWN_COMPONENT_TYPE_COLOR, "(U) NULL");
        }
        Class<?> userClass = ClassUtils.getUserClass(instance);
        AnsiColor color = detectColor(userClass);
        return AnsiOutput.toString(color,
                shortenClassName ? componentPrefix(color) + shortClassName(userClass) : componentPrefix(color) + userClass.getName());
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

    private String shortClassName(Class<?> cls) {
        String[] arr = cls.getName().split("\\.");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            stringBuilder.append(arr[i].charAt(0)).append(".");
        }
        return stringBuilder.append(arr[arr.length - 1]).toString();
    }

    private Map<MsgType, MsgConverterAndHandlerMappingInfo> initMappingInfo() {
        final Map<MsgType, MsgHandler> handlerMappings = msgHandlerMapping.getHandlerMappings();
        final Map<MsgType, MsgConverter> converterMappings = msgConverterMapping.getMsgConverterMappings();

        Map<MsgType, MsgConverterAndHandlerMappingInfo> mappings = converterMappings.entrySet().stream()
                .map(entry -> {
                    MsgConverterAndHandlerMappingInfo info = new MsgConverterAndHandlerMappingInfo();
                    info.setType(entry.getKey());
                    info.setConverter(entry.getValue());
                    MsgHandler msgHandler = handlerMappings.get(entry.getKey());
                    info.setHandler(msgHandler);
                    return info;
                }).collect(toMap(MsgConverterAndHandlerMappingInfo::getType, Function.identity()));

        handlerMappings.forEach((msgType, msgHandler) -> {
            MsgConverterAndHandlerMappingInfo info = mappings.getOrDefault(msgType, new MsgConverterAndHandlerMappingInfo());
            if (info.getHandler() == null) {
                info.setHandler(msgHandler);
                mappings.put(msgType, info);
            }
        });
        return mappings;
    }


    private String formatMsgType(MsgType msgType) {
        return String.format("%s (%s)", HexStringUtils.int2HexString(msgType.getMsgId(), 4, true), msgType.getDesc());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Data
    @Accessors(chain = true)
    private static class MsgConverterAndHandlerMappingInfo {
        private MsgType type;
        private MsgConverter converter;
        private MsgHandler handler;
    }
}
