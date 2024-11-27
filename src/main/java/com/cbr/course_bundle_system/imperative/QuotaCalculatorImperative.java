package com.cbr.course_bundle_system.imperative;

import com.cbr.course_bundle_system.QuotaCalculator;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * Imperative approach for quota calculation.
 */
@RequiredArgsConstructor
public class QuotaCalculatorImperative implements QuotaCalculator {
    private final Map<String, String> providerOfferings;

    @Override
    public Map<String, Double> calculate(Map<String, Integer> topics) {
        Map<String, Integer> topTopics = topTopics(topics);
        Map<String, List<Integer>> topicToCountAndImportance = getTopTopicsToCountAndImportance(topTopics);
        return calculateQuotas(topics, topicToCountAndImportance, topTopics);
    }

    public Map<String, Double> calculateQuotas(Map<String, Integer> topics, Map<String, List<Integer>> topicToCountAndImportance, Map<String, Integer> topTopics) {
        Map<String, Double> resultQuotas = new LinkedHashMap<>(3);
        for (Map.Entry<String, List<Integer>> statsEntry : topicToCountAndImportance.entrySet()) {
            List<Integer> statsEntryValue = statsEntry.getValue();
            Integer matchCount = statsEntryValue.get(0);
            if (matchCount < 1 || matchCount > 2) {
                throw new IllegalArgumentException(String.format("Invalid topic match count: %s", matchCount));
            }
            if (matchCount == 2) {
                String[] providerTopics = providerOfferings.get(statsEntry.getKey()).split("\\+");
                double value = 0.1 * (topTopics.get(providerTopics[0]) + topics.get(providerTopics[1]));
                if (value != 0) {
                    resultQuotas.put(statsEntry.getKey(), value);
                }
            } else {
                int importance = statsEntryValue.get(1);
                switch (importance) {
                    case 0 -> {
                        double value = 0.2 * firstValue(topTopics);
                        if (value != 0) {
                            resultQuotas.put(statsEntry.getKey(), value);
                        }
                    }

                    case 1 -> {
                        double value = 0.25 * secondValue(topTopics);
                        if (value != 0) {
                            resultQuotas.put(statsEntry.getKey(), value);
                        }
                    }

                    case 2 -> {
                        double value = 0.3 * thirdValue(topTopics);
                        if (value != 0) {
                            resultQuotas.put(statsEntry.getKey(), value);
                        }
                    }
                }
            }
        }
        return resultQuotas;
    }

    private Map<String, List<Integer>> getTopTopicsToCountAndImportance(Map<String, Integer> topTopics) {
        Map<String, List<Integer>> topicToCountAndImportance = new HashMap<>(topTopics.size());
        for (Map.Entry<String, String> providerEntry : providerOfferings.entrySet()) {
            Set<String> providerTopics = Set.of(providerEntry.getValue().split("\\+"));
            int topicCount = 0;
            int topicImportance = 0;
            for (Map.Entry<String, Integer> topicEntry : topTopics.entrySet()) {
                if (providerTopics.contains(topicEntry.getKey())) {
                    topicCount++;
                    topicImportance++;
                }
            }
            if (!topicToCountAndImportance.containsKey(providerEntry.getKey()) && topicCount > 0) {
                topicToCountAndImportance.put(providerEntry.getKey(), List.of(topicCount, topicImportance - 1));
            }
        }
        return topicToCountAndImportance;
    }

    public double firstValue(Map<String, Integer> topics) {
        return topics.values().stream().findFirst().orElseThrow();
    }

    public double secondValue(Map<String, Integer> topics) {
        return topics.values().stream().skip(1).findFirst().orElseThrow();
    }

    public double thirdValue(Map<String, Integer> topics) {
        return topics.values().stream().skip(2).findFirst().orElseThrow();
    }

    public Map<String, Integer> topTopics(Map<String, Integer> topTopics) {
        List<Map.Entry<String, Integer>> entryList = topTopics.entrySet()
                .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).toList();
        Map<String, Integer> topsResults = new LinkedHashMap<>(3);
        Map.Entry<String, Integer> firstEntry = entryList.get(0);
        topsResults.put(firstEntry.getKey(), firstEntry.getValue());
        Map.Entry<String, Integer> secondEntry = entryList.get(1);
        topsResults.put(secondEntry.getKey(), secondEntry.getValue());
        Map.Entry<String, Integer> thirdEntry = entryList.get(2);
        topsResults.put(thirdEntry.getKey(), thirdEntry.getValue());
        return topsResults;
    }
}
