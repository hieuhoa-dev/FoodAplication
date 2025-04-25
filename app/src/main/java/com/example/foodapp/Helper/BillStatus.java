package com.example.foodapp.Helper;

public enum BillStatus {
    WAIT_CONFIRM("Wait Confirm"),
    WAIT_PAYMENT("Wait Payment"),
    WAIT_DELIVERY("Wait Delivery");

    private final String description;

    // Constructor
    BillStatus(String description) {
        this.description = description;
    }

    // Getter để lấy chuỗi mô tả
    public String getDescription() {
        return description;
    }
    public static BillStatus fromDescription(String description) {
        for (BillStatus status : BillStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for description: " + description);
    }
}
