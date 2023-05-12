package com.cisco.workspaceintegrations.common.workspaces;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum WorkspaceCategory {
    notSet, focus, huddle, meetingRoom, open, desk, other;

    @JsonCreator
    public static WorkspaceCategory forValue(String value) {
        if (value != null) {
            for (WorkspaceCategory c : values()) {
                if (c.toString().equalsIgnoreCase(value)) {
                    return c;
                }
            }
        }
        return notSet;
    }

    public static String toPrettyString(WorkspaceCategory category) {
        switch (category) {
            case focus:
                return "Focus";
            case huddle:
                return "Huddle";
            case meetingRoom:
                return "Meeting Room";
            case open:
                return "Open";
            case desk:
                return "Desk";
            case other:
                return "Other";
            default:
                return "Not set";

        }
    }
}
