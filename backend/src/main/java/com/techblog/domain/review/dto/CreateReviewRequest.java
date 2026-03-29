package com.techblog.domain.review.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    private Long productId;

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    private String summary;

    @NotBlank(message = "Nội dung review không được để trống")
    private String content;

    private String pros;

    private String cons;

    @NotNull(message = "Điểm tổng kết không được để trống")
    @DecimalMin(value = "0.0", message = "Điểm tối thiểu là 0.0")
    @DecimalMax(value = "10.0", message = "Điểm tối đa là 10.0")
    private BigDecimal overallScore;

    @Valid
    private List<ReviewScoreRequest> scores;

    private boolean allowComments = true;
}
