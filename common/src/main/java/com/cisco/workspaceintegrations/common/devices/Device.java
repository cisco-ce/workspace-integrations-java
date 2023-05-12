package com.cisco.workspaceintegrations.common.devices;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;
import com.cisco.workspaceintegrations.common.locations.WorkspaceLocation;

import static com.cisco.workspaceintegrations.common.Utils.toUnmodifiableSet;

public final class Device extends ValueObject {
    private final String id;
    private final String displayName;
    private final String workspaceId;
    private final String personId;
    private final String orgId;
    private final Set<String> capabilities;
    private final Set<String> permissions;
    private final String connectionStatus;
    private final Date created;
    private final String product;
    private final Set<String> tags;
    private final String ip;
    private final String mac;
    private final String serial;
    private final String activeInterface;
    private final String software;
    private final String primarySipUrl;
    private final Set<String> sipUrls;
    private final Set<String> errorCodes;
    private final String upgradeChannel;
    private final DeviceCategory type;
    private final Object deviceConfigurations;
    private final WorkspaceLocation location;
    private final Date firstSeen;
    private final Date lastSeen;

    @JsonCreator
    private Device(@JsonProperty("id") String id,
                   @JsonProperty("displayName") String displayName,
                   @JsonProperty("workspaceId") String workspaceId,
                   @JsonProperty("personId") String personId,
                   @JsonProperty("orgId") String orgId,
                   @JsonProperty("capabilities") Set<String> capabilities,
                   @JsonProperty("permissions") Set<String> permissions,
                   @JsonProperty("product") String product,
                   @JsonProperty("type") DeviceCategory type,
                   @JsonProperty("tags") Set<String> tags,
                   @JsonProperty("ip") String ip,
                   @JsonProperty("mac") String mac,
                   @JsonProperty("serial") String serial,
                   @JsonProperty("activeInterface") String activeInterface,
                   @JsonProperty("software") String software,
                   @JsonProperty("upgradeChannel") String upgradeChannel,
                   @JsonProperty("primarySipUrl") String primarySipUrl,
                   @JsonProperty("sipUrls") Set<String> sipUrls,
                   @JsonProperty("errorCodes") Set<String> errorCodes,
                   @JsonProperty("connectionStatus") String connectionStatus,
                   @JsonProperty("created") Date created,
                   @JsonProperty("firstSeen") Date firstSeen,
                   @JsonProperty("lastSeen") Date lastSeen,
                   @JsonProperty("location") WorkspaceLocation location,
                   @JsonProperty("deviceConfigurations") Object deviceConfigurations) {
        this.id = id;
        this.displayName = displayName;
        this.workspaceId = workspaceId;
        this.personId = personId;
        this.orgId = orgId;
        this.capabilities = toUnmodifiableSet(capabilities);
        this.permissions = toUnmodifiableSet(permissions);
        this.product = product;
        this.type = type;
        this.tags = toUnmodifiableSet(tags);
        this.ip = ip;
        this.mac = mac;
        this.serial = serial;
        this.activeInterface = activeInterface;
        this.software = software;
        this.upgradeChannel = upgradeChannel;
        this.primarySipUrl = primarySipUrl;
        this.sipUrls = toUnmodifiableSet(sipUrls);
        this.errorCodes = toUnmodifiableSet(errorCodes);
        this.connectionStatus = connectionStatus;
        this.created = created;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
        this.location = location;
        this.deviceConfigurations = deviceConfigurations;
    }

    public String getId() {
        return id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public String getPersonId() {
        return personId;
    }

    public String getOrgId() {
        return orgId;
    }

    public Date getCreated() {
        return created != null ? new Date(created.getTime()) : null;
    }

    public Date getFirstSeen() {
        return firstSeen != null ? new Date(firstSeen.getTime()) : null;
    }

    public Date getLastSeen() {
        return lastSeen != null ? new Date(lastSeen.getTime()) : null;
    }

    public Object getDeviceConfigurations() {
        return this.deviceConfigurations;
    }

    public WorkspaceLocation getLocation() {
        return this.location;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<String> getCapabilities() {
        return capabilities;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public String getProduct() {
        return product;
    }

    public Set<String> getTags() {
        return tags;
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }

    public String getSerial() {
        return serial;
    }

    public String getActiveInterface() {
        return activeInterface;
    }

    public String getSoftware() {
        return software;
    }

    public String getPrimarySipUrl() {
        return primarySipUrl;
    }

    public Set<String> getSipUrls() {
        return sipUrls;
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    public String getUpgradeChannel() {
        return upgradeChannel;
    }

    public DeviceCategory getType() {
        return type;
    }
}
