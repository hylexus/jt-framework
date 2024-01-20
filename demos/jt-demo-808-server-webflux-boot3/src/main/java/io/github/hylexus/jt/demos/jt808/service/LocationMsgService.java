package io.github.hylexus.jt.demos.jt808.service;

import io.github.hylexus.jt.demos.jt808.configuration.pros.Jt808AppProps;
import io.github.hylexus.jt.demos.jt808.msg.req.LocationUploadMsgV2019;
import io.github.hylexus.jt.jt808.spec.Jt808CommandKey;
import io.github.hylexus.jt.jt808.spec.Jt808CommandSender;
import io.github.hylexus.jt.jt808.spec.builtin.msg.extension.BuiltinMsg64;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2013Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.extra.ExtraItemSupport;
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
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@EnableConfigurationProperties({Jt808AppProps.class})
public class LocationMsgService {
    private final Jt808CommandSender commandSender;
    private final Jt808AppProps appProps;
    private final int attachmentServerPortTcp;

    public LocationMsgService(Jt808CommandSender commandSender, Jt808AppProps appProps, @Value("${jt808.attachment-server.port}") int portTcp) {
        this.commandSender = commandSender;
        this.appProps = appProps;
        this.attachmentServerPortTcp = portTcp;
    }

    public void processLocationMsg(Jt808Session session, LocationUploadMsgV2019 body) {
        if (!CollectionUtils.isEmpty(body.getExtraItemList())) {
            final Map<Integer, ExtraItemSupport> groupById = body.getExtraItemList().stream().collect(Collectors.toMap(LocationUploadMsgV2019.ExtraItem::getId, Function.identity()));
            this.processExtraItemList(session, groupById);
        }
    }

    public void processLocationMsg(BuiltinMsg0200V2013Alias body, Jt808Session session) {
        if (!CollectionUtils.isEmpty(body.getExtraItemList())) {
            final Map<Integer, ExtraItemSupport> groupById = body.getExtraItemList().stream().collect(Collectors.toMap(BuiltinMsg0200V2013Alias.ExtraItem::getId, Function.identity()));
            this.processExtraItemList(session, groupById);
        }
    }

    private void processExtraItemList(Jt808Session session, Map<Integer, ExtraItemSupport> groupById) {
        // 苏标: 高级驾驶辅助系统报警信息，定义见表 4-15
        this.process0x64IfNecessary(session, groupById.get(0x64));
    }

    private void process0x64IfNecessary(Jt808Session session, ExtraItemSupport extraItem) {
        if (extraItem == null) {
            return;
        }

        final BuiltinMsg64 msg64 = new BuiltinMsg64(extraItem.getContent());
        final BuiltinMsg9208Alias msg9208 = new BuiltinMsg9208Alias();
        msg9208.setAttachmentServerIp(appProps.getServerIp());
        msg9208.setAttachmentServerIpLength((short) appProps.getServerIp().length());
        msg9208.setAttachmentServerPortTcp(attachmentServerPortTcp);
        msg9208.setAttachmentServerPortUdp(0);
        msg9208.setAlarmIdentifier(msg64.getAlarmIdentifiers());
        msg9208.setAlarmNo(Randoms.randomString(32));
        msg9208.setReservedByte16("");
        final Jt808CommandKey commandKey = Jt808CommandKey.of(session.terminalId(), BuiltinJt808MsgType.SERVER_MSG_9208, session.nextFlowId());
        try {
            final Object resp = this.commandSender.sendCommandAndWaitingForReply(commandKey, msg9208, 20L, TimeUnit.SECONDS);
            log.info("RESP <-- 0x9208: {}", resp);
        } catch (Throwable e) {
            log.error("下发 0x9208 异常", e);
            throw new RuntimeException(e);
        }

    }
}
