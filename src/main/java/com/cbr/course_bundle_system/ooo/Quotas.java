package com.cbr.course_bundle_system.ooo;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a collection of quotas.
 */
@Value
@RequiredArgsConstructor
public class Quotas {

    List<Quota> quotas;

    public Map<String, Double> asMap() {
        return this.quotas.stream()
                .map(quota -> Map.entry(quota.getProviderOffering().name(), quota.calculate()))
                .filter(entry -> entry.getValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
