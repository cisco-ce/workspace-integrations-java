package com.cisco.workspaceintegrations.api.core;

import java.util.Map;

import com.cisco.workspaceintegrations.common.ValueObject;

public abstract class Filter extends ValueObject {

    public abstract void addTo(Map<String, Object> params);
}
