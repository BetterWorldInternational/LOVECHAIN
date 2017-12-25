package org.betterworldinternational.hugapi.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

public class JsonUtil {
    public static Gson gson;

    static {
        GsonBuilder gb = new GsonBuilder();
        gson = gb.create();
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }

    public static boolean isValidJson(String json) {
        try {
            Object object = gson.fromJson(json, Object.class);
            return object != null;
        } catch (Exception ex) {
            return false;
        }
    }

    private static String toJson(Object object) {
        return gson.toJson(object);
    }
}