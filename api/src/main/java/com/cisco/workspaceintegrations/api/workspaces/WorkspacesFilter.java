package com.cisco.workspaceintegrations.api.workspaces;

import java.util.Map;

import com.cisco.workspaceintegrations.api.core.Filter;
import com.cisco.workspaceintegrations.common.workspaces.CalendarType;
import com.cisco.workspaceintegrations.common.workspaces.CallingType;
import com.cisco.workspaceintegrations.common.workspaces.WorkspaceCategory;

import static java.lang.String.valueOf;

public final class WorkspacesFilter extends Filter {

    private final String workspaceLocationId;
    private final String floorId;
    private final String displayName;
    private final Integer capacity;
    private final WorkspaceCategory type;
    private final CallingType callingType;
    private final CalendarType calendarType;
    private final Boolean deviceHostedMeetingsEnabled;

    private WorkspacesFilter(String workspaceLocationId,
                             String floorId,
                             String displayName,
                             Integer capacity,
                             WorkspaceCategory type,
                             CallingType callingType,
                             CalendarType calendarType,
                             Boolean deviceHostedMeetingsEnabled) {
        this.workspaceLocationId = workspaceLocationId;
        this.floorId = floorId;
        this.displayName = displayName;
        this.capacity = capacity;
        this.type = type;
        this.callingType = callingType;
        this.calendarType = calendarType;
        this.deviceHostedMeetingsEnabled = deviceHostedMeetingsEnabled;
    }

    private WorkspacesFilter(Builder builder) {
        workspaceLocationId = builder.workspaceLocationId;
        floorId = builder.floorId;
        displayName = builder.displayName;
        capacity = builder.capacity;
        type = builder.type;
        callingType = builder.callingType;
        calendarType = builder.calendarType;
        deviceHostedMeetingsEnabled = builder.deviceHostedMeetingsEnabled;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void addTo(Map<String, Object> params) {
        if (workspaceLocationId != null) {
            params.put("workspaceLocationId", workspaceLocationId);
        }
        if (floorId != null) {
            params.put("floorId", floorId);
        }
        if (displayName != null) {
            params.put("displayName", displayName);
        }
        if (capacity != null) {
            params.put("capacity", capacity);
        }
        if (type != null) {
            params.put("type", type);
        }
        if (callingType != null) {
            params.put("calling", callingType);
        }
        if (calendarType != null) {
            params.put("calendar", calendarType);
        }
        if (deviceHostedMeetingsEnabled != null) {
            params.put("deviceHostedMeetingsEnabled", valueOf(deviceHostedMeetingsEnabled));
        }
    }

    public String getWorkspaceLocationId() {
        return workspaceLocationId;
    }

    public String getFloorId() {
        return floorId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public WorkspaceCategory getType() {
        return type;
    }

    public CallingType getCallingType() {
        return callingType;
    }

    public CalendarType getCalendarType() {
        return calendarType;
    }

    public Boolean getDeviceHostedMeetingsEnabled() {
        return deviceHostedMeetingsEnabled;
    }

    public static final class Builder {
        private String workspaceLocationId;
        private String floorId;
        private String displayName;
        private Integer capacity;
        private WorkspaceCategory type;
        private CallingType callingType;
        private CalendarType calendarType;
        private Boolean deviceHostedMeetingsEnabled;

        private Builder() {
        }

        public Builder workspaceLocationId(String val) {
            workspaceLocationId = val;
            return this;
        }

        public Builder floorId(String val) {
            floorId = val;
            return this;
        }

        public Builder displayName(String val) {
            displayName = val;
            return this;
        }

        public Builder capacity(Integer val) {
            capacity = val;
            return this;
        }

        public Builder type(WorkspaceCategory val) {
            type = val;
            return this;
        }

        public Builder callingType(CallingType val) {
            callingType = val;
            return this;
        }

        public Builder calendarType(CalendarType val) {
            calendarType = val;
            return this;
        }

        public Builder deviceHostedMeetingsEnabled(Boolean val) {
            deviceHostedMeetingsEnabled = val;
            return this;
        }

        public WorkspacesFilter build() {
            return new WorkspacesFilter(this);
        }
    }
}
