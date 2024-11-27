package com.cbr.course_bundle_system.ooo;

/**
 * Strategy interface for quota calculation.
 */
public interface QuotaCalculationStrategy {
    double calculate(Quota quota);
}
