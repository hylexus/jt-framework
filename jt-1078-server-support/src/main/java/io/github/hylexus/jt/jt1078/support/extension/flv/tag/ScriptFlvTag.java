package io.github.hylexus.jt.jt1078.support.extension.flv.tag;

import java.util.List;

public interface ScriptFlvTag {

    List<AmfMetadata> metadata();

    interface AmfMetadata {

        byte AMF_DATA_TYPE_NUMBER = 0x00;

        String name();

        byte dataType();

        Object value();


        abstract class AbstractAmfMetadata<T> implements AmfMetadata {
            private final T value;
            private final String name;
            private final byte dataType;

            protected AbstractAmfMetadata(String name, byte dataType, T value) {
                this.name = name;
                this.value = value;
                this.dataType = dataType;
            }

            @Override
            public String name() {
                return this.name;
            }

            @Override
            public byte dataType() {
                return dataType;
            }

            @Override
            public Object value() {
                return value;
            }
        }

        class FrameRate extends AbstractAmfMetadata<Double> {
            protected FrameRate(Double value) {
                super("framerate", AMF_DATA_TYPE_NUMBER, value);
            }
        }

        class Duration extends AbstractAmfMetadata<Double> {
            protected Duration(Double value) {
                super("duration", AMF_DATA_TYPE_NUMBER, value);
            }
        }

        class Width extends AbstractAmfMetadata<Double> {
            protected Width(Double value) {
                super("width", AMF_DATA_TYPE_NUMBER, value);
            }
        }

        class Height extends AbstractAmfMetadata<Double> {
            protected Height(Double value) {
                super("height", AMF_DATA_TYPE_NUMBER, value);
            }
        }
    }
}
