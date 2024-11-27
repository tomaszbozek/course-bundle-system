package com.cbr.course_bundle_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class TopicsJsonTests {

    @Autowired
    private JacksonTester<TopicRequest> json;

    @Test
    void givenTopicsInJson_whenDeserializing_shouldReturnTopics() throws IOException {
        String topicsInJson = """
                {
                    "topics": {
                        "reading": 20,
                        "math": 50,
                        "science": 30,
                        "history": 15,
                        "art": 10
                    }
                }
                """;
        TopicRequest object = json.parseObject(topicsInJson);
        assertThat(object).isNotNull();
    }

    private record TopicRequest(Map<String, Integer> topics) {
    }
}
