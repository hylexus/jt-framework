package io.github.hylexus.jt808.samples.customized.msg.req.location;

import lombok.Data;

/**
 * @author hylexus
 * Created At 2019-10-03 8:51 下午
 */
@Data
public class LocationUploadWarning {

    private Boolean emergencyAlarm;
    private Boolean speedAlarm;
}
