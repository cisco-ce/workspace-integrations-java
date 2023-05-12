package com.cisco.workspaceintegrations.common.actions;

public class UpdateApproved extends Action {

    private final int manifestVersion;

    public UpdateApproved(String orgId, String appId, int manifestVersion) {
        super(orgId, appId);
        this.manifestVersion = manifestVersion;
    }

    public int getManifestVersion() {
        return manifestVersion;
    }

    @Override
    public String toString() {
        return "UpdateApproved: orgId=" + getOrgId() + ", appId=" + getAppId() + ", manifestVersion=" + getManifestVersion();
    }
}
