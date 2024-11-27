package com.cbr.course_bundle_system.ooo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvalidTopicSizeExceptionTest {

    @Test
    public void testExceptionMessage() {
        Exception exception = assertThrows(InvalidTopicSizeException.class, () -> {
            throw new InvalidTopicSizeException(0);
        });

        String expectedMessage = "Invalid matched topic size: 0";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
