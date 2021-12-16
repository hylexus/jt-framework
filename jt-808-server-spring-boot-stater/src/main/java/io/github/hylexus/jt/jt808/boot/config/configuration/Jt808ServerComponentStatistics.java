package io.github.hylexus.jt.jt808.boot.config.configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.hylexus.jt.jt808.spec.*;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManagerEventListener;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgDecoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808SubPackageStorage;
import io.github.hylexus.jt.jt808.support.data.deserialize.Jt808FieldDeserializerRegistry;
import io.github.hylexus.jt.jt808.support.data.serializer.Jt808FieldSerializerRegistry;
import io.github.hylexus.jt.jt808.support.netty.Jt808ServerNettyConfigure;
import io.github.hylexus.jt.jt808.support.netty.Jt808TerminalHeatBeatHandler;
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

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Set;

import static io.github.hylexus.jt.utils.CommonUtils.*;
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
    private final MsgTypeParser msgTypeParser;
    private final Set<Class<?>> classSet = Sets.newLinkedHashSet(
            Lists.newArrayList(
                    MsgTypeParser.class,
                    Jt808MsgDecoder.class,
                    Jt808MsgEncoder.class,
                    Jt808MsgBytesProcessor.class,
                    Jt808CommandSender.class,
                    Jt808SessionManager.class,
                    Jt808SubPackageStorage.class,
                    Jt808SessionManagerEventListener.class,
                    Jt808FieldDeserializerRegistry.class,
                    Jt808FieldSerializerRegistry.class,
                    Jt808TerminalHeatBeatHandler.class,
                    Jt808ServerNettyConfigure.class,
                    Jt808RequestMsgQueue.class,
                    Jt808RequestMsgQueueListener.class
            )
    );
    @Setter
    private ApplicationContext applicationContext;

    public Jt808ServerComponentStatistics(
            MsgTypeParser msgTypeParser) {
        this.msgTypeParser = msgTypeParser;
    }

    @Override
    public void run(String... args) {

        StringBuilder stringBuilder = new StringBuilder();

        appendBannerPrefix(stringBuilder);

        stringBuilder
                .append(END_OF_LINE).append(line(1, "Configurable Components:")).append(END_OF_LINE)
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

    private boolean containsBean(Object handler) {
        if (handler == null) {
            return false;
        }
        String[] names = applicationContext.getBeanNamesForType(handler.getClass());
        return names != null && names.length != 0;
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
