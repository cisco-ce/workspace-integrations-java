package com.cisco.workspaceintegrations.common.locations;

import java.util.Optional;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.Utils;
import com.cisco.workspaceintegrations.common.ValueObject;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class WorkspaceLocationRequest extends ValueObject {

    private final String orgId;
    private final String displayName;
    private final String address;
    private final CountryCode countryCode;
    private final String cityName;
    private final Double latitude;
    private final Double longitude;
    private final String notes;

    @JsonCreator
    public WorkspaceLocationRequest(
        @Nullable @JsonProperty(value = "orgId") String orgId,
        @JsonProperty("displayName") String displayName,
        @JsonProperty("address") String address,
        @JsonProperty("countryCode") CountryCode countryCode,
        @Nullable @JsonProperty("cityName") String cityName,
        @Nullable @JsonProperty("latitude") Double latitude,
        @Nullable @JsonProperty("longitude") Double longitude,
        @Nullable @JsonProperty(value = "notes") String notes) {

        Utils.checkRequiredArgument(countryCode, "countryCode is required");
        Utils.checkRequiredArgument("displayName", displayName, 100);
        Utils.checkRequiredArgument("address", address, 100);
        if (latitude != null) {
            Utils.checkMinMax("latitude", latitude, -90, 90);
            Utils.checkNotZero("latitude", latitude);
        }
        if (longitude != null) {
            Utils.checkMinMax("longitude", longitude, -180, 180);
            Utils.checkNotZero("longitude", longitude);
        }

        this.orgId = orgId;
        this.displayName = displayName;
        this.address = address;
        this.countryCode = countryCode;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.notes = notes;
    }

    private WorkspaceLocationRequest(Builder builder) {
        orgId = builder.orgId;
        displayName = builder.displayName;
        address = builder.address;
        countryCode = builder.countryCode;
        cityName = builder.cityName;
        latitude = builder.latitude;
        longitude = builder.longitude;
        notes = builder.notes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(WorkspaceLocation location) {
        return new Builder(location);
    }

    public Optional<String> getOrgId() {
        return Optional.ofNullable(orgId);
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

    public Optional<String> getCityName() {
        return Optional.ofNullable(cityName);
    }

    public Optional<Double> getLatitude() {
        return Optional.ofNullable(latitude);
    }

    public Optional<Double> getLongitude() {
        return Optional.ofNullable(longitude);
    }

    public Optional<String> getNotes() {
        return Optional.ofNullable(notes);
    }

    @Override
    public String toString() {
        return "{"
            + ", orgId=" + orgId
            + ", name=" + Utils.toPiiLengthString(displayName)
            + ", address=" + Utils.toPiiLengthString(address)
            + ", countryCode=" + countryCode
            + ", cityName=" + cityName
            + ", latitude=" + latitude
            + ", longitude=" + longitude
            + '}';
    }

    public static final class Builder {
        private String orgId;
        private String displayName;
        private String address;
        private CountryCode countryCode;
        private String cityName;
        private Double latitude;
        private Double longitude;
        private String notes;

        private Builder() {
        }

        private Builder(WorkspaceLocation location) {
            this.displayName = location.getDisplayName();
            this.address = location.getAddress();
            this.countryCode = location.getCountryCode();
            this.cityName = location.getCityName().orElse(null);
            this.latitude = location.getLatitude().orElse(null);
            this.longitude = location.getLongitude().orElse(null);
            this.notes = location.getNotes().orElse(null);
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

        public Builder latitude(Double val) {
            latitude = val;
            return this;
        }

        public Builder longitude(Double val) {
            longitude = val;
            return this;
        }

        public Builder notes(String val) {
            notes = val;
            return this;
        }

        public WorkspaceLocationRequest build() {
            return new WorkspaceLocationRequest(this);
        }
    }
}
