package com.cbr.course_bundle_system.ooo;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicsTest {

    @Test
    public void testFrom() {
        Map<String, Integer> topicValues = Map.of("Topic1", 5, "Topic2", 3);
        Topics topics = Topics.from(topicValues);

        assertThat(topics.size()).isEqualTo(2);
        assertThat(topics.getTopics()).containsEntry("Topic1", 5)
                .containsEntry("Topic2", 3);
    }

    @Test
    public void testTopThree() {
        Topics topics = Topics.from(Map.of("Topic1", 5, "Topic2", 3, "Topic3", 8, "Topic4", 2));
        Topics topThree = topics.topThree();

        assertThat(topThree.size()).isEqualTo(3);
        assertThat(topThree.getTopics())
                .containsExactlyInAnyOrderEntriesOf(Map.of("Topic3", 8, "Topic1", 5, "Topic2", 3));
    }

    @Test
    public void testMatch() {
        Topics topics1 = Topics.from(Map.of("Topic1", 5, "Topic2", 3));
        Topics topics2 = Topics.from(Map.of("Topic2", 3, "Topic3", 8));

        Topics matched = topics1.match(topics2);

        assertThat(matched.size()).isEqualTo(1);
        assertThat(matched.getTopics())
                .containsEntry("Topic2", 3)
                .doesNotContainKeys("Topic1", "Topic3");
    }

    @Test
    public void testSumValues() {
        Topics topics = Topics.from(Map.of("Topic1", 5, "Topic2", 3, "Topic3", 8));
        assertThat(topics.sumValues()).isEqualTo(16);
    }

    @Test
    public void testGetImportance() {
        Topics topics = Topics.from(Map.of("Topic1", 5));
        assertThat(topics.getImportance()).containsExactly(ImportanceLevel.HIGH);

        topics = Topics.from(Map.of("Topic1", 5, "Topic2", 3));
        assertThat(topics.getImportance())
                .contains(ImportanceLevel.MEDIUM)
                .hasSize(2);

        topics = Topics.from(Map.of("Topic1", 5, "Topic2", 3, "Topic3", 8));
        assertThat(topics.getImportance())
                .contains(ImportanceLevel.HIGH)
                .hasSize(3);
    }
}
