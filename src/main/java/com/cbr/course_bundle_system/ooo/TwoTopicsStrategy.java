package com.cbr.course_bundle_system.ooo;

/**
 * Strategy for calculating quota when there are two matched topics.
 */
public class TwoTopicsStrategy implements QuotaCalculationStrategy {
    @Override
    public double calculate(Quota quota) {
        return 0.1 * quota.getMatchedTopics().sumValues();
    }
}
