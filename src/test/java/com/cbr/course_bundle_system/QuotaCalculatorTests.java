package com.cbr.course_bundle_system;

import com.cbr.course_bundle_system.ooo.ProviderOfferings;
import com.cbr.course_bundle_system.ooo.QuotaCalculatorObjectOriented;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class QuotaCalculatorTests {

    @Test
    void givenTwoTopicsMatchWithProviderOfferings_whenCalculateQuota_thenTheQuoteIsTenPercent() {
        Map<String, String> providerOfferingsData = Map.of(
                "provider_a", "math+science",
                "provider_b", "reading+science",
                "provider_c", "history+math"
        );
        Map<String, Integer> topicsData = Map.of(
                "reading", 20,
                "math", 50,
                "science", 30,
                "history", 15,
                "art", 10
        );

        Map<String, Double> quotaMap = null;
//        quotaMap = new QuotaCalculatorImperative(providerOfferings).calculate(topics);
//        assertThat(quotaMap).isNotNull().containsAllEntriesOf(
//                Map.of(
//                        "provider_a", 8.0,
//                        "provider_b", 5.0,
//                        "provider_c", 10.0
//                )
//        );

        ProviderOfferings providerOfferings = ProviderOfferings.createFrom(providerOfferingsData, topicsData);
        quotaMap = new QuotaCalculatorObjectOriented(providerOfferings).calculate(topicsData);
        assertThat(quotaMap).isNotNull().containsAllEntriesOf(
                Map.of(
                        "provider_a", 8.0,
                        "provider_b", 5.0,
                        "provider_c", 10.0
                )
        );
    }
}
