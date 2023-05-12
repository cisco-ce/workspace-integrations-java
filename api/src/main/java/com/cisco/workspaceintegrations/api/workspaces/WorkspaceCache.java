package com.cisco.workspaceintegrations.api.workspaces;

import java.time.Duration;

import com.cisco.workspaceintegrations.api.caching.InMemoryCache;
import com.cisco.workspaceintegrations.common.workspaces.Workspace;

/**
 * Simple in memory workspace cache. The default TTL is 5 minutes, but can be overriden.
 */
public class WorkspaceCache extends InMemoryCache<Workspace> {

    private final WorkspacesApi api;

    public WorkspaceCache(WorkspacesApi api) {
        this.api = api;
    }

    public WorkspaceCache(WorkspacesApi api, Duration ttl, int maxSize) {
        super(ttl, maxSize);
        this.api = api;
    }

    @Override
    protected Workspace load(String id) {
        return api.getWorkspace(id);
    }
}
