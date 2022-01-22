package io.github.hylexus.jt808.samples.annotation.handler;

import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0005;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hylexus
 */
@Slf4j
@Component
@Jt808RequestHandler
public class RetransmissionHandler {

    private final Jt808ResponseSubPackageStorage responseSubPackageStorage;
    private final Jt808SessionManager sessionManager;

    public RetransmissionHandler(Jt808ResponseSubPackageStorage responseSubPackageStorage, Jt808SessionManager sessionManager) {
        this.responseSubPackageStorage = responseSubPackageStorage;
        this.sessionManager = sessionManager;
    }

    // 7E00054008010000000001391234432900010000000200010002387E
    // 7E000540080100000000013912344329000100030002000100023B7E
    @Jt808RequestHandlerMapping(msgType = 0x0005, desc = "终端补传分包请求")
    public void processRetransmissionMsg(Jt808RequestEntity<BuiltinMsg0005> request) {
        final Jt808Session session = this.sessionManager.findByTerminalId(request.terminalId()).orElseThrow();

        final BuiltinMsg0005 body = request.body();
        // 分包消息中第一包的流水号
        final int firstSubPackageFlowId = body.getFirstSubPackageFlowId();
        // 需要重传的子包ID
        final List<Integer> packageIdList = body.getPackageIdList().stream().map(BuiltinMsg0005.PackageId::getValue).collect(Collectors.toList());

        // 获取子包
        final Collection<ByteBuf> subPackageMsgList = responseSubPackageStorage.getSubPackageMsg(request.terminalId(), firstSubPackageFlowId, packageIdList);

        subPackageMsgList
                .stream()
                .peek(subPackageMsg -> log.info("re-send msg to client {} : {}", request.terminalId(), HexStringUtils.byteBufToString(subPackageMsg)))
                // 将子包发送给终端
                .forEach(session::sendMsgToClient);
    }

}
