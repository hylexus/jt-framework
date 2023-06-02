package io.github.hylexus.jt.jt1078.samples.webflux.boot3.handler;

import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler;
import io.netty.buffer.ByteBuf;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileOutputStream;
import java.io.IOException;

// @Component
@Slf4j
public class MyJt1078RequestHandler implements Jt1078RequestHandler {

    private final Jt1078SessionManager sessionManager;
    private final FileOutputStream outputStream;

    public MyJt1078RequestHandler(Jt1078SessionManager sessionManager, @Value("${jt1078.dump.path}") String path) {
        this.sessionManager = sessionManager;
        try {
            outputStream = new FileOutputStream(path, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean support(Jt1078Request request) {
        return true;
    }

    @Override
    public void handle(Jt1078Request request) {
        // final Jt1078Session session = this.sessionManager.findBySimOrThrow(request.sim());

        final Jt1078PayloadType payloadType = request.payloadType();
        log.info("sim={}, pt={}, channelNumber={}, dataType={}, subPakId={}", request.sim(), payloadType, request.channelNumber(), request.header().dataType(), request.header().subPackageIdentifier());

        // if (payloadType == DefaultJt1078PayloadType.H264 || payloadType == DefaultJt1078PayloadType.ADPCMA) {
        if (payloadType == DefaultJt1078PayloadType.H264) {
            final ByteBuf body = request.body();
            try {
                body.readBytes(outputStream, body.readableBytes());
                // outputStream.write(new byte[]{0x30, 0x31, 0x63, 0x64});
                // request.rawByteBuf().readBytes(outputStream, request.rawByteBuf().readableBytes());
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            outputStream.close();
            log.info("CLOSE ..........");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
