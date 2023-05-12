package com.cisco.workspaceintegrations.common.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public final class Json {

    private static final ObjectMapper MAPPER = newObjectMapper();

    private Json() {
    }

    @SuppressFBWarnings("MS_EXPOSE_REP")
    public static ObjectMapper objectMapper() {
        return MAPPER;
    }

    public static <T> String toJsonString(T object) {
        try {
            return objectMapper().writer().writeValueAsString(object);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to serialize object to json", ex);
        }
    }

    public static <T> JsonNode toJsonNode(T object) {
        return objectMapper().valueToTree(object);
    }

    public static <T> T fromJsonString(String json, Class<T> type) {
        try {
            return objectMapper().readValue(json, type);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to de-serialize json: " + json, ex);
        }
    }

    public static <T> T fromJsonString(String json, TypeReference<T> type) {
        try {
            return objectMapper().readValue(json, type);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to de-serialize json: " + json, ex);
        }
    }

    public static <T> T fromJsonNode(JsonNode json, Class<T> type) {
        return objectMapper().convertValue(json, type);
    }

    public static <T> T fromJsonNode(JsonNode json, TypeReference<T> type) {
        return objectMapper().convertValue(json, type);
    }

    public static Optional<MatchingNode> getMatchingNode(JsonNode node, String... elements) {
        return getMatchingNodes(node, elements).stream().findFirst();
    }

    public static List<MatchingNode> getMatchingNodes(JsonNode node, String... elements) {
        return getMatchingNodes(node, Arrays.asList(elements), null);
    }

    private static List<MatchingNode> getMatchingNodes(JsonNode node, List<String> elements, String idElement) {
        List<MatchingNode> matchingNodes = new ArrayList<>();
        JsonNode n = node;
        for (String element : elements) {
            n = n.get(element);
            if (n == null) {
                return matchingNodes;
            }
            if (n.isArray()) {
                List<String> subElements = elements.subList(elements.indexOf(element) + 1, elements.size());
                n.iterator().forEachRemaining(subNode -> {
                    String id = null;
                    if (idElement != null) {
                        id = subNode.get(idElement).asText();
                    }
                    matchingNodes.addAll(getMatchingNodes(subNode, subElements, id == null ? idElement : id));
                });
            }
        }
        matchingNodes.add(new MatchingNode(n, idElement));
        return matchingNodes;
    }

    public static class MatchingNode {
        private final JsonNode valueNode;
        private final String id;

        MatchingNode(JsonNode valueNode, String id) {
            this.valueNode = valueNode;
            this.id = id;
        }

        public JsonNode getValueNode() {
            return valueNode;
        }

        public String getId() {
            return id;
        }
    }

    private static ObjectMapper newObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(mapper.getVisibilityChecker()
                                   .with(JsonAutoDetect.Visibility.NONE)
                                   .withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        configure(mapper);
        mapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        return mapper;
    }

    private static void configure(ObjectMapper mapper) {
        configureModules(mapper);
        configureSerialization(mapper);
        configureDeserialization(mapper);
    }


    private static void configureModules(ObjectMapper mapper) {
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new CustomJdk8Module());
    }

    private static void configureDeserialization(ObjectMapper mapper) {
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, false);
        mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, false);
        mapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, false);
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, false);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Changed from default
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true); // Changed from default
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true); // Changed from default
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true);
        mapper.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true); // Changed from default
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        mapper.configure(DeserializationFeature.WRAP_EXCEPTIONS, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
        mapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, false);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false);
        mapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false); // Changed from default
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false); // Changed from default
        mapper.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, true);

        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, false);
        mapper.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, false);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);
        mapper.configure(JsonParser.Feature.STRICT_DUPLICATE_DETECTION, false);
        mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, false);
    }

    private static void configureSerialization(ObjectMapper mapper) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);
        mapper.configure(SerializationFeature.WRAP_EXCEPTIONS, true);
        mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, true);
        mapper.configure(SerializationFeature.CLOSE_CLOSEABLE, false);
        mapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);  // Changed from default
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false); // Changed from default
        mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);
        mapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
        mapper.configure(SerializationFeature.EAGER_SERIALIZER_FETCH, true);
        mapper.configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, false);

        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false); // Changed from default
        mapper.configure(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM, true);
        mapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, false);
        mapper.configure(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION, false);
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, false);
    }

    private static final class CustomJdk8Module extends Module {

        @Override
        public String getModuleName() {
            return getClass().getSimpleName();
        }

        @Override
        public Version version() {
            return new Version(1, 0, 0, null, null, null);
        }

        @Override
        public void setupModule(Module.SetupContext setupContext) {
            setupContext.addDeserializers(new CustomJdk8Deserializers());
        }

    }

    private static final class CustomJdk8Deserializers extends Deserializers.Base {

        @Override
        public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType,
                                                             DeserializationConfig config,
                                                             BeanDescription beanDesc,
                                                             TypeDeserializer contentTypeDeserializer,
                                                             JsonDeserializer<?> contentDeserializer) {
            if (refType.hasRawClass(Optional.class)) {
                return new CustomOptionalDeserializer(refType, null, contentTypeDeserializer, contentDeserializer);
            }
            return null;
        }

    }

    @SuppressFBWarnings({
        "SE_INNER_CLASS",
        "SE_NO_SERIALVERSIONID"
    })
    private static final class CustomOptionalDeserializer extends ReferenceTypeDeserializer<Optional<?>> {

        CustomOptionalDeserializer(JavaType type,
                                   ValueInstantiator valueInstantiator,
                                   TypeDeserializer typeDeserializer,
                                   JsonDeserializer<?> jsonDeserializer) {
            super(type, valueInstantiator, typeDeserializer, jsonDeserializer);
        }

        @Override
        public CustomOptionalDeserializer withResolved(TypeDeserializer typeDeserializer,
                                                       JsonDeserializer<?> jsonDeserializer) {
            return new CustomOptionalDeserializer(_fullType, _valueInstantiator, typeDeserializer, jsonDeserializer);
        }

        @Override
        public Optional<?> getNullValue(DeserializationContext context) throws JsonMappingException {
            Object value = _valueDeserializer.getNullValue(context);
            if (value instanceof JsonNode) {
                JsonNode node = (JsonNode) value;
                if (node.isNull() || node.isMissingNode()) {
                    value = null;
                }
            }
            return Optional.ofNullable(value);
        }

        @Override
        public Object getEmptyValue(DeserializationContext context) throws JsonMappingException {
            return getNullValue(context);
        }

        @Override
        public Optional<?> referenceValue(Object contents) {
            return Optional.ofNullable(contents);
        }

        @Override
        public Object getReferenced(Optional<?> reference) {
            return reference.get();
        }

        @Override
        public Optional<?> updateReference(Optional<?> reference, Object contents) {
            return Optional.ofNullable(contents);
        }

    }
}
