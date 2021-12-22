@Slf4j
@Component
public class TerminalRegisterMsgHandlerV2013 implements SimpleJt808RequestHandler<Jt808Response> {

    /**
     * 处理 [终端注册] 消息
     */
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Set.of(BuiltinJt808MsgType.CLIENT_REGISTER);
    }

    /**
     * 处理 [V2013] 版的消息
     */
    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2013();
    }

    // 7E0100002F013912344323007B000B0002696431323374797065313233343536373838373635343332314944313233343501B8CA4A2D3132333435332D7E
    @Override
    public Jt808Response handleMsg(Jt808ServerExchange exchange) {
        // ...忽略请求读取过程

        // 手动创建一个新的Jt808Response返回
        return Jt808Response.newBuilder()
                .msgId(BuiltinJt808MsgType.CLIENT_REGISTER_REPLY)
                .terminalId(exchange.request().terminalId())
                .flowId(exchange.session().nextFlowId())
                .version(exchange.request().version())
                .body(writer -> writer
                        // 1. byte[0,2) WORD 对应的终端注册消息的流水号
                        .writeWord(exchange.request().flowId())
                        // 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端
                        .writeByte(0)
                        // 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)
                        .writeString("AuthCode-123")
                )
                .build();
    }
}
