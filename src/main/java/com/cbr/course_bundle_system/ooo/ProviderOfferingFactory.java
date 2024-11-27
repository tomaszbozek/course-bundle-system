package com.cbr.course_bundle_system.ooo;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory for creating ProviderOffering instances.
 */
public class ProviderOfferingFactory {
    public static ProviderOffering create(String name, String topicsString, Map<String, Integer> topicValues) {
        Map<String, Integer> topicsMap = Arrays.stream(topicsString.split("\\+"))
            .collect(Collectors.toMap(
                topic -> topic,
                topic -> topicValues.getOrDefault(topic, 0)
            ));
        Topics topics = new Topics(topicsMap);
        return new ProviderOffering(name, topics);
    }
}
