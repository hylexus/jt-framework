package io.github.hylexus.jt808.msg.req;

import io.github.hylexus.jt.annotation.Jt808Field;
import io.github.hylexus.jt.data.converter.LatLngDataTypeConverter;
import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * @author hylexus
 * Created At 2019-09-18 9:20 下午
 */
@Getter
@Setter
@Accessors(chain = true)
public class LocationUploadRequestMsg extends AbstractRequestMsg {

    private LocationUploadMsgBody body;

    public LocationUploadRequestMsg(AbstractRequestMsg other) {
        super(other);
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @ToString
    public static class LocationUploadMsgBody {

        @Jt808Field(start = 0, dataType = DWORD)
        private int warningFlag;

        @Jt808Field(start = 4, dataType = DWORD)
        private Integer status;

        @Jt808Field(start = 8, dataType = DWORD, customerDataTypeConverterClass = LatLngDataTypeConverter.class)
        private Double lat;

        @Jt808Field(start = 12, dataType = DWORD, customerDataTypeConverterClass = LatLngDataTypeConverter.class)
        private Double lng;

        @Jt808Field(start = 16, dataType = WORD)
        private int height;

        @Jt808Field(start = 18, dataType = WORD)
        private int speed;

        @Jt808Field(start = 20, dataType = WORD)
        private int direction;

        @Jt808Field(start = 22, dataType = BCD, length = 6)
        private String time;
    }
}
