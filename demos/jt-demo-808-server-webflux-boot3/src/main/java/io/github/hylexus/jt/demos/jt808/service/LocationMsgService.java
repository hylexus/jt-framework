package io.github.hylexus.jt.demos.jt808.service;

import io.github.hylexus.jt.demos.jt808.configuration.pros.Jt808AppProps;
import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location.*;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2013AliasV2;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2019AliasV2;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinMsg9208Alias;
import io.github.hylexus.jt.jt808.spec.impl.BuiltinJt808MsgType;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.utils.Randoms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@EnableConfigurationProperties({Jt808AppProps.class})
public class LocationMsgService {
    private final Jt808CommandSender commandSender;
    private final Jt808AppProps appProps;
    private final int attachmentServerPortTcp;
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4);

    public LocationMsgService(Jt808CommandSender commandSender, Jt808AppProps appProps, @Value("${jt808.attachment-server.port}") int portTcp) {
        this.commandSender = commandSender;
        this.appProps = appProps;
        this.attachmentServerPortTcp = portTcp;
    }

    public void processLocationMsg(Jt808Session session, BuiltinMsg0200V2019AliasV2 body) {
        final Map<Integer, Object> extraItemMap = body.getExtraItemMap();
        // 处理附加项列表
        if (!CollectionUtils.isEmpty(extraItemMap)) {
            this.processExtraItemList(session, extraItemMap);
        }
        // 处理其他消息属性...
    }

    public void processLocationMsg(Jt808Session session, BuiltinMsg0200V2013AliasV2 body) {
        final Map<Integer, Object> extraItemMap = body.getExtraItemMap();
        // 处理附加项列表
        if (!CollectionUtils.isEmpty(extraItemMap)) {
            this.processExtraItemList(session, extraItemMap);
        }
        // 处理其他消息属性...
    }

    private void processExtraItemList(Jt808Session session, Map<Integer, Object> groupById) {
        // 苏标: 高级驾驶辅助系统报警信息，定义见表 4-15
        // ADAS模块视频通道
        this.process0x64IfNecessary(session, (BuiltinMsg64Alias) groupById.get(0x64));

        // 苏标: 驾驶员状态监测系统报警信息，定义见表 4-17
        // DSM模块视频通道
        this.process0x65IfNecessary(session, (BuiltinMsg65Alias) groupById.get(0x65));

        // 苏标: 胎压监测系统报警信息，定义见表 4-18
        this.process0x66IfNecessary(session, (BuiltinMsg66Alias) groupById.get(0x66));

        // 苏标: 盲区监测系统报警信息，定义见表 4-20
        this.process0x67IfNecessary(session, (BuiltinMsg67Alias) groupById.get(0x67));
    }

    private void process0x67IfNecessary(Jt808Session session, BuiltinMsg67Alias msg67) {
        if (msg67 == null) {
            return;
        }

        log.info("ExtraItem-67==>AlarmIdentifier: {}", msg67.getAlarmIdentifier());
        this.doSendMsg9208(session, msg67.getAlarmIdentifier());
    }

    private void process0x66IfNecessary(Jt808Session session, BuiltinMsg66Alias msg66) {
        if (msg66 == null) {
            return;
        }

        log.info("ExtraItem-66==>AlarmIdentifier: {}", msg66.getAlarmIdentifier());
        this.doSendMsg9208(session, msg66.getAlarmIdentifier());
    }

    private void process0x65IfNecessary(Jt808Session session, BuiltinMsg65Alias msg65) {
        if (msg65 == null) {
            return;
        }

        log.info("ExtraItem-65==>AlarmIdentifier: {}", msg65.getAlarmIdentifier());
        this.doSendMsg9208(session, msg65.getAlarmIdentifier());
    }

    private void process0x64IfNecessary(Jt808Session session, BuiltinMsg64Alias msg64) {
        if (msg64 == null) {
            return;
        }

        log.info("ExtraItem-64==>AlarmIdentifier: {}", msg64.getAlarmIdentifier());
        this.doSendMsg9208(session, msg64.getAlarmIdentifier());
    }

    private void doSendMsg9208(Jt808Session session, AlarmIdentifierAlias alarmIdentifier) {
        final BuiltinMsg9208Alias msg9208 = new BuiltinMsg9208Alias();
        msg9208.setAttachmentServerIp(appProps.getServerIp());
        msg9208.setAttachmentServerIpLength((short) appProps.getServerIp().length());
        msg9208.setAttachmentServerPortTcp(attachmentServerPortTcp);
        msg9208.setAttachmentServerPortUdp(0);
        msg9208.setAlarmIdentifier(alarmIdentifier);
        msg9208.setAlarmNo(Randoms.randomString(32));
        msg9208.setReservedByte16("");

        EXECUTOR.submit(() -> {
            final Jt808CommandKey commandKey = Jt808CommandKey.of(session.terminalId(), BuiltinJt808MsgType.CLIENT_COMMON_REPLY, session.nextFlowId());
            try {
                log.info("Waiting for <{}>", commandKey);
                final Object resp = this.commandSender.sendCommandAndWaitingForReply(commandKey, msg9208, 20L, TimeUnit.SECONDS);
                log.info("RESP <-- 0x9208: {}", resp);
            } catch (Throwable e) {
                log.error("下发 0x9208 异常", e);
                throw new RuntimeException(e);
            }
        });
    }
}
