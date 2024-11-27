package com.cbr.course_bundle_system.ooo;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a collection of provider offerings.
 */
@Value
@RequiredArgsConstructor
public class ProviderOfferings {

    List<ProviderOffering> offerings;

    public static ProviderOfferings createFrom(Map<String, String> providerData, Map<String, Integer> topicValues) {
        List<ProviderOffering> offeringsList = providerData.entrySet().stream()
                .map(entry -> ProviderOfferingFactory.create(entry.getKey(), entry.getValue(), topicValues))
                .collect(Collectors.toList());
        return new ProviderOfferings(offeringsList);
    }

    public Quotas createQuotas(Topics topTopics) {
        List<Quota> quotaList = offerings.stream()
                .map(provider -> new Quota(provider, provider.match(topTopics)))
                .collect(Collectors.toList());
        return new Quotas(quotaList);
    }
}
