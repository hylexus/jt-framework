package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.data.resp.BytesBytesValueWrapper;
import io.github.hylexus.jt.data.resp.DwordBytesValueWrapper;
import io.github.hylexus.jt.data.resp.StringBytesValueWrapper;
import io.github.hylexus.jt.data.resp.WordBytesValueWrapper;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.samples.customized.msg.resp.RespTerminalSettings;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hylexus
 * Created At 2019-10-19 7:55 下午
 */
public class EncoderTest {

    private final Encoder encoder = new Encoder(new BytesEncoder.DefaultBytesEncoder());

    @Test
    public void test1() throws IllegalAccessException, InstantiationException {
        RespTerminalSettings param = new RespTerminalSettings();
        ArrayList<RespTerminalSettings.ParamItem> paramList = Lists.newArrayList(
                new RespTerminalSettings.ParamItem(0x0001, DwordBytesValueWrapper.of(789)),
                new RespTerminalSettings.ParamItem(0x0010, StringBytesValueWrapper.of("124")),
                new RespTerminalSettings.ParamItem(0x0081, WordBytesValueWrapper.of(456)),
                new RespTerminalSettings.ParamItem(0x0110, BytesBytesValueWrapper.of("123".getBytes()))
        );
        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());

        byte[] bytes = encoder.encodeCommandBody(param);
        System.out.println(HexStringUtils.bytes2HexString(bytes));
    }

    @Test
    public void test2() throws IllegalAccessException, InstantiationException {
        RespTerminalSettings param = new RespTerminalSettings();
        List<RespTerminalSettings.ParamItem> paramList = Lists.newArrayList(
                new RespTerminalSettings.ParamItem(0x0029, DwordBytesValueWrapper.of(100))
        );

        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());
        byte[] bytes = encoder.encodeCommandBody(param);
        // 7E8103000A013184610090000701000000290400000064827E
        // 01000000290400000064
        Assert.assertEquals("01000000290400000064", HexStringUtils.bytes2HexString(bytes));
        System.out.println(HexStringUtils.bytes2HexString(bytes));
    }


}
