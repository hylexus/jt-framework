@Slf4j
@Component
public class TerminalRegisterMsgHandlerV2011 implements SimpleJt808RequestHandler<BuiltinMsg8100> {

    /**
     * 处理 [终端注册] 消息
     */
    @Override
    public Set<MsgType> getSupportedMsgTypes() {
        return Set.of(BuiltinJt808MsgType.CLIENT_REGISTER);
    }

    /**
     * 处理 [V2011] 版的消息
     */
    @Override
    public Set<Jt808ProtocolVersion> getSupportedVersions() {
        return Jt808ProtocolVersion.unmodifiableSetVersion2011();
    }

    // 7E01000023013912344321007B000B0002696431323361626364656667684944313233343501B8CA4A2D313233343531317E
    @Override
    public BuiltinMsg8100 handleMsg(Jt808ServerExchange exchange) {

        processRequest(exchange);

        return ...
    }
}
