package com.cisco.workspaceintegrations.api.devices;

import java.time.Duration;

import com.cisco.workspaceintegrations.api.caching.InMemoryCache;
import com.cisco.workspaceintegrations.common.devices.Device;

/**
 * Simple in memory workspace cache. The default TTL is 5 minutes, but can be overriden.
 */
public class DeviceCache extends InMemoryCache<Device> {

    private final DevicesApi api;

    public DeviceCache(DevicesApi api) {
        this.api = api;
    }

    public DeviceCache(DevicesApi api, Duration ttl, int maxSize) {
        super(ttl, maxSize);
        this.api = api;
    }

    @Override
    protected Device load(String id) {
        return api.getDevice(id);
    }
}
