package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.data.resp.BytesDataType;
import io.github.hylexus.jt.data.resp.DwordDataType;
import io.github.hylexus.jt.data.resp.StringDataType;
import io.github.hylexus.jt.data.resp.WordDataType;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.jt808.server.msg.resp.RespTerminalSettings;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author hylexus
 * Created At 2019-10-19 7:55 下午
 */
public class EncoderTest {

    private Encoder encoder = new Encoder();

    @Test
    public void test1() throws IllegalAccessException, InstantiationException {
        RespTerminalSettings param = new RespTerminalSettings();
        ArrayList<RespTerminalSettings.ParamItem> paramList = Lists.newArrayList(
                new RespTerminalSettings.ParamItem(DwordDataType.of(0x0001), DwordDataType.of(789)),
                new RespTerminalSettings.ParamItem(DwordDataType.of(0x0010), StringDataType.of("124")),
                new RespTerminalSettings.ParamItem(DwordDataType.of(0x0081), WordDataType.of(456)),
                new RespTerminalSettings.ParamItem(DwordDataType.of(0x0110), BytesDataType.of("123".getBytes()))
        );
        param.setParamList(paramList);
        param.setTotalParamCount(paramList.size());

        byte[] bytes = encoder.encodeCommandBody(param);
        System.out.println(HexStringUtils.bytes2HexString(bytes));
    }


}
