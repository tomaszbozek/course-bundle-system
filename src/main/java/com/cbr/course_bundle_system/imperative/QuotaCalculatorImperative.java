package com.cbr.course_bundle_system.imperative;

import com.cbr.course_bundle_system.QuotaCalculator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements the quota calculation using an imperative and object-oriented approach.
 * Calculates quotas for providers based on the top three topics.
 */
public class QuotaCalculatorImperative implements QuotaCalculator {

    // Coefficients used in quota calculations
    private static final double TWO_TOPICS_COEFFICIENT = 0.1;
    private static final double FIRST_TOPIC_COEFFICIENT = 0.2;
    private static final double SECOND_TOPIC_COEFFICIENT = 0.25;
    private static final double THIRD_TOPIC_COEFFICIENT = 0.3;

    // Map of providers and their offerings (topics)
    private final Map<String, String> providerOfferings;

    /**
     * Constructs a QuotaCalculatorImperative with the specified provider offerings.
     *
     * @param providerOfferings a map where the key is the provider name and the value is a string of topics
     * @throws IllegalArgumentException if providerOfferings is null
     */
    public QuotaCalculatorImperative(Map<String, String> providerOfferings) {
        if (providerOfferings == null) {
            throw new IllegalArgumentException("Provider offerings map cannot be null.");
        }
        this.providerOfferings = new HashMap<>(providerOfferings);
    }

    /**
     * Calculates the quotas for each provider based on the input topics.
     *
     * @param topics a map where the key is the topic and the value is its associated value
     * @return an unmodifiable map of provider names to their calculated quotas
     * @throws IllegalArgumentException if topics is null or empty
     */
    @Override
    public Map<String, Double> calculate(Map<String, Integer> topics) {
        if (topics == null || topics.isEmpty()) {
            throw new IllegalArgumentException("Topics map cannot be null or empty.");
        }

        Map<String, Integer> topTopics = extractTopThreeTopicsByValue(topics);
        return calculateQuotas(topTopics);
    }

    /**
     * Extracts the top three topics based on their values in descending order.
     *
     * @param topics a map of topics and their values
     * @return a LinkedHashMap containing the top three topics
     */
    private Map<String, Integer> extractTopThreeTopicsByValue(Map<String, Integer> topics) {
        return topics.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry::getKey))
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existingValue, newValue) -> existingValue,
                        LinkedHashMap::new
                ));
    }

    /**
     * Calculates the quotas for providers based on the top topics.
     *
     * @param topTopics a map of the top three topics and their values
     * @return a map of provider names to their calculated quotas
     */
    private Map<String, Double> calculateQuotas(Map<String, Integer> topTopics) {
        Map<String, Double> quotas = new LinkedHashMap<>();

        Map<String, Integer> topicRanks = assignTopicRanks(topTopics);

        for (Map.Entry<String, String> providerEntry : providerOfferings.entrySet()) {
            String providerName = providerEntry.getKey();
            Set<String> providerTopics = parseProviderTopics(providerEntry.getValue());

            Set<String> matchedTopics = getMatchedTopics(providerTopics, topTopics.keySet());

            int matchedTopicCount = matchedTopics.size();
            if (matchedTopicCount == 2) {
                quotas.put(providerName, calculateTwoTopicsQuota(matchedTopics, topTopics));
            } else if (matchedTopicCount == 1) {
                String matchedTopic = matchedTopics.iterator().next();
                quotas.put(providerName, calculateSingleTopicQuota(matchedTopic, topTopics, topicRanks));
            }
            // Providers matching zero or all three topics are not assigned a quota
        }

        return Collections.unmodifiableMap(quotas);
    }

    /**
     * Assigns ranks to topics based on their order in the top topics.
     *
     * @param topTopics a map of the top topics
     * @return a map of topics to their assigned ranks
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
     *
     * @param offerings a string of topics separated by '+'
     * @return a set of topics offered by the provider
     */
    private Set<String> parseProviderTopics(String offerings) {
        if (offerings == null || offerings.isEmpty()) {
            return Collections.emptySet();
        }
        return Arrays.stream(offerings.split("\\+"))
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves the set of topics that match between the provider and top topics.
     *
     * @param providerTopics a set of topics offered by the provider
     * @param topTopics      a set of the top topics
     * @return a set of matched topics
     */
    private Set<String> getMatchedTopics(Set<String> providerTopics, Set<String> topTopics) {
        return providerTopics.stream()
                .filter(topTopics::contains)
                .collect(Collectors.toSet());
    }

    /**
     * Calculates the quota for providers matching two topics.
     *
     * @param matchedTopics a set of matched topics
     * @param topTopics     a map of the top topics and their values
     * @return the calculated quota
     */
    private double calculateTwoTopicsQuota(Set<String> matchedTopics, Map<String, Integer> topTopics) {
        int totalValue = matchedTopics.stream()
                .mapToInt(topTopics::get)
                .sum();
        return TWO_TOPICS_COEFFICIENT * totalValue;
    }

    /**
     * Calculates the quota for providers matching a single topic.
     *
     * @param topic       the matched topic
     * @param topTopics   a map of the top topics and their values
     * @param topicRanks  a map of topics to their assigned ranks
     * @return the calculated quota
     */
    private double calculateSingleTopicQuota(String topic, Map<String, Integer> topTopics, Map<String, Integer> topicRanks) {
        int topicValue = topTopics.get(topic);
        int rank = topicRanks.getOrDefault(topic, -1);

        double coefficient = switch (rank) {
            case 0 -> FIRST_TOPIC_COEFFICIENT;
            case 1 -> SECOND_TOPIC_COEFFICIENT;
            case 2 -> THIRD_TOPIC_COEFFICIENT;
            default -> 0.0;
        };

        return coefficient * topicValue;
    }
}
