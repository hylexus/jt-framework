/*
 * This source code is a product of Sun Microsystems, Inc. and is provided
 * for unrestricted use.  Users may copy or modify this source code without
 * charge.
 *
 * SUN SOURCE CODE IS PROVIDED AS IS WITH NO WARRANTIES OF ANY KIND INCLUDING
 * THE WARRANTIES OF DESIGN, MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE, OR ARISING FROM A COURSE OF DEALING, USAGE OR TRADE PRACTICE.
 *
 * Sun source code is provided with no support and without any obligation on
 * the part of Sun Microsystems, Inc. to assist in its use, correction,
 * modification or enhancement.
 *
 * SUN MICROSYSTEMS, INC. SHALL HAVE NO LIABILITY WITH RESPECT TO THE
 * INFRINGEMENT OF COPYRIGHTS, TRADE SECRETS OR ANY PATENTS BY THIS SOFTWARE
 * OR ANY PART THEREOF.
 *
 * In no event will Sun Microsystems, Inc. be liable for any lost revenue
 * or profits or other special, indirect and consequential damages, even if
 * Sun has been advised of the possibility of such damages.
 *
 * Sun Microsystems, Inc.
 * 2550 Garcia Avenue
 * Mountain View, California  94043
 */

/*
 * g726_24.c
 *
 * Description:
 *
 * g723_24_encoder(), g723_24_decoder()
 *
 * These routines comprise an implementation of the CCITT G.723 24 Kbps
 * ADPCM coding algorithm.  Essentially, this implementation is identical to
 * the bit level description except for a few deviations which take advantage
 * of workstation attributes, such as hardware 2's complement arithmetic.
 *
 * The ITU-T G.726 coder is an adaptive differential pulse code modulation
 * (ADPCM) waveform coding algorithm, suitable for coding of digitized
 * telephone bandwidth (0.3-3.4 kHz) speech or audio signals sampled at 8 kHz.
 * This coder operates on a sample-by-sample basis. Input samples may be
 * represented in linear PCM or companded 8-bit G.711 (m-law/A-law) formats
 * (i.e., 64 kbps). For 32 kbps operation, each sample is converted into a
 * 4-bit quantized difference signal resulting in a compression ratio of
 * 2:1 over the G.711 format. For 24 kbps 40 kbps operation, the quantized
 * difference signal is 3 bits and 5 bits, respectively.
 *
 * $Log: g726_24.c,v $
 * Revision 1.4  2002/11/20 04:29:13  robertj
 * Included optimisations for G.711 and G.726 codecs, thanks Ted Szoczei
 *
 * Revision 1.1  2002/02/11 23:24:23  robertj
 * Updated to openH323 v1.8.0
 *
 * Revision 1.2  2002/02/10 21:14:54  dereks
 * Add cvs log history to head of the file.
 * Ensure file is terminated by a newline.
 *
 *
 *
 */
package io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.g7xx;

import static io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.g7xx.G711.alaw2linear;
import static io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.g7xx.G711.ulaw2linear;
import static io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.g7xx.G726_32.unsignedInt;

/**
 * 这个包中的代码是从以下几个代码库/资料库中复制、修改的:
 * <ul>
 *     <li><a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a></li>
 *     <li><a href="https://github.com/fredrikhederstierna/g726">https://github.com/fredrikhederstierna/g726</a></li>
 * </ul>
 *
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server/blob/flv/src/main/java/cn/org/hentai/jtt1078/codec/g726/G726_24.java#L20">https://gitee.com/matrixy/jtt1078-video-server/blob/flv/src/main/java/cn/org/hentai/jtt1078/codec/g726/G726_24.java#L20</a>
 * @see <a href="https://github.com/fredrikhederstierna/g726/blob/41d813c33e2001515e7992bc39423250996e258b/src/g726_24.c#L150">https://github.com/fredrikhederstierna/g726/blob/41d813c33e2001515e7992bc39423250996e258b/src/g726_24.c#L150</a>
 */
public class G726_24 extends G72X {
    public static final G726_24 instance = new G726_24();

    public static G726_24 getInstance() {
        return instance;
    }

    /*
     * Maps G.723_24 code word to reconstructed scale factor normalized log
     * magnitude values.
     */
    static short[] _dqlntab = {-2048, 135, 273, 373, 373, 273, 135, -2048};

    /* Maps G.723_24 code word to log of scale factor multiplier. */
    static short[] _witab = {-128, 960, 4384, 18624, 18624, 4384, 960, -128};

    /*
     * Maps G.723_24 code words to a set of values whose long and short
     * term averages are computed and then compared to give an indication
     * how stationary (steady state) the signal is.
     */
    static short[] _fitab = {0, 0x200, 0x400, 0xE00, 0xE00, 0x400, 0x200, 0};

    static int[] qtab_723_24 = {8, 218, 331};

    /*
     * g723_24_encoder()
     *
     * Encodes a linear PCM, A-law or u-law input sample and returns its 3-bit code.
     * Returns -1 if invalid input coding value.
     */
    int g726_24_encoder(int sl, int in_coding, G726State state_ptr) {
        int sezi;
        int sei;
        int sez;            /* ACCUM */
        int se;
        int d;                /* SUBTA */
        int y;                /* MIX */
        int i;
        int dq;
        int sr;                /* ADDB */
        int dqsez;            /* ADDC */

        switch (in_coding) {    /* linearize input sample to 14-bit PCM */
            case AUDIO_ENCODING_ALAW:
                sl = alaw2linear(sl) >> 2;
                break;
            case AUDIO_ENCODING_ULAW:
                sl = ulaw2linear(sl) >> 2;
                break;
            case AUDIO_ENCODING_LINEAR:
                sl >>= 2;        /* sl of 14-bit dynamic range */
                break;
            default:
                return (-1);
        }

        sezi = predictor_zero(state_ptr);
        sez = sezi >> 1;
        sei = sezi + predictor_pole(state_ptr);
        se = sei >> 1;            /* se = estimated signal */

        d = sl - se;            /* d = estimation diff. */

        /* quantize prediction difference d */
        y = step_size(state_ptr);    /* quantizer step size */
        i = quantize(d, y, qtab_723_24, 3);    /* i = ADPCM code */
        dq = reconstruct(i & 4, _dqlntab[i], y); /* quantized diff. */

        sr = (dq < 0) ? se - (dq & 0x3FFF) : se + dq; /* reconstructed signal */

        dqsez = sr + sez - se;        /* pole prediction diff. */

        update(3, y, _witab[i], _fitab[i], dq, sr, dqsez, state_ptr);

        return (i);
    }

    @Override
    public int decode(byte[] in_buff, int in_offset, int in_len, int out_coding, byte[] out_buff, int out_offset) {
        return this.decode(in_buff, in_offset, in_len, out_coding, out_buff, out_offset, new G726State());
    }

    /**
     * Decodes the input chunk in_buff of G726_24 encoded data and returns
     * the linear PCM, A-law or u-law chunk into out_buff. <br>
     * It returns the actual size of the output data, or -1 in case of unknown
     * out_coding value.
     */
    public int decode(byte[] in_buff, int in_offset, int in_len, int out_coding, byte[] out_buff, int out_offset, G726State state) {

        if (out_coding == AUDIO_ENCODING_ALAW || out_coding == AUDIO_ENCODING_ULAW) {

            int len_div_3 = in_len / 3;
            for (int i = 0; i < len_div_3; i++) {
                int value8 = 0;
                int in_index = in_offset + i * 3;
                for (int j = 0; j < 3; j++) {
                    value8 += unsignedInt(in_buff[in_index + j]) << (8 * (2 - j));
                }
                int out_index = out_offset + i * 8;
                for (int k = 0; k < 8; k++) {
                    int in_value = (value8 >> (3 * (7 - k))) & 0x7;
                    int out_value = g726_24_decoder(in_value, out_coding, state);
                    out_buff[out_index + k] = (byte) out_value;
                }
            }
            return len_div_3 * 8;
        } else if (out_coding == AUDIO_ENCODING_LINEAR) {

            int len_div_3 = in_len / 3;
            for (int i = 0; i < len_div_3; i++) {
                int value16 = 0;
                int in_index = in_offset + i * 3;
                for (int j = 0; j < 3; j++) {
                    value16 += unsignedInt(in_buff[in_index + j]) << (8 * (2 - j));
                }
                int out_index = out_offset + i * 16;
                for (int k = 0; k < 8; k++) {
                    int k2 = k * 2;
                    int in_value = (value16 >> (3 * (7 - k))) & 0x7;
                    // int out_value=G711.ulaw2linear(decode(in_value,AUDIO_ENCODING_ULAW,state));
                    int out_value = g726_24_decoder(in_value, out_coding, state);
                    out_buff[out_index + k2] = (byte) (out_value & 0xFF);
                    out_buff[out_index + k2 + 1] = (byte) (out_value >> 8);
                }
            }
            return len_div_3 * 16;
        } else return -1;
    }

    /*
     * g723_24_decoder()
     *
     * Decodes a 3-bit CCITT G.723_24 ADPCM code and returns
     * the resulting 16-bit linear PCM, A-law or u-law sample value.
     * -1 is returned if the output coding is unknown.
     */
    int g726_24_decoder(
            int i,
            int out_coding,
            G72X.G726State state_ptr) {
        int sezi;
        int sez;            /* ACCUM */
        int sei;
        int se;
        int y;                /* MIX */
        int dq;
        int sr;                /* ADDB */
        int dqsez;

        i &= 0x07;                /* mask to get proper bits */
        sezi = predictor_zero(state_ptr);
        sez = sezi >> 1;
        sei = sezi + predictor_pole(state_ptr);
        se = sei >> 1;            /* se = estimated signal */

        y = step_size(state_ptr);    /* adaptive quantizer step size */
        dq = reconstruct(i & 0x04, _dqlntab[i], y); /* unquantize pred diff */

        sr = (dq < 0) ? (se - (dq & 0x3FFF)) : (se + dq); /* reconst. signal */

        dqsez = sr - se + sez;            /* pole prediction diff. */

        update(3, y, _witab[i], _fitab[i], dq, sr, dqsez, state_ptr);

        switch (out_coding) {
            case AUDIO_ENCODING_ALAW:
                return (tandem_adjust_alaw(sr, se, y, i, 4, qtab_723_24));
            case AUDIO_ENCODING_ULAW:
                return (tandem_adjust_ulaw(sr, se, y, i, 4, qtab_723_24));
            case AUDIO_ENCODING_LINEAR:
                return (sr << 2);    /* sr was of 14-bit dynamic range */
            default:
                return (-1);
        }
    }

}
