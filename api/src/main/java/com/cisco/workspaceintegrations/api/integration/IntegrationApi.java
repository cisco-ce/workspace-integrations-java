package com.cisco.workspaceintegrations.api.integration;

import com.cisco.workspaceintegrations.api.core.WebexDeveloperApi;
import com.cisco.workspaceintegrations.api.core.WebexHttp;
import com.cisco.workspaceintegrations.common.integration.Integration;
import com.cisco.workspaceintegrations.common.integration.IntegrationUpdate;

public class IntegrationApi extends WebexDeveloperApi {

    public IntegrationApi(WebexHttp webexHttp) {
        super(webexHttp, webexHttp.getProvisioning().getAppUrl());
    }

    public Integration postUpdate(IntegrationUpdate update) {
        return this.getWebexHttp().patch(getBaseUrl(), update, Integration.class);
    }

    public void deactivateIntegration() {
        this.getWebexHttp().delete(getBaseUrl());
    }
}
