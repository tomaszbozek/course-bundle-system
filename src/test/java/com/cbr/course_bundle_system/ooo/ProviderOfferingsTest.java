package com.cbr.course_bundle_system.ooo;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProviderOfferingsTest {

    @Test
    public void testCreateFrom() {
        Map<String, String> providerData = new HashMap<>();
        providerData.put("ProviderA", "Topic1+Topic2");

        Map<String, Integer> topicValues = new HashMap<>();
        topicValues.put("Topic1", 5);
        topicValues.put("Topic2", 3);

        ProviderOfferings providerOfferings = ProviderOfferings.createFrom(providerData, topicValues);

        List<ProviderOffering> offerings = providerOfferings.getOfferings();
        assertThat(offerings.size()).isEqualTo(1);
        ProviderOffering offering = offerings.getFirst();
        assertThat(offering.name()).isEqualTo("ProviderA");
        assertThat(offering.topics().size()).isEqualTo(2);
    }

    @Test
    public void testCreateQuotas() {
        Topics topics = new Topics(Map.of("Topic1", 5, "Topic2", 3));
        ProviderOffering offering = new ProviderOffering("ProviderA", topics);
        ProviderOfferings providerOfferings = new ProviderOfferings(List.of(offering));

        Topics topTopics = new Topics(Map.of("Topic1", 5, "Topic3", 8));
        Quotas quotas = providerOfferings.createQuotas(topTopics);

        assertThat(quotas.getQuotas().size()).isEqualTo(1);
        Quota quota = quotas.getQuotas().getFirst();
        assertThat(quota.getProviderOffering().name()).isEqualTo("ProviderA");
        assertThat(quota.getMatchedTopics().size()).isEqualTo(1);
    }
}
