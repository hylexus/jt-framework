package io.github.hylexus.jt.jt1078.support.extension.h264;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class Sps {
    private byte profileIdc;
    private byte levelIdc;
    private byte profileCompat;
    private int width;
    private int height;
}
