package com.cbr.course_bundle_system;

import java.util.Map;

public interface QuotaCalculator {
    Map<String, Double> calculate(Map<String, Integer> topicsMap);
}
