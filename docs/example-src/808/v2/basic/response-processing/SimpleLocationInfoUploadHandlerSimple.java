@Component
public class SimpleLocationInfoUploadHandlerSimple implements SimpleJt808RequestHandler<Jt808Response> {
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Set.of(BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD);
    }

    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Set.of(Jt808ProtocolVersion.VERSION_2019);
    }

    // 7e02004086010000000001893094655200E4000000000000000101D907F2073D336C000000000000211124114808010400000026030200003001153101002504000000001404000000011504000000FA160400000000170200001803000000EA10FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF02020000EF0400000000F31B017118000000000000000000000000000000000000000000000000567e
    @Override
    public Jt808Response handleMsg(Jt808ServerExchange exchange) {
        final Jt808Request request = exchange.request();

        // 忽略请求读取过程

        // 下面是直接返回Jt808Response类型数据
        return exchange.response()
                .msgType(BuiltinJt808MsgType.SERVER_COMMON_REPLY.getMsgId())
                .flowId(exchange.session().nextFlowId())
                // body
                .writeWord(request.flowId())
                .writeWord(BuiltinJt808MsgType.CLIENT_LOCATION_INFO_UPLOAD.getMsgId())
                .writeByte(0);
    }
}
