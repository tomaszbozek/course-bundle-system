package com.cbr.course_bundle_system;

import com.cbr.course_bundle_system.ooo.ProviderOfferings;
import com.cbr.course_bundle_system.ooo.QuotaCalculatorObjectOriented;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/quotas")
class QuotasController {

    @GetMapping
    private Map<String, Double> getQuotas() {
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

        ProviderOfferings providerOfferings = ProviderOfferings.createFrom(providerOfferingsData, topicsData);
        return new QuotaCalculatorObjectOriented(providerOfferings).calculate(topicsData);
    }
}