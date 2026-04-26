package com.eventmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Request body for creating or updating a ticket tier")
public class CreateTierRequest {

    @Schema(description = "Tier name", example = "VIP", maxLength = 100)
    @NotBlank(message = "Tier name is required")
    @Size(max = 100, message = "Tier name cannot exceed 100 characters")
    private String name;

    @Schema(description = "Optional tier description", example = "Front row access with meet & greet")
    private String description;

    @Schema(description = "Ticket price in INR (0 for free events)", example = "1500.00")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private BigDecimal price;

    @Schema(description = "Total number of tickets in this tier", example = "200")
    @NotNull(message = "Total quantity is required")
    @Min(value = 1, message = "Total quantity must be at least 1")
    private Integer totalQty;

    @Schema(description = "Maximum tickets a single buyer can purchase (default 10)", example = "4")
    @Min(value = 1, message = "Max per order must be at least 1")
    private Integer maxPerOrder;

    @Schema(description = "When ticket sales open (ISO-8601). Null means immediately.", example = "2025-11-01T10:00:00")
    private LocalDateTime saleStartsAt;

    @Schema(description = "When ticket sales close (ISO-8601). Null means until sold out.", example = "2025-12-30T23:59:59")
    private LocalDateTime saleEndsAt;

    public CreateTierRequest() {
    }

    public CreateTierRequest(String name, String description, BigDecimal price, Integer totalQty,
                             Integer maxPerOrder, LocalDateTime saleStartsAt, LocalDateTime saleEndsAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.totalQty = totalQty;
        this.maxPerOrder = maxPerOrder;
        this.saleStartsAt = saleStartsAt;
        this.saleEndsAt = saleEndsAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public Integer getMaxPerOrder() {
        return maxPerOrder;
    }

    public void setMaxPerOrder(Integer maxPerOrder) {
        this.maxPerOrder = maxPerOrder;
    }

    public LocalDateTime getSaleStartsAt() {
        return saleStartsAt;
    }

    public void setSaleStartsAt(LocalDateTime saleStartsAt) {
        this.saleStartsAt = saleStartsAt;
    }

    public LocalDateTime getSaleEndsAt() {
        return saleEndsAt;
    }

    public void setSaleEndsAt(LocalDateTime saleEndsAt) {
        this.saleEndsAt = saleEndsAt;
    }
}
