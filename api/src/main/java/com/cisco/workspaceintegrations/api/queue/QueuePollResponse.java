package com.cisco.workspaceintegrations.api.queue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;
import com.cisco.workspaceintegrations.common.messages.Message;

import static java.util.Collections.emptyList;

public final class QueuePollResponse extends ValueObject {

    private final List<Message> messages;

    @JsonCreator
    public QueuePollResponse(@JsonProperty("messages") List<Message> messages) {
        this.messages = messages == null ? emptyList() : messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "{messages=" + messages + "}";
    }
}
