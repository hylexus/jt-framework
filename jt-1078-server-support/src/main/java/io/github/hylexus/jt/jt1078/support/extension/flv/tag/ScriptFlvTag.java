package io.github.hylexus.jt.jt1078.support.extension.flv.tag;

import io.netty.buffer.ByteBuf;

import java.util.List;

import static io.github.hylexus.jt.jt1078.support.extension.flv.tag.Amf.ofTerminator;

public interface ScriptFlvTag {

    List<Amf> metadata();

    static ScriptFlvTag of(List<Amf> metadataList) {
        return () -> metadataList;
    }

    default int writeTo(ByteBuf buffer) {
        final int writerIndex = buffer.writerIndex();
        for (Amf metadata : this.metadata()) {
            metadata.writeTo(buffer);
        }
        ofTerminator().writeTo(buffer);
        return buffer.writerIndex() - writerIndex;
    }
}
