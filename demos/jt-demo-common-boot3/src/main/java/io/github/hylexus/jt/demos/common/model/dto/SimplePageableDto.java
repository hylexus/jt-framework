package io.github.hylexus.jt.demos.common.model.dto;

import lombok.Data;

@Data
public class SimplePageableDto {
    private int page = 1;
    private int pageSize = 10;
}
