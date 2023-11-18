package io.github.hylexus.jt.jt1078.support.extension.audio.impl;

import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormat;
import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverterRegistry;
import io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.AdpcmImaToPcmJt1078AudioFormatConverter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hylexus
 */
@Slf4j
public class DefaultJt1078AudioFormatConverterRegistry implements Jt1078AudioFormatConverterRegistry {

    private final Map<ConvertibleMetadata, Jt1078AudioFormatConverter> converterMap = new ConcurrentHashMap<>();

    public DefaultJt1078AudioFormatConverterRegistry() {
        registerDefaults(this);
    }

    static void registerDefaults(Jt1078AudioFormatConverterRegistry registry) {
        registry.register(DefaultJt1078AudioFormat.ADPCM_IMA, DefaultJt1078AudioFormat.PCM, new AdpcmImaToPcmJt1078AudioFormatConverter());
    }

    @Override
    public Jt1078AudioFormatConverterRegistry register(Jt1078AudioFormat source, Jt1078AudioFormat target, Jt1078AudioFormatConverter converter) {
        final ConvertibleMetadata key = ConvertibleMetadata.of(source, target);
        final Jt1078AudioFormatConverter old = converterMap.get(key);
        if (old != null && old.shouldBeReplacedBy(converter)) {
            log.info("Jt1078AudioFormatConverter [{}] has been replaced by [{}]", old.getClass().getName(), converter.getClass().getName());
        }
        this.converterMap.put(key, converter);
        return this;
    }

    @Override
    public Optional<Jt1078AudioFormatConverter> getConverter(Jt1078AudioFormat source, Jt1078AudioFormat target) {
        return Optional.ofNullable(this.converterMap.get(ConvertibleMetadata.of(source, target)));
    }


    @ToString
    @EqualsAndHashCode(of = {"source", "target"})
    private static class ConvertibleMetadata {

        private final Jt1078AudioFormat source;
        private final Jt1078AudioFormat target;

        private ConvertibleMetadata(Jt1078AudioFormat source, Jt1078AudioFormat target) {
            this.source = source;
            this.target = target;
        }

        static ConvertibleMetadata of(Jt1078AudioFormat source, Jt1078AudioFormat target) {
            return new ConvertibleMetadata(source, target);
        }
    }
}
