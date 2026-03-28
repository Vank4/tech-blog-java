package com.techblog.domain.interaction.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompareResponse {

    private Long listId;
    private String name;
    private List<CompareItemResponse> items;
}