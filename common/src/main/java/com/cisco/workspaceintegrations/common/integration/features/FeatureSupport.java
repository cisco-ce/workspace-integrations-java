package com.cisco.workspaceintegrations.common.integration.features;

import java.util.Set;

public interface FeatureSupport {

    Set<Feature> getFeatures();

    default boolean supportsFeature(Feature feature) {
        return getFeatures().contains(feature);
    }
}
