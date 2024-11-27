package com.cbr.course_bundle_system.imperative;

import com.cbr.course_bundle_system.QuotaCalculator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Imperative approach for quota calculation.
 */
public class QuotaCalculatorImperative implements QuotaCalculator {
    private static final double TWO_TOPICS_COEFFICIENT = 0.1;
    private static final double FIRST_TOPIC_COEFFICIENT = 0.2;
    private static final double SECOND_TOPIC_COEFFICIENT = 0.25;
    private static final double THIRD_TOPIC_COEFFICIENT = 0.3;

    private final Map<String, String> providerOfferings;

    public QuotaCalculatorImperative(Map<String, String> providerOfferings) {
        this.providerOfferings = providerOfferings;
    }

    @Override
    public Map<String, Double> calculate(Map<String, Integer> topics) {
        Map<String, Integer> topTopics = getTopThreeTopics(topics);
        return calculateQuotas(topTopics);
    }

    /**
     * Retrieves the top three topics based on their values.
     */
    private Map<String, Integer> getTopThreeTopics(Map<String, Integer> topics) {
        return topics.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * Calculates the quotas for providers based on the top topics.
     */
    private Map<String, Double> calculateQuotas(Map<String, Integer> topTopics) {
        Map<String, Double> quotas = new LinkedHashMap<>();

        Map<String, Integer> topicRanks = assignTopicRanks(topTopics);

        for (Map.Entry<String, String> providerEntry : providerOfferings.entrySet()) {
            String providerName = providerEntry.getKey();
            Set<String> providerTopics = parseProviderTopics(providerEntry.getValue());

            Set<String> matchedTopics = getMatchedTopics(providerTopics, topTopics.keySet());

            switch (matchedTopics.size()) {
                case 2 -> quotas.put(providerName, calculateTwoTopicsQuota(matchedTopics, topTopics));
                case 1 -> {
                    String matchedTopic = matchedTopics.iterator().next();
                    quotas.put(providerName, calculateSingleTopicQuota(matchedTopic, topTopics, topicRanks));
                }
            }
        }

        return quotas;
    }

    /**
     * Assigns ranks to topics based on their order in the top topics.
     */
    private Map<String, Integer> assignTopicRanks(Map<String, Integer> topTopics) {
        Map<String, Integer> topicRanks = new HashMap<>();
        int rank = 0;
        for (String topic : topTopics.keySet()) {
            topicRanks.put(topic, rank++);
        }
        return topicRanks;
    }

    /**
     * Parses the provider's offerings into a set of topics.
     */
    private Set<String> parseProviderTopics(String offerings) {
        return new HashSet<>(Arrays.asList(offerings.split("\\+")));
    }

    /**
     * Retrieves the set of topics that match between the provider and top topics.
     */
    private Set<String> getMatchedTopics(Set<String> providerTopics, Set<String> topTopics) {
        Set<String> matchedTopics = new HashSet<>(providerTopics);
        matchedTopics.retainAll(topTopics);
        return matchedTopics;
    }

    /**
     * Calculates the quota for providers matching two topics.
     */
    private double calculateTwoTopicsQuota(Set<String> matchedTopics, Map<String, Integer> topTopics) {
        int totalValue = matchedTopics.stream()
                .mapToInt(topTopics::get)
                .sum();
        return TWO_TOPICS_COEFFICIENT * totalValue;
    }

    /**
     * Calculates the quota for providers matching a single topic.
     */
    private double calculateSingleTopicQuota(String topic, Map<String, Integer> topTopics, Map<String, Integer> topicRanks) {
        int topicValue = topTopics.get(topic);
        int rank = topicRanks.get(topic);

        double coefficient = switch (rank) {
            case 0 -> FIRST_TOPIC_COEFFICIENT;
            case 1 -> SECOND_TOPIC_COEFFICIENT;
            case 2 -> THIRD_TOPIC_COEFFICIENT;
            default -> 0.0;
        };

        return coefficient * topicValue;
    }
}
