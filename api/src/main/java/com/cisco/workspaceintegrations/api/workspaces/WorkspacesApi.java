package com.cisco.workspaceintegrations.api.workspaces;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import com.cisco.workspaceintegrations.api.core.ListResponse;
import com.cisco.workspaceintegrations.api.core.WebexDeveloperApi;
import com.cisco.workspaceintegrations.api.core.WebexHttp;
import com.cisco.workspaceintegrations.common.workspaces.CreateWorkspace;
import com.cisco.workspaceintegrations.common.workspaces.UpdateWorkspace;
import com.cisco.workspaceintegrations.common.workspaces.Workspace;

/**
 * <a href="https://developer.webex.com/docs/api/v1/workspaces">Workspaces API docs</a>
 * Workspaces represent where people work, such as conference rooms, meeting spaces, lobbies, and lunch rooms. Devices may be associated with workspaces.
 * <p>
 * Viewing the list of workspaces in an organization requires an administrator auth token with the spark-admin:workspaces_read scope. Adding, updating, or deleting workspaces in an organization requires an administrator auth token with the spark-admin:workspaces_write scope.
 * <p>
 * The Workspaces API can also be used by partner administrators acting as administrators of a different organization than their own. In those cases an orgId value must be supplied, as indicated in the reference documentation for the relevant endpoints.
 */
public class WorkspacesApi extends WebexDeveloperApi {

    public WorkspacesApi(WebexHttp webexHttp) {
        super(webexHttp, "workspaces");
    }

    /**
     * Create a workspace: <a href="https://developer.webex.com/docs/api/v1/workspaces/create-a-workspace">API docs</a>
     * Required scope: <i>spark-admin:workspaces_write</i>
     */
    public Workspace createWorkspace(CreateWorkspace create) {
        return this.getWebexHttp().post(getBaseUrl(), create, Workspace.class);
    }

    /**
     * Update a workspace: <a href="https://developer.webex.com/docs/api/v1/workspaces/update-a-workspace">API docs</a>
     * Required scope: <i>spark-admin:workspaces_write</i>
     */
    public Workspace updateWorkspace(String workspaceId, UpdateWorkspace update) {
        return this.getWebexHttp().put(urlFor(workspaceId), update, Workspace.class);
    }

    /**
     * Read a workspace: <a href="https://developer.webex.com/docs/api/v1/workspaces/get-workspace-details">API docs</a>
     * Required scope: <i>spark-admin:workspaces_read</i>
     */
    public Workspace getWorkspace(String workspaceId) {
        return this.getWebexHttp().get(urlFor(workspaceId), Workspace.class);
    }

    /**
     * Read workspaces: <a href="https://developer.webex.com/docs/api/v1/workspaces/get-workspace-details">API docs</a>
     * Required scope: <i>spark-admin:workspaces_read</i>
     *
     * @param start The start index for pagination (0 based)
     * @param max   The max number of workspaces per page (upper limit is 1000)
     */
    public ListResponse<Workspace> getWorkspaces(int start, int max) {
        return this.getWebexHttp().get(
            getBaseUrl(),
            new ListResponse.ListResponseReference<>(Workspace.class),
            ImmutableMap.of("start", start, "max", max)
        );
    }

    /**
     * Read workspaces matching the given filter: <a href="https://developer.webex.com/docs/api/v1/workspaces/get-workspace-details">API docs</a>
     * Required scope: <i>spark-admin:workspaces_read</i>
     *
     * @param start  The start index for pagination (0 based)
     * @param max    The max number of workspaces per page (upper limit is 1000)
     * @param filter To filter workspaces by type, displayName, calling, calendar etc.
     */
    public ListResponse<Workspace> getWorkspaces(int start, int max, WorkspacesFilter filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("max", max);
        filter.addTo(params);
        return this.getWebexHttp().get(
            getBaseUrl(),
            new ListResponse.ListResponseReference<>(Workspace.class),
            params
        );
    }

    public void deleteWorkspace(String workspaceId) {
        this.getWebexHttp().delete(urlFor(workspaceId));
    }
}
