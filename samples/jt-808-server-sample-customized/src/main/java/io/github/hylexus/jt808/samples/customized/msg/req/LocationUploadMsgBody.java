package io.github.hylexus.jt808.samples.customized.msg.req;

import io.github.hylexus.jt808.msg.RequestMsgBody;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * Created At 2020-02-02 7:16 下午
 */
@Data
@Accessors(chain = true)
public class LocationUploadMsgBody implements RequestMsgBody {
    private int warningFlag;

    private int status;

    private Double lat;

    private Double lng;

    private short height;

    private short speed;

    private Short direction;

    private String time;
}
