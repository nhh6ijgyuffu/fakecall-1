package com.alex.fakecall.utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Reader;
import java.lang.reflect.Type;

public abstract class GsonUtils {

    private static final Gson GSON = createGson(true);

    private static final Gson GSON_NO_NULLS = createGson(false);

    /**
     * Create the standard {@link Gson} configuration
     *
     * @return created gson, never null
     */
    public static Gson createGson() {
        return createGson(true);
    }

    /**
     * Create the standard {@link Gson} configuration
     *
     * @param serializeNulls whether nulls should be serialized
     * @return created gson, never null
     */
    public static Gson createGson(final boolean serializeNulls) {
        final GsonBuilder builder = new GsonBuilder();
        if (serializeNulls)
            builder.serializeNulls();
        return builder.create();
    }

    /**
     * Get reusable pre-configured {@link Gson} instance
     *
     * @return Gson instance
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * Get reusable pre-configured {@link Gson} instance
     *
     * @param serializeNulls serialize null?
     * @return Gson instance
     */
    public static Gson getGson(final boolean serializeNulls) {
        return serializeNulls ? GSON : GSON_NO_NULLS;
    }

    /**
     * Convert object to json
     *
     * @param object object to serialize
     * @return json string
     */
    public static String toJson(final Object object) {
        return toJson(object, true);
    }

    /**
     * Convert object to json
     *
     * @param object       object to serialize
     * @param includeNulls serialize null?
     * @return json string
     */
    public static String toJson(final Object object,
                                final boolean includeNulls) {
        return includeNulls ? GSON.toJson(object) : GSON_NO_NULLS
                .toJson(object);
    }

    /**
     * Convert Object to JSONObject
     */
    public static JSONObject toJSon(final Object object) throws JSONException {
        String jsonStr = GsonUtils.toJson(object);
        return new JSONObject(jsonStr);
    }

    /**
     * Convert string to given type
     *
     * @param json json in string format
     * @param type type of wrapper class
     * @return instance of type
     */
    public static <V> V fromJson(String json, Class<V> type) {
        return GSON.fromJson(json, type);
    }

    /**
     * Convert string to given type
     *
     * @param json json in string format
     * @param type type of wrapper class
     * @return instance of type
     */
    public static <V> V fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    /**
     * Convert content of reader to given type
     *
     * @param reader a reader
     * @param type   type of wrapper class
     * @return instance of type
     */
    public static <V> V fromJson(Reader reader, Class<V> type) {
        return GSON.fromJson(reader, type);
    }

    /**
     * Convert content of reader to given type
     *
     * @param reader a reader
     * @param type   type of wrapper class
     * @return instance of type
     */
    public static <V> V fromJson(Reader reader, Type type) {
        return GSON.fromJson(reader, type);
    }

    /**
     * Convert json element to given type
     *
     * @param element json element
     * @param type    type of wrapper class
     * @return instance of type
     */
    public static <V> V fromJson(JsonElement element, Class<V> type) {
        return GSON.fromJson(element, type);
    }

    /**
     * Convert content of reader to given type
     *
     * @param element json element
     * @param type    type of wrapper class
     * @return instance of type
     */
    public static <V> V fromJson(JsonElement element, Type type) {
        return GSON.fromJson(element, type);
    }
}