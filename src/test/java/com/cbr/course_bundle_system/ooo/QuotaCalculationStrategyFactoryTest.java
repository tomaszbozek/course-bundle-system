package com.cbr.course_bundle_system.ooo;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class QuotaCalculationStrategyFactoryTest {

    @Test
    public void testGetStrategyTwoTopics() {
        ProviderOffering offering = new ProviderOffering("ProviderA", new Topics(Map.of("Topic1", 5, "Topic2", 3)));
        Topics matchedTopics = new Topics(Map.of("Topic1", 5, "Topic2", 3));
        Quota quota = new Quota(offering, matchedTopics);

        QuotaCalculationStrategy strategy = QuotaCalculationStrategyFactory.getStrategy(quota);
        assertThat(strategy instanceof TwoTopicsStrategy).isTrue();
    }

    @Test
    public void testGetStrategySingleTopicLowImportance() {
        ProviderOffering offering = new ProviderOffering("ProviderB", new Topics(Map.of("Topic1", 5)));
        Topics matchedTopics = new Topics(Map.of("Topic1", 5));
        Quota quota = new Quota(offering, matchedTopics);

        QuotaCalculationStrategy strategy = QuotaCalculationStrategyFactory.getStrategy(quota);
        assertThat(strategy instanceof SingleTopicStrategy).isTrue();
    }

    @Test
    public void testGetStrategyInvalidSize() {
        ProviderOffering offering = new ProviderOffering("ProviderC", new Topics(Map.of()));
        Topics matchedTopics = new Topics(Map.of());


        assertThatThrownBy(() -> new Quota(offering, matchedTopics)).isInstanceOf(InvalidTopicSizeException.class);
    }
}
