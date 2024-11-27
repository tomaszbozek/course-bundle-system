package com.cbr.course_bundle_system.ooo;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;

public class QuotasTest {

    @Test
    public void testAsMap() {
        ProviderOffering offering1 = new ProviderOffering("ProviderA", new Topics(Map.of("Topic1", 5)));
        ProviderOffering offering2 = new ProviderOffering("ProviderB", new Topics(Map.of("Topic2", 3)));

        Quota quota1 = new Quota(offering1, new Topics(Map.of("Topic1", 5)));
        Quota quota2 = new Quota(offering2, new Topics(Map.of("Topic2", 3)));

        Quotas quotas = new Quotas(List.of(quota1, quota2));
        Map<String, Double> quotaMap = quotas.asMap();

        assertThat(quotaMap.size()).isEqualTo(2);
        assertThat(quotaMap.get("ProviderA")).isEqualTo(1.0); // 0.2 * 5
        assertThat(quotaMap.get("ProviderB")).isCloseTo(0.6, within(0.0001));// 0.2 * 3
    }
}
