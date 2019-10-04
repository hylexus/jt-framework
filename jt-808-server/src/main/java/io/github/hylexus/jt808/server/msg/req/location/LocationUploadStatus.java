package io.github.hylexus.jt808.server.msg.req.location;

import io.github.hylexus.jt.annotation.msg.slice.SplittableField;
import lombok.Data;

/**
 * @author hylexus
 * Created At 2019-10-04 5:25 下午
 */
@Data
public class LocationUploadStatus {

    @SplittableField.BitAt(bitIndex = 0)
    private Boolean acc;

    @SplittableField.BitAt(bitIndex = 4)
    private Boolean runningStatus;

    @SplittableField.BitAtRange(startIndex = 8, endIndex = 9)
    @SplittableField(splitPropertyValueIntoNestedBeanField = "bit8To9Details")
    private Integer bit8to9;

    private Bit8To9 bit8To9Details;

    @SplittableField.BitAt(bitIndex = 18)
    private Integer bit18;

    @Data
    public static class Bit8To9 {
        @SplittableField.BitAt(bitIndex = 0)
        private Integer bit0;
        @SplittableField.BitAt(bitIndex = 1)
        private Integer bit1;
    }
}
