package com.cisco.workspaceintegrations.common.integration.features;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

public class CrossLaunch extends ValueObject {

    private final ManageContent manageContent;
    private final AssignContent assignContent;

    @JsonCreator
    public CrossLaunch(@JsonProperty(value = "manageContent") ManageContent manageContent,
                       @JsonProperty(value = "assignContent") AssignContent assignContent) {
        this.manageContent = manageContent;
        this.assignContent = assignContent;
    }

    public Optional<ManageContent> getManageContent() {
        return Optional.ofNullable(manageContent);
    }

    public Optional<AssignContent> getAssignContent() {
        return Optional.ofNullable(assignContent);
    }
}
