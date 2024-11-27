package com.cbr.course_bundle_system.ooo;

import lombok.Getter;

/**
 * Enum representing the importance level based on topic size.
 */
@Getter
public enum ImportanceLevel {
    LOW(2),
    MEDIUM(1),
    HIGH(0);

    private final int value;

    ImportanceLevel(int value) {
        this.value = value;
    }

    public static ImportanceLevel fromIndex(int index) {
        return switch (index) {
            case 0 -> HIGH;
            case 1 -> MEDIUM;
            case 2 -> LOW;
            default -> throw new IllegalArgumentException(String.format("Invalid index: %s", index));
        };
    }
}
