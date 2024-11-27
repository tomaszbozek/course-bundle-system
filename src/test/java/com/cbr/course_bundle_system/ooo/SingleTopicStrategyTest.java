package com.cbr.course_bundle_system.ooo;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SingleTopicStrategyTest {

    @Test
    public void testCalculateLowImportance() {
        SingleTopicStrategy strategy = new SingleTopicStrategy(0.2);
        ProviderOffering offering = new ProviderOffering("ProviderB", new Topics(Map.of("Topic1", 5)));
        Topics matchedTopics = new Topics(Map.of("Topic1", 5));
        Quota quota = new Quota(offering, matchedTopics);

        double result = strategy.calculate(quota);
        assertThat(result).isEqualTo(1.0); // 0.2 * 5
    }

    @Test
    public void testCalculateHighImportance() {
        SingleTopicStrategy strategy = new SingleTopicStrategy(0.3);
        ProviderOffering offering = new ProviderOffering("ProviderC", new Topics(Map.of("Topic1", 5)));
        Topics matchedTopics = new Topics(Map.of("Topic1", 5));
        Quota quota = new Quota(offering, matchedTopics);

        double result = strategy.calculate(quota);
        assertThat(result).isEqualTo(1.5); // 0.3 * 5
    }
}
