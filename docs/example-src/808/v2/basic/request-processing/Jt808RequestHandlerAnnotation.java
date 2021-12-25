@Slf4j
@Component
// @Jt808RequestHandler 标记该类 表示该类是一个消息处理器累(具体的处理器是该类中的部分方法)
@Jt808RequestHandler
public class SomeRequestHandler {

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2013)
    public TerminalRegisterReplyMsg processTerminalRegisterMsgV2011(Jt808Request request, TerminalRegisterMsgV2011 body) {

        processRequest(request, body);
        return ...;
    }

    @Jt808RequestHandlerMapping(msgType = 0x0100, versions = VERSION_2019)
    public TerminalRegisterReplyMsg processTerminalRegisterMsgV2019(Jt808RequestEntity<TerminalRegisterMsgV2019> request) {

        processRequest(request, body);
        return ...;
    }

}
