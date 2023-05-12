package com.cisco.workspaceintegrations.api.locations;

import java.util.HashMap;
import java.util.Map;

import com.cisco.workspaceintegrations.api.core.ListResponse;
import com.cisco.workspaceintegrations.api.core.WebexDeveloperApi;
import com.cisco.workspaceintegrations.api.core.WebexHttp;
import com.cisco.workspaceintegrations.common.locations.WorkspaceLocation;
import com.cisco.workspaceintegrations.common.locations.WorkspaceLocationRequest;

/**
 * <a href="https://developer.webex.com/docs/api/v1/workspace-locations">Workspace Locations API docs</a>
 * <p>
 * A Workspace Location is a physical location with a name, address, country, city, latitude and longitude.
 * <p>
 * Viewing the list of locations in an organization requires an administrator auth token with the spark-admin:workspace_locations_read scope. Adding, updating, or deleting workspace locations in an organization requires an administrator auth token with the spark-admin:workspace_locations_write scope.
 * The Workspace Locations API can also be used by partner administrators acting as administrators of a different organization than their own. In those cases an orgId value must be supplied, as indicated in the reference documentation for the relevant endpoints.
 */
public class WorkspaceLocationsApi extends WebexDeveloperApi {

    public WorkspaceLocationsApi(WebexHttp webexHttp) {
        super(webexHttp, "workspaceLocations");
    }

    /**
     * Create a workspace location: <a href="https://developer.webex.com/docs/api/v1/workspace-locations/create-a-workspace-location">API docs</a>
     * Required scope: <i>spark-admin:workspace_locations_write</i>
     */
    public WorkspaceLocation createLocation(WorkspaceLocationRequest locationRequest) {
        return this.getWebexHttp().post(getBaseUrl(), locationRequest, WorkspaceLocation.class);
    }

    /**
     * Update a workspace location: <a href="https://developer.webex.com/docs/api/v1/workspace-locations/update-a-workspace-location">API docs</a>
     * Required scope: <i>spark-admin:workspace_locations_write</i>
     */
    public WorkspaceLocation updateLocation(String locationId, WorkspaceLocationRequest locationRequest) {
        return this.getWebexHttp().put(urlFor(locationId), locationRequest, WorkspaceLocation.class);
    }

    /**
     * Read a workspace location: <a href="https://developer.webex.com/docs/api/v1/workspace-locations/get-a-workspace-location-details">API docs</a>
     * Required scope: <i>spark-admin:workspace_locations_read</i>
     */
    public WorkspaceLocation getLocation(String locationId) {
        return this.getWebexHttp().get(urlFor(locationId), WorkspaceLocation.class);
    }

    /**
     * Read all workspace location: <a href="https://developer.webex.com/docs/api/v1/workspace-locations/list-workspace-locations">API docs</a>
     * Required scope: <i>spark-admin:workspace_locations_read</i>
     */
    public ListResponse<WorkspaceLocation> getAllLocations() {
        return this.getWebexHttp().get(
            getBaseUrl(),
            new ListResponse.ListResponseReference<>(WorkspaceLocation.class)
        );
    }

    /**
     * Read workspace location matching the given filter: <a href="https://developer.webex.com/docs/api/v1/workspace-locations/list-workspace-locations">API docs</a>
     * Required scope: <i>spark-admin:workspace_locations_read</i>
     */
    public ListResponse<WorkspaceLocation> getLocations(WorkspaceLocationsFilter filter) {
        Map<String, Object> params = new HashMap<>();
        filter.addTo(params);
        return this.getWebexHttp().get(
            getBaseUrl(),
            new ListResponse.ListResponseReference<>(WorkspaceLocation.class),
            params
        );
    }

    /**
     * Delete a workspace location: <a href="https://developer.webex.com/docs/api/v1/workspace-locations/delete-a-workspace-location">API docs</a>
     * Required scope: <i>spark-admin:workspace_locations_write</i>
     */
    public void deleteLocation(String locationId) {
        this.getWebexHttp().delete(urlFor(locationId));
    }
}
