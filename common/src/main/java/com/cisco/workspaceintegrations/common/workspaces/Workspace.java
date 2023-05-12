package com.cisco.workspaceintegrations.common.workspaces;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public final class Workspace extends ValueObject {

    public static final Integer CAPACITY_NOT_SET = -1;

    private final String id;
    private final String orgId;
    private final String workspaceLocationId;
    private final String floorId;
    private final String displayName;
    private final int capacity;
    private final WorkspaceCategory type;
    private final String sipAddress;
    private final Instant created;
    private final Calling calling;
    private final Calendar calendar;
    private final String notes;
    private final HotdeskingStatus hotdeskingStatus;
    private final DeviceHostedMeetings deviceHostedMeetings;

    @JsonCreator
    private Workspace(@JsonProperty("id") String id,
                      @JsonProperty("orgId") String orgId,
                      @JsonProperty("workspaceLocationId") String workspaceLocationId,
                      @JsonProperty("floorId") String floorId,
                      @JsonProperty("displayName") String displayName,
                      @JsonProperty("capacity") Integer capacity,
                      @JsonProperty("type") WorkspaceCategory type,
                      @JsonProperty("sipAddress") String sipAddress,
                      @JsonProperty("created") Instant created,
                      @JsonProperty("calling") Calling calling,
                      @JsonProperty("calendar") Calendar calendar,
                      @JsonProperty("notes") String notes,
                      @JsonProperty("hotdeskingStatus") HotdeskingStatus hotdeskingStatus,
                      @JsonProperty("deviceHostedMeetings") DeviceHostedMeetings deviceHostedMeetings) {

        this.id = id;
        this.orgId = orgId;
        this.workspaceLocationId = workspaceLocationId;
        this.floorId = floorId;
        this.displayName = displayName;
        this.capacity = capacity == null ? CAPACITY_NOT_SET : capacity;
        this.type = type;
        this.sipAddress = sipAddress;
        this.created = created;
        this.calling = calling;
        this.calendar = calendar;
        this.notes = notes;
        this.hotdeskingStatus = hotdeskingStatus;
        this.deviceHostedMeetings = deviceHostedMeetings;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("orgId")
    public String getOrgId() {
        return orgId;
    }

    @JsonProperty("workspaceLocationId")
    public Optional<String> getWorkspaceLocationId() {
        return Optional.ofNullable(workspaceLocationId);
    }

    @JsonProperty("floorId")
    public Optional<String> getFloorId() {
        return Optional.ofNullable(floorId);
    }

    @JsonProperty("type")
    public WorkspaceCategory getType() {
        return type == WorkspaceCategory.notSet ? null : type;
    }

    @JsonProperty("capacity")
    public int getCapacity() {
        return capacity;
    }

    public boolean hasCapacity() {
        return capacity != CAPACITY_NOT_SET;
    }

    @JsonProperty("calling")
    public Calling getCalling() {
        return calling;
    }

    @JsonProperty("calendar")
    public Calendar getCalendar() {
        return calendar;
    }

    @JsonProperty("notes")
    public String getNotes() {
        return notes;
    }

    @JsonProperty("hotdeskingStatus")
    public HotdeskingStatus getHotdeskingStatus() {
        return hotdeskingStatus;
    }

    @JsonProperty("deviceHostedMeetings")
    public DeviceHostedMeetings getDeviceHostedMeetings() {
        return deviceHostedMeetings;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("sipAddress")
    public String getSipAddress() {
        return sipAddress;
    }

    @JsonProperty("created")
    public Instant getCreated() {
        return created;
    }
}
