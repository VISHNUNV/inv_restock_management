package com.inv_restock_management.constants;

public enum Priority {
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    final int order;

    Priority(int i) {
        this.order = i;
    }

    public int getOrder() {
        return order;
    }
}
