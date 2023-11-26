package io.github.hylexus.jt.jt1078.support.extension.audio;

import io.github.hylexus.jt.core.ReplaceableComponent;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;

/**
 * @author hylexus
 * @see <a href="https://github.com/pdeljanov/Symphonia.git">https://github.com/pdeljanov/Symphonia.git(Rust 版本的音频库)</a>
 * @see <a href="https://wiki.multimedia.cx/index.php/IMA_ADPCM">https://wiki.multimedia.cx/index.php/IMA_ADPCM</a>
 * @see <a href="https://www.cs.columbia.edu/~hgs/audio/dvi/IMA_ADPCM.pdf">https://www.cs.columbia.edu/~hgs/audio/dvi/IMA_ADPCM.pdf</a>
 * @see <a href="https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf">https://ww1.microchip.com/downloads/en/AppNotes/00643b.pdf</a>
 * @see <a href="https://www.hentai.org.cn/article?id=8">https://www.hentai.org.cn/article?id=8</a>
 * @since 2.2.0
 */
public interface Jt1078AudioFormatConverter extends ReplaceableComponent {

    @Nonnull
    Jt1078AudioData convert(ByteBuf stream);

    default void close() {
    }

    default boolean isEmptyStream(ByteBuf stream) {
        return stream.readableBytes() <= 0;
    }

    interface Jt1078AudioData extends AutoCloseable {
        void close();

        ByteBuf payload();

        default boolean isEmpty() {
            return this.payload() == null || this.payload().readableBytes() <= 0;
        }

        static Jt1078AudioData empty() {
            return Empty.INSTANCE;
        }

        enum Empty implements Jt1078AudioData {
            INSTANCE;

            @Override
            public void close() {
                // do nothing
            }

            @Override
            public ByteBuf payload() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }
        }
    }

}
