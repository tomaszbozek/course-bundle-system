package com.cbr.course_bundle_system.ooo;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProviderOfferingTest {

    @Test
    public void testMatch() {
        Topics providerTopics = new Topics(Map.of("Topic1", 5, "Topic2", 3));
        ProviderOffering offering = new ProviderOffering("ProviderA", providerTopics);

        Topics topTopics = new Topics(Map.of("Topic1", 5, "Topic3", 8));
        Topics matchedTopics = offering.match(topTopics);

        assertThat(matchedTopics.size()).isEqualTo(1);
        assertThat(matchedTopics.getTopics().get("Topic1")).isEqualTo(5);
    }
}
