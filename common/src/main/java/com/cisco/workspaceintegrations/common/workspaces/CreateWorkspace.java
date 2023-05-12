package com.cisco.workspaceintegrations.common.workspaces;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.Utils;
import com.cisco.workspaceintegrations.common.ValueObject;

public class CreateWorkspace extends ValueObject {

    private final String displayName;
    private final String orgId;
    private final String workspaceLocationId;
    private final String floorId;
    private final Integer capacity;
    private final WorkspaceCategory type;
    private final Calling calling;
    private final Calendar calendar;
    private final String sipAddress;
    private final String notes;
    private final HotdeskingStatus hotdeskingStatus;
    private final DeviceHostedMeetings deviceHostedMeetings;

    @JsonCreator
    public CreateWorkspace(@JsonProperty("displayName") String displayName,
                           @JsonProperty("orgId") String orgId,
                           @JsonProperty("workspaceLocationId") String workspaceLocationId,
                           @JsonProperty("floorId") String floorId,
                           @JsonProperty("capacity") Integer capacity,
                           @JsonProperty("type") WorkspaceCategory type,
                           @JsonProperty("calling") Calling calling,
                           @JsonProperty("sipAddress") String sipAddress,
                           @JsonProperty("calendar") Calendar calendar,
                           @JsonProperty("notes") String notes,
                           @JsonProperty("hotdeskingStatus") HotdeskingStatus hotdeskingStatus,
                           @JsonProperty("deviceHostedMeetings") DeviceHostedMeetings deviceHostedMeetings) {
        this.displayName = displayName;
        this.orgId = orgId;
        this.workspaceLocationId = workspaceLocationId;
        this.floorId = floorId;
        this.capacity = capacity;
        this.type = type;
        this.calling = calling;
        this.sipAddress = sipAddress;
        this.calendar = calendar;
        this.notes = notes;
        this.hotdeskingStatus = hotdeskingStatus;
        this.deviceHostedMeetings = deviceHostedMeetings;
    }

    private CreateWorkspace(Builder builder) {
        displayName = builder.displayName;
        orgId = builder.orgId;
        workspaceLocationId = builder.workspaceLocationId;
        floorId = builder.floorId;
        capacity = builder.capacity;
        type = builder.type;
        calling = builder.calling;
        calendar = builder.calendar;
        sipAddress = builder.sipAddress;
        notes = builder.notes;
        hotdeskingStatus = builder.hotdeskingStatus;
        deviceHostedMeetings = builder.deviceHostedMeetings;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getWorkspaceLocationId() {
        return workspaceLocationId;
    }

    public String getFloorId() {
        return floorId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public WorkspaceCategory getType() {
        return type;
    }

    public Calling getCalling() {
        return calling;
    }

    public String getSipAddress() {
        return sipAddress;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getNotes() {
        return notes;
    }

    public HotdeskingStatus getHotdeskingStatus() {
        return hotdeskingStatus;
    }

    public DeviceHostedMeetings getDeviceHostedMeetings() {
        return deviceHostedMeetings;
    }

    @Override
    public String toString() {
        return "{displayName=" + Utils.toPiiLengthString(displayName)
            + ", orgId=" + orgId
            + ", workspaceLocationId=" + workspaceLocationId
            + ", floorId=" + floorId
            + ", capacity=" + capacity
            + ", type=" + type
            + ", calling=" + calling
            + ", sipAddress=" + Utils.toPiiLengthString(sipAddress)
            + ", calendar=" + calendar
            + ", notes='" + Utils.toPiiLengthString(notes)
            + ", hotdeskingStatus='" + hotdeskingStatus
            + ", deviceHostedMeetings='" + deviceHostedMeetings
            + "}";
    }


    public static final class Builder {
        private String displayName;
        private String orgId;
        private String workspaceLocationId;
        private String floorId;
        private Integer capacity;
        private WorkspaceCategory type;
        private Calling calling;
        private Calendar calendar;
        private String sipAddress;
        private String notes;
        private HotdeskingStatus hotdeskingStatus;
        private DeviceHostedMeetings deviceHostedMeetings;

        private Builder() {
        }

        public Builder displayName(String val) {
            displayName = val;
            return this;
        }

        public Builder orgId(String val) {
            orgId = val;
            return this;
        }

        public Builder workspaceLocationId(String val) {
            workspaceLocationId = val;
            return this;
        }

        public Builder floorId(String val) {
            floorId = val;
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

        public Builder calling(Calling val) {
            calling = val;
            return this;
        }

        public Builder calendar(Calendar val) {
            calendar = val;
            return this;
        }

        public Builder sipAddress(String val) {
            sipAddress = val;
            return this;
        }

        public Builder notes(String val) {
            notes = val;
            return this;
        }

        public Builder hotdeskingStatus(HotdeskingStatus val) {
            hotdeskingStatus = val;
            return this;
        }

        public Builder deviceHostedMeetings(DeviceHostedMeetings val) {
            deviceHostedMeetings = val;
            return this;
        }

        public CreateWorkspace build() {
            return new CreateWorkspace(this);
        }
    }
}
