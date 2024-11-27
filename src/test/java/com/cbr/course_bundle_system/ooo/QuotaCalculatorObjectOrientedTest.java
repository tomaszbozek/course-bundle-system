package com.cbr.course_bundle_system.ooo;

import com.cbr.course_bundle_system.QuotaCalculator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class QuotaCalculatorObjectOrientedTest {

    @Test
    public void testCalculate() {
        // Prepare test data
        Map<String, String> providerData = new HashMap<>();
        providerData.put("ProviderA", "Topic1+Topic2");
        providerData.put("ProviderB", "Topic2+Topic3");
        providerData.put("ProviderC", "Topic3");

        Map<String, Integer> topicValues = new HashMap<>();
        topicValues.put("Topic1", 13);
        topicValues.put("Topic2", 7);
        topicValues.put("Topic3", 8);
        topicValues.put("Topic4", 1);
        topicValues.put("Topic5", 2);

        ProviderOfferings providerOfferings = ProviderOfferings.createFrom(providerData, topicValues);
        QuotaCalculator calculator = new QuotaCalculatorObjectOriented(providerOfferings);

        // Execute the method under test
        Map<String, Double> quotas = calculator.calculate(topicValues);

        // Verify the results
        assertThat(quotas.size()).isEqualTo(3);
        assertThat(quotas.get("ProviderA")).isEqualTo(2);
        assertThat(quotas.get("ProviderB")).isEqualTo(1.5);
        assertThat(quotas.get("ProviderC")).isEqualTo(1.6);
    }
}
