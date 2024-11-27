package com.cbr.course_bundle_system.ooo;

/**
 * Strategy for calculating quota when there is a single matched topic.
 */
public class SingleTopicStrategy implements QuotaCalculationStrategy {
    private final double multiplier;

    public SingleTopicStrategy(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public double calculate(Quota quota) {
        return multiplier * quota.getMatchedTopics().sumValues();
    }
}
