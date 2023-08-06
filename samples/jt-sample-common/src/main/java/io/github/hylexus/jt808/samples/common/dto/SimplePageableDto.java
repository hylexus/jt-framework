package io.github.hylexus.jt808.samples.common.dto;

import lombok.Data;

@Data
public class SimplePageableDto {
    private int page = 1;
    private int pageSize = 10;
}
