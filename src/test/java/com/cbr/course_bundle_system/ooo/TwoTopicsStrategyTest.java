package com.cbr.course_bundle_system.ooo;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TwoTopicsStrategyTest {

    @Test
    public void testCalculate() {
        TwoTopicsStrategy strategy = new TwoTopicsStrategy();
        ProviderOffering offering = new ProviderOffering("ProviderA", new Topics(Map.of("Topic1", 5, "Topic2", 3)));
        Topics matchedTopics = new Topics(Map.of("Topic1", 5, "Topic2", 3));
        Quota quota = new Quota(offering, matchedTopics);

        double result = strategy.calculate(quota);
        assertThat(result).isEqualTo(0.8); // 0.1 * (5 + 3)
    }
}
