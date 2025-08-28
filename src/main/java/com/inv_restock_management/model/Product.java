package com.inv_restock_management.model;

import jakarta.validation.constraints.NotNull;

public record Product(
                    @NotNull(message = "Id Is Mandatory")
                    Long id,
                    @NotNull(message = "Product Name Is Mandatory")
                    String name,
                    @NotNull(message = "Current Stock Is Mandatory")
                    Integer currentStock,
                    @NotNull(message = "Minimum Required Stock Is Mandatory")
                    Integer minRequiredStock,
                    @NotNull(message = "Critical Item Is Mandatory")
                    Boolean criticalItem) {
}