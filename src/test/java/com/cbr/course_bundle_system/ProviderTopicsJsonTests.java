package com.cbr.course_bundle_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ProviderTopicsJsonTests {

    @Autowired
    private JacksonTester<TopicsConfiguration> json;

    @Test
    void givenTopiConfigurationJson_whenParsingObject_thenReturnTopicConfiguration() throws IOException {
        String topicsConfiguration = """
                {
                    "provider_topics": {
                        "provider_a": "math+science",
                        "provider_b": "reading+science",
                        "provider_c": "history+math"
                    }
                }
                """;
        TopicsConfiguration object = json.parseObject(topicsConfiguration);
        assertThat(object).isNotNull();
    }

    private record TopicsConfiguration(Map<String, String> providerTopics) {
    }
}
