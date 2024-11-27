package com.cbr.course_bundle_system.ooo;

import java.util.List;

/**
 * Factory for selecting the appropriate quota calculation strategy.
 */
public class QuotaCalculationStrategyFactory {
    public static QuotaCalculationStrategy getStrategy(Quota quota) {
        int size = quota.getMatchedTopics().size();
        List<ImportanceLevel> importanceLevels = quota.getMatchedTopics().getImportance();

        if (size == 2) {
            return new TwoTopicsStrategy();
        } else if (size == 1) {
            return switch (importanceLevels.getFirst()) {
                case HIGH -> new SingleTopicStrategy(0.2);
                case MEDIUM -> new SingleTopicStrategy(0.25);
                case LOW -> new SingleTopicStrategy(0.3);
            };
        } else {
            throw new InvalidTopicSizeException(size);
        }
    }
}
