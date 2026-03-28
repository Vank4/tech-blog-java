package com.techblog.domain.interaction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompareProductSpecResponse {

    private Long id;
    private String specKey;
    private String specValue;
    private String unit;
    private int displayOrder;
}