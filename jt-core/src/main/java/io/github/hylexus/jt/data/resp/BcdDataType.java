package io.github.hylexus.jt.data.resp;

import io.github.hylexus.oaks.utils.BcdOps;
import lombok.Value;

/**
 * @author hylexus
 * Created At 2019-10-17 10:56 下午
 */
@Value
public class BcdDataType implements DataType<String> {
    private String value;

    private BcdDataType(String value) {
        this.value = value;
    }

    public static BcdDataType of(String value) {
        return new BcdDataType(value);
    }


    @Override
    public byte[] getAsBytes() {
        return BcdOps.bcdString2bytes(value);
    }

}
