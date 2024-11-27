package com.cbr.course_bundle_system.ooo;

/**
 * Represents a provider's offering with associated topics.
 */
public record ProviderOffering(String name, Topics topics) {

    public Topics match(Topics other) {
        return topics.match(other);
    }
}
