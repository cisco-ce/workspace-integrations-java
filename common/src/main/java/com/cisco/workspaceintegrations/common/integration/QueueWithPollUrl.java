package com.cisco.workspaceintegrations.common.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QueueWithPollUrl extends Queue {

    private final String pollUrl;

    @JsonCreator
    public QueueWithPollUrl(@JsonProperty(value = "pollUrl") String pollUrl,
                            @JsonProperty(value = "state") QueueState state) {
        super(state);
        this.pollUrl = pollUrl;
    }

    public String getPollUrl() {
        return pollUrl;
    }

    @Override
    public String toString() {
        return "{pollUrl=" + pollUrl + ", state=" + getState() + '}';
    }
}
