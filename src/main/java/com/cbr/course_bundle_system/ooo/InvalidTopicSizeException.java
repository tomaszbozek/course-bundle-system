package com.cbr.course_bundle_system.ooo;

/**
 * Custom exception thrown when an invalid number of topics is encountered.
 */
public class InvalidTopicSizeException extends RuntimeException {
    public InvalidTopicSizeException(int size) {
        super("Invalid matched topic size: " + size);
    }
}
