package com.cisco.workspaceintegrations.common.devices;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DeviceCategory {
    roomdesk,
    phone,
    webexgo,
    accessory,
    unknown;

    @JsonCreator
    public static DeviceCategory fromString(String string) {
        if (string != null) {
            for (DeviceCategory category : values()) {
                if (category.toString().equalsIgnoreCase(string.toLowerCase())) {
                    return category;
                }
            }
        }
        return DeviceCategory.unknown;
    }
}
