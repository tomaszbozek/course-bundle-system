package com.cbr.course_bundle_system.ooo;

import lombok.Value;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a collection of topics with associated values.
 */

@Value
public class Topics {

    Map<String, Integer> topics; // Use LinkedHashMap for maintaining insertion order
    List<String> sortedTopicIndices;

    Topics(Map<String, Integer> topics) {
        this.topics = new LinkedHashMap<>();
        this.topics.putAll(topics); // Ensure LinkedHashMap is maintained
        this.sortedTopicIndices = computeSortedIndices();
    }

    private List<String> computeSortedIndices() {
        return topics.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static Topics from(Map<String, Integer> topics) {
        return new Topics(Map.copyOf(topics));
    }

    public Topics topThree() {
        return new Topics(getTopThreeAsMap(this.topics));
    }

    private Map<String, Integer> getTopThreeAsMap(Map<String, Integer> topics) {
        return topics.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Ensure insertion order is preserved
                ));
    }

    public Topics match(Topics other) {
        Map<String, Integer> matched = topics.entrySet().stream()
                .filter(entry -> other.topics.containsKey(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Maintain order of matched topics
                ));
        return new Topics(matched);
    }

    public int size() {
        return topics.size();
    }

    public double sumValues() {
        return topics.values().stream().mapToInt(Integer::intValue).sum();
    }

    public List<ImportanceLevel> getImportance() {
        return this.sortedTopicIndices.stream()
                .map(topic -> ImportanceLevel.fromIndex(this.sortedTopicIndices.indexOf(topic)))
                .toList();
    }
}
