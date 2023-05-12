package com.cisco.workspaceintegrations.api.devices;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.api.core.ListResponse;
import com.cisco.workspaceintegrations.api.core.ListResponse.ListResponseReference;
import com.cisco.workspaceintegrations.api.core.WebexDeveloperApi;
import com.cisco.workspaceintegrations.api.core.WebexHttp;
import com.cisco.workspaceintegrations.common.devices.ActivationCode;
import com.cisco.workspaceintegrations.common.devices.Device;
import com.cisco.workspaceintegrations.common.workspaces.Workspace;

import static com.google.common.base.Preconditions.checkNotNull;

public class DevicesApi extends WebexDeveloperApi {

    public DevicesApi(WebexHttp webexHttp) {
        super(webexHttp, "devices");
    }

    public Device getDevice(String deviceId) {
        return this.getWebexHttp().get(urlFor(deviceId), Device.class);
    }

    public void deleteDevice(String deviceId) {
        this.getWebexHttp().delete(urlFor(deviceId));
    }

    public ListResponse<Workspace> getDevices(int start, int max) {
        return this.getWebexHttp().get(
            getBaseUrl(),
            new ListResponseReference<>(Workspace.class),
            ImmutableMap.of("start", start, "max", max)
        );
    }

    public ListResponse<Device> getDevices(int start, int max, DevicesFilter filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("max", max);
        filter.addTo(params);
        return this.getWebexHttp().get(
            getBaseUrl(),
            new ListResponseReference<>(Device.class),
            params
        );
    }

    public ActivationCode createActivationCode(String workspaceId) {
        return this.getWebexHttp().post(urlFor("activationCode"), new CreateActivationCode(workspaceId), ActivationCode.class);
    }

    static class CreateActivationCode {
        private final String workspaceId;

        @JsonCreator
        CreateActivationCode(@JsonProperty("workspaceId") String workspaceId) {
            this.workspaceId = checkNotNull(workspaceId);
        }

        public String getWorkspaceId() {
            return workspaceId;
        }
    }
}
