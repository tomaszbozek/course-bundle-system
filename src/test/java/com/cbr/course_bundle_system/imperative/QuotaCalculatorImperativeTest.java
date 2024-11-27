package com.cbr.course_bundle_system.imperative;

import com.cbr.course_bundle_system.QuotaCalculator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class QuotaCalculatorImperativeTest {

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void testCalculate(
            Map<String, String> providerOfferings,
            Map<String, Integer> topics,
            Map<String, Double> expectedQuotas,
            boolean expectException
    ) {
        if (expectException) {
            assertThrows(IllegalArgumentException.class, () -> {
                QuotaCalculator calculator = new QuotaCalculatorImperative(providerOfferings);
                calculator.calculate(topics);
            });
        } else {
            QuotaCalculator calculator = new QuotaCalculatorImperative(providerOfferings);
            Map<String, Double> actualQuotas = calculator.calculate(topics);
            assertEquals(expectedQuotas, actualQuotas);
        }
    }

    private static Stream<Object[]> provideTestData() {
        return Stream.of(
                // Test case 1: Multiple providers
                new Object[]{
                        Map.of(
                                "ProviderA", "Topic1+Topic2",
                                "ProviderB", "Topic2+Topic3",
                                "ProviderC", "Topic3",
                                "ProviderD", "Topic4+Topic5"
                        ),
                        Map.of(
                                "Topic1", 10,
                                "Topic2", 8,
                                "Topic3", 6,
                                "Topic4", 4,
                                "Topic5", 2
                        ),
                        Map.of(
                                "ProviderA", 0.1 * (10 + 8), // 1.8
                                "ProviderB", 0.1 * (8 + 6),  // 1.4
                                "ProviderC", 0.3 * 6         // 1.8
                        ),
                        false
                },
                // Test case 2: Single topic provider
                new Object[]{
                        Map.of(
                                "ProviderX", "Topic1",
                                "ProviderY", "Topic2",
                                "ProviderZ", "Topic5"
                        ),
                        Map.of(
                                "Topic1", 15,
                                "Topic2", 10,
                                "Topic3", 5,
                                "Topic4", 3,
                                "Topic5", 1
                        ),
                        Map.of(
                                "ProviderX", 0.2 * 15, // 3.0
                                "ProviderY", 0.25 * 10 // 2.5
                        ),
                        false
                },
                // Test case 3: No matching providers
                new Object[]{
                        Map.of(
                                "ProviderA", "Topic4+Topic5",
                                "ProviderB", "Topic6"
                        ),
                        Map.of(
                                "Topic1", 7,
                                "Topic2", 5,
                                "Topic3", 3
                        ),
                        Map.of(),
                        false
                },
                // Test case 4: Equal topic values
                new Object[]{
                        Map.of(
                                "ProviderA", "Topic1+Topic2",
                                "ProviderB", "Topic2+Topic3"
                        ),
                        Map.of(
                                "Topic1", 5,
                                "Topic2", 5,
                                "Topic3", 5,
                                "Topic4", 5,
                                "Topic5", 5
                        ),
                        Map.of(
                                "ProviderA", 0.1 * (5 + 5), // 1.0
                                "ProviderB", 0.1 * (5 + 5)  // 1.0
                        ),
                        false
                },
                // Test case 5: Empty inputs (Expect exception)
                new Object[]{
                        Map.of(),
                        Map.of(),
                        Map.of(),
                        true // Changed from false to true
                },
                // Test case 6: Null inputs (Expect exception)
                new Object[]{
                        null,
                        null,
                        null,
                        true
                }
        );
    }
}
