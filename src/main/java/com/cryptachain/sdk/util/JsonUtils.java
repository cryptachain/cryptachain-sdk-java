package com.cryptachain.sdk.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Utility class providing a pre-configured Jackson {@link ObjectMapper}.
 *
 * <p>The mapper is configured with:</p>
 * <ul>
 *   <li>Snake-case naming strategy (matching the CryptaChain API convention)</li>
 *   <li>Java 8 date/time support via {@link JavaTimeModule}</li>
 *   <li>Ignoring unknown properties for forward compatibility</li>
 *   <li>Non-null inclusion for serialization</li>
 * </ul>
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER = createMapper();

    private JsonUtils() {}

    /**
     * Returns the shared, pre-configured ObjectMapper instance.
     *
     * @return the ObjectMapper
     */
    public static ObjectMapper mapper() {
        return MAPPER;
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
