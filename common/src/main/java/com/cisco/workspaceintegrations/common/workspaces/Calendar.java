package com.cisco.workspaceintegrations.common.workspaces;

import java.util.Optional;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.Utils;
import com.cisco.workspaceintegrations.common.ValueObject;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Calendar extends ValueObject {

    private static final Calendar NO_CALENDAR = new Calendar(CalendarType.NONE, null);

    private final CalendarType type;
    private final String emailAddress;

    @JsonCreator
    public Calendar(@JsonProperty("type") CalendarType type,
                    @JsonProperty("emailAddress") @Nullable String emailAddress) {
        this.type = type;
        this.emailAddress = emailAddress;
    }

    @JsonProperty("type")
    public CalendarType getType() {
        return type;
    }

    @JsonProperty("emailAddress")
    public Optional<String> getEmailAddress() {
        return Optional.ofNullable(emailAddress);
    }

    @Override
    public String toString() {
        return "{type=" + type + ", emailAddress=" + Utils.toPiiLengthString(emailAddress) + "}";
    }

    public boolean hasCalendarAndEmailAddress() {
        return !type.isNone() && !type.isUnknown() && emailAddress != null;
    }
}
