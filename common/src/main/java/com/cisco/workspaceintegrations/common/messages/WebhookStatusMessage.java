package com.cisco.workspaceintegrations.common.messages;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import com.cisco.workspaceintegrations.common.Utils;
import com.cisco.workspaceintegrations.common.xapi.Key;
import com.cisco.workspaceintegrations.common.xapi.StatusConverter;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

public class WebhookStatusMessage extends WebhookDeviceMessage {

    private final Instant timestamp;
    private final WebhookStatusChanges changes;
    private final boolean isFullSync;

    @JsonCreator
    public WebhookStatusMessage(@JsonProperty("deviceId") String deviceId,
                                @JsonProperty("workspaceId") String workspaceId,
                                @JsonProperty("orgId") String orgId,
                                @JsonProperty("appId") String appId,
                                @JsonProperty("timestamp") Instant timestamp,
                                @JsonProperty("changes") WebhookStatusChanges changes,
                                @JsonProperty("isFullSync") boolean isFullSync) {
        super(deviceId, workspaceId, orgId, appId);
        this.timestamp = checkNotNull(timestamp);
        this.changes = checkNotNull(changes);
        this.isFullSync = isFullSync;
    }

    @JsonProperty("timestamp")
    public Instant timestamp() {
        return timestamp;
    }

    @JsonProperty("changes")
    public WebhookStatusChanges changes() {
        return changes;
    }

    @JsonProperty("isFullSync")
    public boolean isFullSync() {
        return isFullSync;
    }

    @Override
    public String type() {
        return WEBHOOK_STATUS_MESSAGE_TYPE;
    }

    public <T> Optional<T> getUpdatedValue(Key key, Class<T> objectType) {
        return changes.getUpdatedValue(key, objectType);
    }

    public Optional<String> getUpdatedStringValue(Key key) {
        return changes.getUpdatedValue(key, String.class);
    }

    public Optional<Integer> getUpdatedIntegerValue(Key key) {
        return changes.getUpdatedValue(key, Integer.class);
    }

    @Override
    public String toString() {
        return "{timestamp=" + timestamp
            + ", changes=" + changes
            + ", isFullSync=" + isFullSync
            + ", deviceId=" + deviceId()
            + ", workspaceId=" + workspaceId()
            + ", orgId=" + orgId()
            + '}';
    }

    public static final class WebhookStatusChanges {

        private final Map<Key, JsonNode> updated;
        private final Set<Key> removed;

        @JsonCreator
        public WebhookStatusChanges(@JsonProperty("updated") Map<Key, JsonNode> updated,
                                    @JsonProperty("removed") Set<Key> removed) {
            this.updated = updated == null ? emptyMap() : unmodifiableMap(updated);
            this.removed = Utils.toUnmodifiableSet(removed);
        }

        public Map<Key, JsonNode> updated() {
            return updated;
        }

        public Set<Key> removed() {
            return removed;
        }

        public boolean isUpdated(Key key) {
            return updated.keySet().stream().anyMatch(key::encloses);
        }

        boolean isRemoved(Key key) throws IllegalArgumentException {
            return removed.stream().anyMatch(key::encloses);
        }

        public <T> Optional<T> getUpdatedValue(Key key, Class<T> objectType) {
            return StatusConverter.getObject(key, objectType, updated);
        }

        @Override
        public String toString() {
            return "{updated=" + updated + ", removed=" + removed + '}';
        }
    }
}
