package com.cisco.workspaceintegrations.api.xapi;

import java.net.URI;

import com.google.common.collect.ImmutableMap;

import com.cisco.workspaceintegrations.api.core.WebexDeveloperApi;
import com.cisco.workspaceintegrations.api.core.WebexHttp;
import com.cisco.workspaceintegrations.common.xapi.CommandRequest;
import com.cisco.workspaceintegrations.common.xapi.CommandResponse;
import com.cisco.workspaceintegrations.common.xapi.Key;
import com.cisco.workspaceintegrations.common.xapi.StatusResponse;

import static com.cisco.workspaceintegrations.common.xapi.Key.key;

/**
 * <a href="https://developer.webex.com/docs/api/v1/xapi">xAPI API docs</a>
 * The xAPI allows developers to programmatically invoke commands and query the status of devices that run Webex RoomOS software.
 * See <a href="https://roomos.cisco.com/xapi">roomos.cisco.com</a> for resources on the xAPI.
 */
public class XAPI extends WebexDeveloperApi {

    private final URI statusUrl;

    public XAPI(WebexHttp webexHttp) {
        super(webexHttp, "xapi");
        this.statusUrl = urlFor("status");
    }

    public StatusResponse getStatus(String deviceId, Key name) {
        return this.getWebexHttp().get(
            statusUrl,
            StatusResponse.class,
            ImmutableMap.of("deviceId", deviceId, "name", name)
        );
    }

    public StatusResponse getAllStatuses(String deviceId) {
        return this.getWebexHttp().get(
            statusUrl,
            StatusResponse.class,
            ImmutableMap.of("deviceId", deviceId, "name", key("*.*"))
        );
    }

    public CommandResponse executeCommand(Key command, CommandRequest request) {
        return this.getWebexHttp().post(urlFor("command/" + command), request, CommandResponse.class);
    }
}
