package com.cisco.workspaceintegrations.api.devices;

import java.util.Map;
import java.util.Set;

import com.cisco.workspaceintegrations.api.core.Filter;
import com.cisco.workspaceintegrations.common.devices.DeviceCategory;
import com.cisco.workspaceintegrations.common.xapi.Key;

public final class DevicesFilter extends Filter {
    private final String displayName;
    private final String workspaceId;
    private final String personId;
    private final String orgId;
    private final Set<String> capabilities;
    private final Set<String> permissions;
    private final String connectionStatus;
    private final String product;
    private final Set<String> tags;
    private final String mac;
    private final String serial;
    private final String software;
    private final Set<String> errorCodes;
    private final String upgradeChannel;
    private final DeviceCategory type;
    private Key key;
    private final boolean includeDeviceConfigurations;
    private final boolean includeLocation;
    private final boolean minimalConfigs;

    private DevicesFilter(String displayName,
                          String workspaceId,
                          String personId,
                          String orgId,
                          Set<String> capabilities,
                          Set<String> permissions,
                          String product,
                          DeviceCategory type,
                          Set<String> tags,
                          String mac,
                          String serial,
                          String software,
                          String upgradeChannel,
                          Set<String> errorCodes,
                          String connectionStatus,
                          boolean minimalConfigs,
                          boolean includeLocation,
                          boolean includeDeviceConfigurations) {
        this.displayName = displayName;
        this.workspaceId = workspaceId;
        this.personId = personId;
        this.orgId = orgId;
        this.capabilities = capabilities;
        this.permissions = permissions;
        this.product = product;
        this.type = type;
        this.tags = tags;
        this.mac = mac;
        this.serial = serial;
        this.software = software;
        this.upgradeChannel = upgradeChannel;
        this.errorCodes = errorCodes;
        this.connectionStatus = connectionStatus;
        this.minimalConfigs = minimalConfigs;
        this.includeLocation = includeLocation;
        this.includeDeviceConfigurations = includeDeviceConfigurations;
    }

    private DevicesFilter(Builder builder) {
        displayName = builder.displayName;
        workspaceId = builder.workspaceId;
        personId = builder.personId;
        orgId = builder.orgId;
        capabilities = builder.capabilities;
        permissions = builder.permissions;
        connectionStatus = builder.connectionStatus;
        product = builder.product;
        tags = builder.tags;
        mac = builder.mac;
        serial = builder.serial;
        software = builder.software;
        errorCodes = builder.errorCodes;
        upgradeChannel = builder.upgradeChannel;
        type = builder.type;
        key = builder.key;
        includeDeviceConfigurations = builder.includeDeviceConfigurations;
        includeLocation = builder.includeLocation;
        minimalConfigs = builder.minimalConfigs;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void addTo(Map<String, Object> params) {
        if (displayName != null) {
            params.put("displayName", displayName);
        }
        if (workspaceId != null) {
            params.put("workspaceId", workspaceId);
        }
        if (personId != null) {
            params.put("personId", personId);
        }
        if (orgId != null) {
            params.put("orgId", orgId);
        }
        if (capabilities != null && !capabilities.isEmpty()) {
            params.put("capability", String.join(",", capabilities));
        }
        if (permissions != null) {
            params.put("permission", String.join(",", permissions));
        }
        if (product != null) {
            params.put("product", product);
        }
        if (type != null) {
            params.put("type", type);
        }
        if (tags != null) {
            params.put("tag", String.join(",", tags));
        }
        if (mac != null) {
            params.put("mac", mac);
        }
        if (serial != null) {
            params.put("serial", serial);
        }
        if (software != null) {
            params.put("software", software);
        }
        if (upgradeChannel != null) {
            params.put("upgradeChannel", upgradeChannel);
        }
        if (errorCodes != null) {
            params.put("errorCode", String.join(",", errorCodes));
        }
        if (connectionStatus != null) {
            params.put("connectionStatus", connectionStatus);
        }
        if (minimalConfigs) {
            params.put("minimalConfigs", "true");
        }
        if (includeLocation) {
            params.put("includeLocation", "true");
        }
        if (includeDeviceConfigurations) {
            params.put("includeDeviceConfigurations", "true");
        }
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

    public String getMac() {
        return mac;
    }

    public String getSerial() {
        return serial;
    }

    public String getSoftware() {
        return software;
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

    public Key getKey() {
        return key;
    }

    public boolean isIncludeDeviceConfigurations() {
        return includeDeviceConfigurations;
    }

    public boolean isIncludeLocation() {
        return includeLocation;
    }

    public boolean isMinimalConfigs() {
        return minimalConfigs;
    }


    public static final class Builder {
        private String displayName;
        private String workspaceId;
        private String personId;
        private String orgId;
        private Set<String> capabilities;
        private Set<String> permissions;
        private String connectionStatus;
        private String product;
        private Set<String> tags;
        private String mac;
        private String serial;
        private String software;
        private Set<String> errorCodes;
        private String upgradeChannel;
        private DeviceCategory type;
        private Key key;
        private boolean includeDeviceConfigurations;
        private boolean includeLocation;
        private boolean minimalConfigs;

        private Builder() {
        }

        public Builder displayName(String val) {
            displayName = val;
            return this;
        }

        public Builder workspaceId(String val) {
            workspaceId = val;
            return this;
        }

        public Builder personId(String val) {
            personId = val;
            return this;
        }

        public Builder orgId(String val) {
            orgId = val;
            return this;
        }

        public Builder capabilities(Set<String> val) {
            capabilities = val;
            return this;
        }

        public Builder permissions(Set<String> val) {
            permissions = val;
            return this;
        }

        public Builder connectionStatus(String val) {
            connectionStatus = val;
            return this;
        }

        public Builder product(String val) {
            product = val;
            return this;
        }

        public Builder tags(Set<String> val) {
            tags = val;
            return this;
        }

        public Builder mac(String val) {
            mac = val;
            return this;
        }

        public Builder serial(String val) {
            serial = val;
            return this;
        }

        public Builder software(String val) {
            software = val;
            return this;
        }

        public Builder errorCodes(Set<String> val) {
            errorCodes = val;
            return this;
        }

        public Builder upgradeChannel(String val) {
            upgradeChannel = val;
            return this;
        }

        public Builder type(DeviceCategory val) {
            type = val;
            return this;
        }

        public Builder key(Key val) {
            key = val;
            return this;
        }

        public Builder includeDeviceConfigurations(boolean val) {
            includeDeviceConfigurations = val;
            return this;
        }

        public Builder includeLocation(boolean val) {
            includeLocation = val;
            return this;
        }

        public Builder minimalConfigs(boolean val) {
            minimalConfigs = val;
            return this;
        }

        public DevicesFilter build() {
            return new DevicesFilter(this);
        }
    }
}
