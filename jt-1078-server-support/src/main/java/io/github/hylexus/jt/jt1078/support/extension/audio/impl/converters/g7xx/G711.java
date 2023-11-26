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
 * g711.c
 *
 * u-law, A-law and linear PCM conversions.
 */
package io.github.hylexus.jt.jt1078.support.extension.audio.impl.converters.g7xx;

/**
 * 这个包中的代码是从以下几个代码库/资料库中复制、修改的:
 * <ul>
 *     <li><a href="https://gitee.com/matrixy/jtt1078-video-server">https://gitee.com/matrixy/jtt1078-video-server</a></li>
 *     <li><a href="https://github.com/fredrikhederstierna/g726">https://github.com/fredrikhederstierna/g726</a></li>
 * </ul>
 *
 * @see <a href="https://github.com/fredrikhederstierna/g726/blob/41d813c33e2001515e7992bc39423250996e258b/src/g711.c#L56">https://github.com/fredrikhederstierna/g726/blob/41d813c33e2001515e7992bc39423250996e258b/src/g711.c#L56</a>
 * @see <a href="https://gitee.com/matrixy/jtt1078-video-server/blob/flv/src/main/java/cn/org/hentai/jtt1078/codec/g726/G726.java#L17">https://gitee.com/matrixy/jtt1078-video-server/blob/flv/src/main/java/cn/org/hentai/jtt1078/codec/g726/G726.java#L17</a>
 */
public class G711 {
    // #define	SIGN_BIT	(0x80)		/* Sign bit for a A-law byte. */
    public static final int SIGN_BIT = 0x80;
    // #define	QUANT_MASK	(0xf)		/* Quantization field mask. */
    public static final int QUANT_MASK = 0xf;
    // #define	NSEGS		(8)		/* Number of A-law segments. */
    // #define	SEG_SHIFT	(4)		/* Left shift for segment number. */
    public static final int SEG_SHIFT = 4;
    // #define	SEG_MASK	(0x70)		/* Segment field mask. */
    public static final int SEG_MASK = 0x70;

    // #define	BIAS		(0x84)		/* Bias for linear code. */
    public static final int BIAS = 0x84;
    // #define CLIP            8159
    public static final int CLIP = 8159;
    static int[] seg_aend = {0x1F, 0x3F, 0x7F, 0xFF, 0x1FF, 0x3FF, 0x7FF, 0xFFF};
    static int[] seg_uend = {0x3F, 0x7F, 0xFF, 0x1FF, 0x3FF, 0x7FF, 0xFFF, 0x1FFF};

    /*
     * linear2ulaw() - Convert a linear PCM value to u-law
     *
     * In order to simplify the encoding process, the original linear magnitude
     * is biased by adding 33 which shifts the encoding range from (0 - 8158) to
     * (33 - 8191). The result can be seen in the following encoding table:
     *
     *	Biased Linear Input Code	Compressed Code
     *	------------------------	---------------
     *	00000001wxyza			000wxyz
     *	0000001wxyzab			001wxyz
     *	000001wxyzabc			010wxyz
     *	00001wxyzabcd			011wxyz
     *	0001wxyzabcde			100wxyz
     *	001wxyzabcdef			101wxyz
     *	01wxyzabcdefg			110wxyz
     *	1wxyzabcdefgh			111wxyz
     *
     * Each biased linear code has a leading 1 which identifies the segment
     * number. The value of the segment number is equal to 7 minus the number
     * of leading 0's. The quantization interval is directly available as the
     * four bits wxyz.  * The trailing bits (a - h) are ignored.
     *
     * Ordinarily the complement of the resulting code word is used for
     * transmission, and so the code word is complemented before it is returned.
     *
     * For further information see John C. Bellamy's Digital Telephony, 1982,
     * John Wiley & Sons, pps 98-111 and 472-476.
     */
    public static int linear2ulaw(int pcm_val)    /* 2's complement (16-bit range) */ {
        int mask;
        int seg;
        int uval;

        /* Get the sign and the magnitude of the value. */
        pcm_val = pcm_val >> 2;
        if (pcm_val < 0) {
            pcm_val = -pcm_val;
            mask = 0x7F;
        } else {
            mask = 0xFF;
        }
        if (pcm_val > CLIP) pcm_val = CLIP;        /* clip the magnitude */
        pcm_val += (BIAS >> 2);

        /* Convert the scaled magnitude to segment number. */
        seg = search(pcm_val, seg_uend, 8);

        /*
         * Combine the sign, segment, quantization bits;
         * and complement the code word.
         */
        if (seg >= 8)        /* out of range, return maximum value. */
            return (0x7F ^ mask);
        else {
            uval = (seg << 4) | ((pcm_val >> (seg + 1)) & 0xF);
            return (uval ^ mask);
        }

    }

    /*
     * ulaw2linear() - Convert a u-law value to 16-bit linear PCM
     *
     * First, a biased linear code is derived from the code word. An unbiased
     * output can then be obtained by subtracting 33 from the biased code.
     *
     * Note that this function expects to be passed the complement of the
     * original code word. This is in keeping with ISDN conventions.
     */
    public static int ulaw2linear(int u_val) {
        int t;

        /* Complement to obtain normal u-law value. */
        u_val = ~u_val;

        /*
         * Extract and bias the quantization bits. Then
         * shift up by the segment number and subtract out the bias.
         */
        t = ((u_val & QUANT_MASK) << 3) + BIAS;
        t <<= (u_val & SEG_MASK) >> SEG_SHIFT;

        return (((u_val & SIGN_BIT) != 0) ? (BIAS - t) : (t - BIAS));
    }

    /*
     * linear2alaw() - Convert a 16-bit linear PCM value to 8-bit A-law
     *
     * linear2alaw() accepts an 16-bit integer and encodes it as A-law data.
     *
     *		Linear Input Code	Compressed Code
     *	------------------------	---------------
     *	0000000wxyza			000wxyz
     *	0000001wxyza			001wxyz
     *	000001wxyzab			010wxyz
     *	00001wxyzabc			011wxyz
     *	0001wxyzabcd			100wxyz
     *	001wxyzabcde			101wxyz
     *	01wxyzabcdef			110wxyz
     *	1wxyzabcdefg			111wxyz
     *
     * For further information see John C. Bellamy's Digital Telephony, 1982,
     * John Wiley & Sons, pps 98-111 and 472-476.
     */
    public static int linear2alaw(int pcm_val)        /* 2's complement (16-bit range) */
    /* changed from "short" *drago* */ {
        int mask;    /* changed from "short" *drago* */
        int seg;    /* changed from "short" *drago* */
        int aval;

        pcm_val = pcm_val >> 3;

        if (pcm_val >= 0) {
            mask = 0xD5;        /* sign (7th) bit = 1 */
        } else {
            mask = 0x55;        /* sign bit = 0 */
            pcm_val = -pcm_val - 1;
        }

        /* Convert the scaled magnitude to segment number. */
        seg = search(pcm_val, seg_aend, 8);

        /* Combine the sign, segment, and quantization bits. */

        if (seg >= 8)        /* out of range, return maximum value. */
            return (0x7F ^ mask);
        else {
            aval = seg << SEG_SHIFT;
            if (seg < 2)
                aval |= (pcm_val >> 1) & QUANT_MASK;
            else
                aval |= (pcm_val >> seg) & QUANT_MASK;
            return (aval ^ mask);
        }
    }

    /*
     * alaw2linear() - Convert an A-law value to 16-bit linear PCM
     *
     */
    public static int alaw2linear(int a_val) {
        int t;      /* changed from "short" *drago* */
        int seg;    /* changed from "short" *drago* */

        a_val ^= 0x55;

        t = (a_val & QUANT_MASK) << 4;
        seg = ((short) a_val & SEG_MASK) >> SEG_SHIFT;

        switch (seg) {
            case 0:
                t += 8;
                break;
            case 1:
                t += 0x108;
                break;
            default:
                t += 0x108;
                t <<= seg - 1;
        }
        return (((a_val & SIGN_BIT) != 0) ? t : -t);
    }

    static int search(
            int val,    /* changed from "short" *drago* */
            int[] table,
            int size)    /* changed from "short" *drago* */ {
        int i;        /* changed from "short" *drago* */

        for (i = 0; i < size; i++) {
            if (val <= table[i])
                return (i);
        }
        return (size);
    }
}
