package org.jabref.logic.importer.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.JSONObject;

/**
 * Converts an {@link InputStream} into a {@link JSONObject}.
 */
public class JsonReader {

    private static JsonParser parser = new JsonParser();

    public static JsonObject toJsonObject(InputStreamReader input) {
        return parser.parse(input).getAsJsonObject();
    }

    public static JsonArray toJsonArray(InputStreamReader input) {
        return parser.parse(input).getAsJsonArray();
    }

    public static JsonObject toJsonObject(InputStream input) {
        return toJsonObject(new InputStreamReader(input, StandardCharsets.UTF_8));
    }

    public static JsonArray toJsonArray(InputStream input) {
        return toJsonArray(new InputStreamReader(input, StandardCharsets.UTF_8));

    }
}
