package io.github.hylexus.jt808.samples.client.debug.client1;

import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.samples.client.debug.utils.TimeUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2013;
import static io.github.hylexus.jt.jt808.Jt808ProtocolVersion.VERSION_2019;
import static io.github.hylexus.jt.jt808.JtProtocolConstant.NETTY_HANDLER_NAME_808_FRAME;
import static io.github.hylexus.jt.jt808.JtProtocolConstant.PACKAGE_DELIMITER;
import static io.github.hylexus.jt808.samples.client.debug.client1.MessageGenerator.formatTerminalId;
import static io.github.hylexus.jt808.samples.client.debug.client1.MessageGenerator.generateTerminalIds;

@Slf4j
public class SimpleClient {
    private final MessageGenerator messageGenerator = new MessageGenerator();

    public static void start(final ClientProps args) {
        final int clientCount = args.getClientCount();
        final String serverIp = args.getServerIp();
        final int serverPort = args.getServerPort();
        final List<String> terminalIds = generateTerminalIds(clientCount);
        for (int i = 0; i < clientCount; i++) {
            final int copy = i;
            new Thread(() -> new SimpleClient().connectServer(terminalIds.get(copy), Jt808FlowIdGenerator.DEFAULT, serverIp, serverPort)).start();
        }
    }

    public void connectServer(String terminalId, Jt808FlowIdGenerator flowIdGenerator, String serverIp, int serverPort) {
        final NioEventLoopGroup group = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(@NonNull SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast(
                                NETTY_HANDLER_NAME_808_FRAME,
                                new DelimiterBasedFrameDecoder(
                                        2048,
                                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER}),
                                        Unpooled.copiedBuffer(new byte[]{PACKAGE_DELIMITER, PACKAGE_DELIMITER})
                                )
                        );
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(@NonNull ChannelHandlerContext ctx) throws Exception {
                                new Thread(() -> {
                                    while (true) {
                                        try {
                                            TimeUtils.sleep(new Random().nextInt(500) + 100, TimeUnit.MILLISECONDS);
                                            final ByteBuf byteBuf = generateMsg(terminalId, flowIdGenerator, ThreadLocalRandom.current().nextInt(100));
                                            //final ByteBuf byteBuf = generateMsg(terminalId, flowIdGenerator, 40);
                                            //final ByteBuf byteBuf = generateMsg(terminalId, flowIdGenerator, 20);
                                            ctx.writeAndFlush(byteBuf);
                                        } catch (Exception e) {
                                            log.error(e.getMessage(), e);
                                        }
                                    }
                                }).start();
                            }

                            @Override
                            public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) throws Exception {
                                try {
                                    if (((ByteBuf) msg).isReadable()) {
                                        log.info("---> {}", "7E" + HexStringUtils.byteBufToString((ByteBuf) msg) + "7E");
                                    }
                                } finally {
                                    ((ByteBuf) msg).release();
                                }
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                log.error(cause.getMessage(), cause);
                                super.exceptionCaught(ctx, cause);
                            }
                        });
                    }
                });

        try {
            log.info("===> Connect to server {}:{}", serverIp, serverIp);
            final ChannelFuture channelFuture = bootstrap.connect(serverIp, serverPort).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            group.shutdownGracefully();
        }
    }

    private ByteBuf generateMsg(String terminalId, Jt808FlowIdGenerator flowIdGenerator, int randomNumber) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        // [0,20)
        if (randomNumber < 20) {
            // normal
            return messageGenerator.randomClientRegisterV2013(flowIdGenerator, formatTerminalId(terminalId, VERSION_2013));
        } else if (randomNumber <= 50) {
            // [20,50)
            // resp-sub-package
            return messageGenerator.randomClientRegisterV2019(flowIdGenerator, formatTerminalId(terminalId, VERSION_2019));
        } else if (randomNumber <= 75) {
            // [50,75)
            // sub-package + req-escape
            int size = random.nextBoolean() ? 33 : 1024;
            return messageGenerator.randomLocationMsgV2013(flowIdGenerator, formatTerminalId(terminalId, VERSION_2013), size);
        } else {
            // [75,100)
            // sub-package + req-escape
            int size = random.nextBoolean() ? 33 : 1024;
            return messageGenerator.randomLocationMsgV2019(flowIdGenerator, formatTerminalId(terminalId, VERSION_2019), size);
        }
    }
}