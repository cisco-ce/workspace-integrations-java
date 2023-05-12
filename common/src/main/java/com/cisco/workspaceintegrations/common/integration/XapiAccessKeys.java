package com.cisco.workspaceintegrations.common.integration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.Utils;
import com.cisco.workspaceintegrations.common.ValueObject;
import com.cisco.workspaceintegrations.common.xapi.Key;

public final class XapiAccessKeys extends ValueObject {
    private final Set<Key> commands;
    private final Set<Key> statuses;
    private final Set<Key> events;

    @JsonCreator
    private XapiAccessKeys(@JsonProperty(value = "commands") Set<Key> commands,
                           @JsonProperty(value = "statuses") Set<Key> statuses,
                           @JsonProperty(value = "events") Set<Key> events) {
        this.commands = Utils.toUnmodifiableSet(commands);
        this.statuses = Utils.toUnmodifiableSet(statuses);
        this.events = Utils.toUnmodifiableSet(events);
    }

    public Set<Key> getCommands() {
        return commands;
    }

    public Set<Key> getStatuses() {
        return statuses;
    }

    public Set<Key> getEvents() {
        return events;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static XapiAccessKeys noAccess() {
        return builder().build();
    }

    @Override
    public String toString() {
        return "{commands=" + commands
            + ", statuses=" + statuses
            + ", events=" + events
            + "}";
    }

    public static final class Builder {
        private final Set<Key> commands;
        private final Set<Key> statuses;
        private final Set<Key> events;

        Builder() {
            commands = new HashSet<>();
            statuses = new HashSet<>();
            events = new HashSet<>();
        }

        public Builder allowCommands(Collection<Key> keys) {
            commands.addAll(keys);
            return this;
        }

        public Builder allowStatuses(Collection<Key> keys) {
            statuses.addAll(keys);
            return this;
        }

        public Builder allowEvents(Collection<Key> keys) {
            events.addAll(keys);
            return this;
        }

        public XapiAccessKeys build() {
            return new XapiAccessKeys(commands, statuses, events);
        }
    }
}
