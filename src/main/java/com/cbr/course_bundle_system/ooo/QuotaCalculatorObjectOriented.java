package com.cbr.course_bundle_system.ooo;

import com.cbr.course_bundle_system.QuotaCalculator;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Object oriented approach for quota calculation.
 * Java 21 supports data oriented approach but it out of scope for me and time ;)
 */
@RequiredArgsConstructor
public class QuotaCalculatorObjectOriented implements QuotaCalculator {
    private final ProviderOfferings providerOfferings;

    @Override
    public Map<String, Double> calculate(Map<String, Integer> topicsMap) {
        return calculate(Topics.from(topicsMap));
    }

    private Map<String, Double> calculate(Topics topics) {
        return providerOfferings.createQuota(topics.tops()).asMap();
    }

    @Value
    @Builder
    public static class ProviderOfferings {

        List<ProviderOffering> values;

        @Value
        @Builder
        public static class Quotas {

            @Singular
            List<Quota> values;

            public Map<String, Double> asMap() {
                return values.stream().map(quota -> Map.entry(quota.providerOffering.name, quota.calculate()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }
        }

        @Value
        @Builder
        private static class Quota {
            ProviderOffering providerOffering;
            Topics countMatch;

            public Double calculate() {
                int size = countMatch.size();
                if (size != 1 && size != 2) {
                    throw new IllegalArgumentException(String.format("Invalid topics size: %s", size));
                }
                if (size == 2) {
                    return 0.1 * providerOffering.getTopics().getSumValue();
                } else {
                    int importance = countMatch.getImportance();
                    switch (importance) {
                        case 0 -> {
                            return 0.2 * countMatch.getSumValue();
                        }
                        case 1 -> {
                            return 0.25 * countMatch.getSumValue();
                        }
                        case 2 -> {
                            return 0.3 * countMatch.getSumValue();
                        }
                        default -> {
                            return 0.0;
                        }
                    }
                }
            }
        }

        public static ProviderOfferings from(Map<String, String> providerOfferings, Map<String, Integer> topics) {
            List<ProviderOffering> offeringsList = new ArrayList<>();
            for (Map.Entry<String, String> offeringEntry : providerOfferings.entrySet()) {
                String[] topicsSplit = offeringEntry.getValue().split("\\+");
                List<Map.Entry<Topics.Topic, Integer>> topicValues = Set.of(topicsSplit)
                        .stream().toList()
                        .stream().map(topicName -> Map.entry(new Topics.Topic(topicName), topics.get(topicName)))
                        .toList();
                offeringsList.add(new ProviderOffering(offeringEntry.getKey(), Topics.fromEntries(topicValues)));
            }
            return new ProviderOfferings(offeringsList);
        }

        public Quotas createQuota(Topics topTopics) {
            Quotas.QuotasBuilder builder = Quotas.builder();
            for (ProviderOffering providerOffering : values) {
                Topics countMatch = providerOffering.match(topTopics);
                builder.value(new Quota(providerOffering, countMatch));
            }
            return builder.build();
        }

        @Value
        @Builder
        private static class ProviderOffering {

            String name;
            Topics topics;

            public Topics match(Topics topics) {
                return this.topics.match(topics);
            }
        }
    }

    @Value
    @Builder
    public static class Topics {

        Map<Topic, Integer> values;

        public static Topics from(Map<String, Integer> topics) {
            return new Topics(topics.entrySet().stream().map(entry -> Map.entry(new Topic(entry.getKey()), entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }

        public Topics tops() {
            return Topics.fromEntries(values.entrySet().stream()
                    .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).toList().subList(0, 3));
        }

        public Topics match(Topics topics) {
            List<Map.Entry<Topic, Integer>> result = new ArrayList<>();
            for (Map.Entry<Topic, Integer> entryInternal : values.entrySet()) {
                for (Map.Entry<Topic, Integer> entryExternal : topics.values.entrySet()) {
                    if (entryInternal.getKey().equals(entryExternal.getKey())) {
                        result.add(entryInternal);
                    }
                }
            }
            return Topics.fromEntries(result);
        }

        private static Topics fromEntries(List<Map.Entry<Topic, Integer>> result) {
            return new Topics(result.stream().collect(
                    Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue,
                            () -> new TreeMap<>(Comparator.comparing(o -> o.name))
                    )
            ));
        }

        public double getSumValue() {
            return values.values().stream().mapToInt(Integer::intValue).sum();
        }

        public int size() {
            return values.size();
        }

        public int getImportance() {
            return values.size();
        }

        @Value
        @Builder
        @EqualsAndHashCode
        private static class Topic {
            String name;
        }
    }
}
