package com.techblog.domain.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewScoreRequest {

    @NotBlank(message = "Tiêu chí không được để trống")
    private String criterion;

    @NotNull(message = "Điểm không được để trống")
    @DecimalMin(value = "0.0", message = "Điểm tối thiểu là 0.0")
    @DecimalMax(value = "10.0", message = "Điểm tối đa là 10.0")
    private BigDecimal score;

    @NotNull(message = "Điểm tối đa không được để trống")
    private BigDecimal maxScore = BigDecimal.valueOf(10.0);

    private String note;

    private int displayOrder;
}
