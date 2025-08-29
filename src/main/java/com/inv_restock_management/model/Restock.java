package com.inv_restock_management.model;

import com.inv_restock_management.constants.Priority;

public record Restock(
                    Long id,
                    String name,
                    Integer restockQuantity,
                    Priority priority) {
}