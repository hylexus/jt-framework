package io.github.hylexus.jt808.samples.annotation.handler;

import io.github.hylexus.jt.jt808.spec.Jt808RequestEntity;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg0200V2013Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.resp.BuiltinServerCommonReplyMsg;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandler;
import io.github.hylexus.jt.jt808.support.annotation.handler.Jt808RequestHandlerMapping;
import io.github.hylexus.jt808.samples.annotation.msg.req.LocationBatchUploadMsgV2019;
import io.github.hylexus.jt808.samples.annotation.msg.req.LocationUploadMsgV2019;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2013;
import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2019;

/**
 * 附加项列表和苏标附件服务器的示例，参考 demos/jt-demo-808-server-webflux-boot3 的 io.github.hylexus.jt.demos.jt808.handler.LocationMsgHandler
 * <p>
 * 附加项列表和苏标附件服务器的示例，参考 demos/jt-demo-808-server-webflux-boot3 的 io.github.hylexus.jt.demos.jt808.handler.LocationMsgHandler
 * <p>
 * 附加项列表和苏标附件服务器的示例，参考 demos/jt-demo-808-server-webflux-boot3 的 io.github.hylexus.jt.demos.jt808.handler.LocationMsgHandler
 *
 * @author hylexus
 */
@Slf4j
@Component
@Jt808RequestHandler
public class LocationMsgHandler {

    public static final String LATEST_GEO_KEY = "latest_geo_key";

    // 7E070400E401583860765500040003010049000000000004000301D9F190073CA3C1000C00000000211204082941010400D728AD300100310109250400000000140400000004150400000000
    // 1604000000001702000118030000000049000000000004000301D9F190073CA3C1000C00000000211130171352010400D728AD300100310109250400000000140400000004150400000000
    // 1604000000001702000118030000000049000000000004000301D9F190073CA3C1000C00000000211130171357010400D728AD300115310109250400000000140400000004150400000000
    // 160400000000170200011803000000407E
    @Jt808RequestHandlerMapping(msgType = 0x0704)
    public BuiltinServerCommonReplyMsg processLocationBatchUploadMsgV2019(Jt808RequestEntity<LocationBatchUploadMsgV2019> request) {
        log.info("LocationBatchUpload -- V2019 -- {}", request.body());

        return BuiltinServerCommonReplyMsg.success(request.msgId(), request.flowId());
    }

    /**
     * 附加项列表和苏标附件服务器的示例，参考 demos/jt-demo-808-server-webflux-boot3 的 io.github.hylexus.jt.demos.jt808.handler.LocationMsgHandler
     * <p>
     * 附加项列表和苏标附件服务器的示例，参考 demos/jt-demo-808-server-webflux-boot3 的 io.github.hylexus.jt.demos.jt808.handler.LocationMsgHandler
     * <p>
     * 附加项列表和苏标附件服务器的示例，参考 demos/jt-demo-808-server-webflux-boot3 的 io.github.hylexus.jt.demos.jt808.handler.LocationMsgHandler
     */
    @Jt808RequestHandlerMapping(msgType = 0x0200, versions = VERSION_2019)
    public BuiltinServerCommonReplyMsg processLocationUploadMsgV2019(Jt808RequestEntity<LocationUploadMsgV2019> request, Jt808Session session) {
        final LocationUploadMsgV2019 body = request.body();
        log.info("LocationUpload -- V2019 -- {}", body);

        // 这里演示性的记录一下当前会话对应的终端最近一次上报的经纬度
        // 这里仅仅是为方便演示才将最近一次地理位置信息存储到内存中的  实际上你应该使用外部存储介质!!!(否则终端断开连接,session销毁, 数据也就没了)
        // 这里仅仅是为方便演示才将最近一次地理位置信息存储到内存中的  实际上你应该使用外部存储介质!!!(否则终端断开连接,session销毁, 数据也就没了)
        // 这里仅仅是为方便演示才将最近一次地理位置信息存储到内存中的  实际上你应该使用外部存储介质!!!(否则终端断开连接,session销毁, 数据也就没了)
        // @see io.github.hylexus.jt808.samples.annotation.controller.DebugController.terminalList
        // 你可以通过浏览器访问 http://localhost:8808/index.html 来查看效果
        // 你可以通过浏览器访问 http://localhost:8808/index.html 来查看效果
        // 你可以通过浏览器访问 http://localhost:8808/index.html 来查看效果
        session.setAttribute(LATEST_GEO_KEY, body.getLng() * 1.0 / 100_0000 + ", " + body.getLat() * 1.0 / 100_0000);
        return BuiltinServerCommonReplyMsg.success(request.msgId(), request.flowId());
    }

    /**
     * 附加项列表和苏标附件服务器的示例，参考 demos/jt-demo-808-server-webflux-boot3 的 io.github.hylexus.jt.demos.jt808.handler.LocationMsgHandler
     * <p>
     * 附加项列表和苏标附件服务器的示例，参考 demos/jt-demo-808-server-webflux-boot3 的 io.github.hylexus.jt.demos.jt808.handler.LocationMsgHandler
     * <p>
     * 附加项列表和苏标附件服务器的示例，参考 demos/jt-demo-808-server-webflux-boot3 的 io.github.hylexus.jt.demos.jt808.handler.LocationMsgHandler
     */
    @Jt808RequestHandlerMapping(msgType = 0x0200, versions = VERSION_2013)
    public BuiltinServerCommonReplyMsg processLocationUploadMsgV2013(Jt808RequestEntity<BuiltinMsg0200V2013Alias> request, Jt808Session session) {
        final BuiltinMsg0200V2013Alias body = request.body();
        log.info("LocationUpload -- V2019 -- {}", body);

        // 这里演示性的记录一下当前会话对应的终端最近一次上报的经纬度
        // 这里仅仅是为方便演示才将最近一次地理位置信息存储到内存中的  实际上你应该使用外部存储介质!!!(否则终端断开连接,session销毁, 数据也就没了)
        // 这里仅仅是为方便演示才将最近一次地理位置信息存储到内存中的  实际上你应该使用外部存储介质!!!(否则终端断开连接,session销毁, 数据也就没了)
        // 这里仅仅是为方便演示才将最近一次地理位置信息存储到内存中的  实际上你应该使用外部存储介质!!!(否则终端断开连接,session销毁, 数据也就没了)
        // @see io.github.hylexus.jt808.samples.annotation.controller.DebugController.terminalList
        // 你可以通过浏览器访问 http://localhost:8808/index.html 来查看效果
        // 你可以通过浏览器访问 http://localhost:8808/index.html 来查看效果
        // 你可以通过浏览器访问 http://localhost:8808/index.html 来查看效果
        session.setAttribute(LATEST_GEO_KEY, body.getLng() * 1.0 / 100_0000 + ", " + body.getLat() * 1.0 / 100_0000);
        return BuiltinServerCommonReplyMsg.success(request.msgId(), request.flowId());
    }
}
