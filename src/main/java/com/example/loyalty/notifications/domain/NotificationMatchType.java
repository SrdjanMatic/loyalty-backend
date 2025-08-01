package com.example.loyalty.notifications.domain;

public enum NotificationMatchType {
    PERFECT_MATCH("perfect_match"),
    FOOD_MATCH("food_match"),
    NO_MATCH("no_match"),
    UNKNOWN("unknown");

    private final String value;

    NotificationMatchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static NotificationMatchType fromValue(String value) {
        for (NotificationMatchType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return value;
    }
}
