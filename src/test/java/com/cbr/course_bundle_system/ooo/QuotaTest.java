package com.cbr.course_bundle_system.ooo;


import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuotaTest {

    @Test
    public void testCalculateTwoTopics() {
        ProviderOffering offering = new ProviderOffering("ProviderA", Topics.from(Map.of("Topic1", 5, "Topic2", 3)));
        Topics matchedTopics = Topics.from(Map.of("Topic1", 5, "Topic2", 3));
        Quota quota = new Quota(offering, matchedTopics);

        double result = quota.calculate();
        assertThat(result).isEqualTo(0.8); // 0.1 * (5 + 3)
    }

    @Test
    public void testCalculateSingleTopicLowImportance() {
        ProviderOffering offering = new ProviderOffering("ProviderB", Topics.from(Map.of("Topic1", 5)));
        Topics matchedTopics = Topics.from(Map.of("Topic1", 5));
        Quota quota = new Quota(offering, matchedTopics);

        double result = quota.calculate();
        assertThat(result).isEqualTo(1.0); // 0.2 * 5
    }

    @Test
    public void testCalculateSingleTopicHighImportance() {
        ProviderOffering offering = new ProviderOffering("ProviderC", Topics.from(Map.of("Topic1", 5, "Topic2", 3, "Topic3", 2)));
        Topics matchedTopics = Topics.from(Map.of("Topic1", 5));
        Quota quota = new Quota(offering, matchedTopics);

        double result = quota.calculate();
        assertThat(result).isEqualTo(1.0); // 0.2 * 5
    }

    @Test
    public void testInvalidTopicSizeException() {
        ProviderOffering offering = new ProviderOffering("ProviderD", Topics.from(Map.of("Topic1", 5)));
        Topics matchedTopics = Topics.from(Map.of()); // Empty topics

        assertThatThrownBy(() -> new Quota(offering, matchedTopics))
                .isInstanceOf(InvalidTopicSizeException.class)
                .hasMessage("Invalid matched topic size: 0");
    }
}
