package com.cbr.course_bundle_system.ooo;

import lombok.Getter;

/**
 * Represents a quota for a provider offering based on matched topics.
 */
public class Quota {
    @Getter
    private final ProviderOffering providerOffering;
    @Getter
    private final Topics matchedTopics;
    private final QuotaCalculationStrategy strategy;

    public Quota(ProviderOffering providerOffering, Topics matchedTopics) {
        this.providerOffering = providerOffering;
        this.matchedTopics = matchedTopics;
        this.strategy = QuotaCalculationStrategyFactory.getStrategy(this);
    }

    public double calculate() {
        return strategy.calculate(this);
    }
}
