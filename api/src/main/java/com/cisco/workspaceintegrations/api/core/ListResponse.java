package com.cisco.workspaceintegrations.api.core;

import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListResponse<E> {

    private final List<E> items;

    @JsonCreator
    public ListResponse(@JsonProperty("item") List<E> items) {
        this.items = items;
    }

    public List<E> getItems() {
        return items;
    }

    public static class ListResponseReference<T> extends TypeReference<ListResponse<T>> {

        private final Class<T> elementType;

        public ListResponseReference(Class<T> elementType) {
            this.elementType = checkNotNull(elementType);
        }

        @Override
        public Type getType() {
            return TypeFactory.defaultInstance().constructParametricType(ListResponse.class, elementType);
        }
    }
}
