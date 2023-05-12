package com.cisco.workspaceintegrations.common.xapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import com.cisco.workspaceintegrations.common.json.Json;
import com.cisco.workspaceintegrations.common.xapi.Key.Segment;

import static java.util.stream.Collectors.toMap;

public final class StatusConverter {

    private StatusConverter() {
    }

    @SuppressFBWarnings("UC_USELESS_OBJECT")
    public static JsonNode mapToTree(Map<Key, JsonNode> status) {
        status.keySet().stream().filter(key -> !key.isAbsolute()).findFirst().ifPresent(key -> {
            throw new IllegalArgumentException(key + " is not absolute");
        });
        try {
            ObjectNode root = JsonNodeFactory.instance.objectNode();
            status.forEach((key, value) -> {
                ObjectNode current = root;
                for (int i = 0; i < key.segments().size(); i++) {
                    Segment segment = key.segments().get(i);
                    if (i == key.segments().size() - 1) {
                        if (segment.isArray()) {
                            getOrCreateArray(current, segment).set("value", value);
                        } else {
                            current.set(segment.value(), value);
                        }
                    } else {
                        current = segment.isArray() ? getOrCreateArray(current, segment) : getOrCreateObject(current, segment);
                    }
                }
            });
            return root;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to transform flat status to tree structure", ex);
        }
    }

    public static <T> Optional<T> getObject(Key objectKey, Class<T> objectType, Map<Key, JsonNode> values) {
        if (!objectKey.isAbsolute()) {
            throw new IllegalArgumentException("Object key must be absolute. For key: " + objectKey);
        }
        if (objectType.isAssignableFrom(Collection.class)) {
            throw new IllegalArgumentException(
                "Collection types should not go here. "
                    + "Use getArray(Key, Class<T>) or getObject(Key, TypeReference<T>) instead."
            );
        }
        return getObject(objectKey, values).map(
            o -> Json.objectMapper().convertValue(o.isArray() ? o.get(0) : o, objectType)
        );
    }

    public static Optional<List<JsonNode>> getList(Key arrayKey, Map<Key, JsonNode> values) {
        if (!arrayKey.lastSegment().isArray()) {
            throw new IllegalArgumentException("Last segment of key must be an array element. For key: " + arrayKey);
        }
        return getObject(arrayKey, values).map(
            node -> {
                if (!node.isArray()) {
                    throw new IllegalStateException("Expected node " + node + " to be an array");
                }
                List<JsonNode> list = new ArrayList<>();
                node.elements().forEachRemaining(list::add);
                return list;
            }
        );
    }

    private static Optional<JsonNode> getObject(Key key, Map<Key, JsonNode> values) {
        if (key.lastSegment().isSegmentWildcard()) {
            throw new IllegalArgumentException("Last segment of object key cannot be a segment wildcard. For key: " + key);
        }
        List<Segment> segmentsToStrip = key.segmentsBefore(key.lastSegment());
        JsonNode root = StatusConverter.mapToTree(
            values.entrySet().stream()
                  .filter(e -> key.encloses(e.getKey()) || key.append("*").encloses(e.getKey()))
                  .collect(toMap(e -> e.getKey().strip(segmentsToStrip), Map.Entry::getValue))
        );
        return Optional.ofNullable(root.get(key.lastSegment().value()));
    }

    private static ObjectNode getOrCreateObject(ObjectNode container, Segment segment) {
        JsonNode existing = container.get(segment.value());
        if (existing == null) {
            ObjectNode node = JsonNodeFactory.instance.objectNode();
            container.set(segment.value(), node);
            return node;
        } else {
            return (ObjectNode) existing;
        }
    }

    private static ObjectNode getOrCreateArray(ObjectNode container, Segment segment) {
        JsonNode existing = container.get(segment.value());
        ArrayNode array;
        if (existing == null) {
            array = JsonNodeFactory.instance.arrayNode();
            container.set(segment.value(), array);
        } else {
            array = (ArrayNode) existing;
        }
        return getOrCreateArrayElement(array, segment.array().absoluteIndex());
    }

    private static ObjectNode getOrCreateArrayElement(ArrayNode array, int index) {
        for (JsonNode element : array) {
            if (element.get("id").asInt() == index) {
                return (ObjectNode) element;
            }
        }
        ObjectNode element = JsonNodeFactory.instance.objectNode();
        element.put("id", index);
        array.add(element);
        return element;
    }
}
