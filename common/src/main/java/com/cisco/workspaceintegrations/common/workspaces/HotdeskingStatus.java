package com.cisco.workspaceintegrations.common.workspaces;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum HotdeskingStatus {
    on, off, unknown;

    @JsonCreator
    public static HotdeskingStatus forValue(String value) {
        if (value != null) {
            for (HotdeskingStatus t : values()) {
                if (t.toString().equalsIgnoreCase(value)) {
                    return t;
                }
            }
        }
        return unknown;
    }

    public static HotdeskingStatus fromBoolean(Boolean hotdeskingEnabled) {
        if (hotdeskingEnabled == null) {
            return off;
        }
        return hotdeskingEnabled ? on : off;
    }
}
