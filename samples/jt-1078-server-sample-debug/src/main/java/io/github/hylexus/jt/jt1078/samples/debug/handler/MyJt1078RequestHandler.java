package io.github.hylexus.jt.jt1078.samples.debug.handler;

import io.github.hylexus.jt.jt1078.spec.Jt1078PayloadType;
import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
import io.github.hylexus.jt.jt1078.spec.impl.request.DefaultJt1078PayloadType;
import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@Slf4j
public class MyJt1078RequestHandler implements Jt1078RequestHandler {

    private final Jt1078SessionManager sessionManager;
    private FileOutputStream outputStream;

    {
        try {
            outputStream = new FileOutputStream("/Users/hylexus/tmp/jtt/tem.dat");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public MyJt1078RequestHandler(Jt1078SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean support(Jt1078Request request) {
        return true;
    }

    @Override
    public void handle(Jt1078Request request) {
        final Jt1078Session session = this.sessionManager.findBySimOrThrow(request.sim());

        final Jt1078PayloadType payloadType = request.payloadType();
        log.info("sim={}, pt={}, channelNumber={}, dataType={}, subPakId={}", request.sim(), payloadType, request.channelNumber(), request.header().dataType(), request.header().subPackageIdentifier());

        if (payloadType == DefaultJt1078PayloadType.H264 || payloadType == DefaultJt1078PayloadType.ADPCMA) {
            final ByteBuf body = request.body();
            try {
                body.readBytes(outputStream, body.readableBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
