package org.jabref.logic.util.io;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

public class GsonUtil {

    public static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXX";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static Gson gson;
    private static Gson gsonExpose;

    public static Gson getInstance() {
        if (gson == null) {
            gson = getGsonBuilderInstance(false).create();
        }
        return gson;
    }

    public static Gson getExposeInstance() {
        if (gsonExpose == null) {
            gsonExpose = getGsonBuilderInstance(true).create();
        }
        return gsonExpose;
    }

    public static Gson getInstance(boolean onlyExpose) {
        if (!onlyExpose) {
            if (gson == null) {
                gson = getGsonBuilderInstance(false).create();
            }
            return gson;
        } else {
            if (gsonExpose == null) {
                gsonExpose = getGsonBuilderInstance(true).create();
            }
            return gsonExpose;
        }
    }

    private static GsonBuilder getGsonBuilderInstance(boolean onlyExpose) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (onlyExpose) {
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        }
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (tojson, type, jsonSerializationContext) -> {
            return new JsonPrimitive(tojson.format(formatter));
        });
        gsonBuilder.enableComplexMapKeySerialization();

        return gsonBuilder;
    }

    public static <T> T fromJson(String json, Class<T> classOfT,
            boolean onlyExpose) {
        try {
            return getInstance(onlyExpose).fromJson(json, classOfT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
