// package io.github.hylexus.jt.jt1078.support.extension.audio.impl;
//
// import io.github.hylexus.jt.jt1078.support.extension.audio.Jt1078AudioFormatConverter;
// import lombok.Getter;
// import lombok.experimental.Accessors;
//
// @Getter
// @Accessors(fluent = true)
// public class DefaultAudioFormatOptions implements Jt1078AudioFormatConverter.AudioFormatOptions {
//     private final int sampleRate;
//     private final int bitDepth;
//     private final int channelCount;
//     private final int bitCount;
//
//     public DefaultAudioFormatOptions(int sampleRate, int bitDepth, int bitCount) {
//         this(sampleRate, bitDepth, 1, bitCount);
//     }
//
//     public DefaultAudioFormatOptions(int sampleRate, int bitDepth, int channelCount, int bitCount) {
//         this.sampleRate = sampleRate;
//         this.bitDepth = bitDepth;
//         this.channelCount = channelCount;
//         this.bitCount = bitCount;
//     }
//
//     @Override
//     public String toString() {
//         return "DefaultAudioFormatOptions{" +
//                 "sampleRate=" + sampleRate +
//                 ", bitCount=" + bitCount +
//                 ", channelCount=" + channelCount +
//                 ", bitRate=" + bitRate() +
//                 '}';
//     }
// }
