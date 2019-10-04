package io.github.hylexus.jt808.server.msg.req.location;

import io.github.hylexus.jt.annotation.msg.SplittableField;
import lombok.Data;

/**
 * @author hylexus
 * Created At 2019-10-04 5:25 下午
 */
@Data
public class LocationUploadStatus {

    @SplittableField.BitAt(0)
    private Boolean acc;

    @SplittableField.BitAt(4)
    private Boolean runningStatus;

    @SplittableField.BitAtRange(startIndex = 8, endIndex = 9)
    @SplittableField(splitPropsIntoField = "bit8To9Details")
    private Integer bit8to9;

    private Bit8To9 bit8To9Details;

    @SplittableField.BitAt(18)
    private Integer bit18;

    @Data
    public static class Bit8To9 {
        @SplittableField.BitAt(0)
        private Integer bit0;
        @SplittableField.BitAt(1)
        private Integer bit1;
    }
}
