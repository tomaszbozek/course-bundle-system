package com.cbr.course_bundle_system.ooo;

import com.cbr.course_bundle_system.QuotaCalculator;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Object-oriented approach for quota calculation.
 */
@RequiredArgsConstructor
public class QuotaCalculatorObjectOriented implements QuotaCalculator {
    private final ProviderOfferings providerOfferings;

    @Override
    public Map<String, Double> calculate(Map<String, Integer> topicsMap) {
        return providerOfferings.createQuotas(Topics.from(topicsMap).topThree()).asMap();
    }
}
