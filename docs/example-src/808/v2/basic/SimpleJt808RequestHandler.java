public interface SimpleJt808RequestHandler<T> extends MultipleVersionSupport {

    /**
     * @return 该处理器可以处理什么类型的消息
     */
    Set<MsgType> getSupportedMsgTypes();

    /**
     * @return 该处理器可以处理的协议类型(默认为ALL)
     */
    @Override
    default Set<Jt808ProtocolVersion> getSupportedVersions() {
        return MultipleVersionSupport.super.getSupportedVersions();
    }

    /**
     * 处理消息
     *
     * @return {@link Jt808Response} 或 可以转换为 {@link Jt808Response} 的类型
     */
    T handleMsg(Jt808ServerExchange exchange);

}