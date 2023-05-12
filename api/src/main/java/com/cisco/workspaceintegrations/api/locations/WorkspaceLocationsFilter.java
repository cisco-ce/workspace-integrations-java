package com.cisco.workspaceintegrations.api.locations;

import java.util.Map;

import com.cisco.workspaceintegrations.api.core.Filter;
import com.cisco.workspaceintegrations.common.locations.CountryCode;

import static com.cisco.workspaceintegrations.common.Utils.toPiiLengthString;

public class WorkspaceLocationsFilter extends Filter {
    private final String orgId;
    private final String displayName;
    private final String address;
    private final CountryCode countryCode;
    private final String cityName;

    public WorkspaceLocationsFilter(String orgId, String displayName, String address, CountryCode countryCode, String cityName) {
        this.orgId = orgId;
        this.displayName = displayName;
        this.address = address;
        this.countryCode = countryCode;
        this.cityName = cityName;
    }

    private WorkspaceLocationsFilter(Builder builder) {
        orgId = builder.orgId;
        displayName = builder.displayName;
        address = builder.address;
        countryCode = builder.countryCode;
        cityName = builder.cityName;
    }

    @Override
    public void addTo(Map<String, Object> params) {
        if (orgId != null) {
            params.put("orgId", orgId);
        }
        if (displayName != null) {
            params.put("displayName", displayName);
        }
        if (address != null) {
            params.put("address", address);
        }
        if (countryCode != null) {
            params.put("countryCode", countryCode);
        }
        if (cityName != null) {
            params.put("cityName", cityName);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String geOrgId() {
        return orgId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAddress() {
        return address;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return "{"
            + ", orgId=" + orgId
            + ", name='" + toPiiLengthString(displayName) + '\''
            + ", address='" + toPiiLengthString(address) + '\''
            + ", countryCode='" + countryCode + '\''
            + ", cityName='" + cityName + '\''
            + '}';
    }

    public static final class Builder {
        private String orgId;
        private String displayName;
        private String address;
        private CountryCode countryCode;
        private String cityName;

        private Builder() {
        }

        public Builder orgId(String val) {
            orgId = val;
            return this;
        }

        public Builder displayName(String val) {
            displayName = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder countryCode(CountryCode val) {
            countryCode = val;
            return this;
        }

        public Builder cityName(String val) {
            cityName = val;
            return this;
        }

        public WorkspaceLocationsFilter build() {
            return new WorkspaceLocationsFilter(this);
        }
    }
}
