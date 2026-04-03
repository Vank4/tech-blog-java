package com.techblog.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Slug is required")
    private String slug;

    @NotBlank(message = "Brand is required")
    private String brand;

    private String model;

    @JsonAlias("shortDesc")
    @NotBlank(message = "Short description is required")
    private String shortDescription;

    @NotBlank(message = "Description is required")
    private String description;

    private Long categoryId;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    @NotBlank(message = "Currency is required")
    private String currency = "VND";

    private boolean allowComments = true;
}