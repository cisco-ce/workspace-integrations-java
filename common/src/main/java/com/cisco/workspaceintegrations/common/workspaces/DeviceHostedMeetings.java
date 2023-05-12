package com.cisco.workspaceintegrations.common.workspaces;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

import static com.cisco.workspaceintegrations.common.Utils.toPiiLengthString;

public class DeviceHostedMeetings extends ValueObject {

    public static final DeviceHostedMeetings DISABLED = new DeviceHostedMeetings(false, null);
    private final boolean enabled;
    private final String siteUrl;

    @JsonCreator
    public DeviceHostedMeetings(@JsonProperty("enabled") boolean enabled,
                                @JsonProperty("siteUrl") String siteUrl) {
        this.enabled = enabled;
        this.siteUrl = siteUrl;
        if (this.enabled) {
            if (this.siteUrl == null) {
                throw new IllegalArgumentException("Invalid siteUrl");
            }
        }
    }

    @Override
    public String toString() {
        return "{enabled=" + enabled + ", siteUrl=" + toPiiLengthString(siteUrl) + "}";
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
