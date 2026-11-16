package com.unicartagena.apppaises.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Response;

/** Extrae el mensaje {"error": "..."} que devuelve el backend. */
public class ApiError {
    public static String from(Response<?> response, String porDefecto) {
        try {
            if (response.errorBody() != null) {
                JsonObject obj = JsonParser.parseString(response.errorBody().string()).getAsJsonObject();
                if (obj.has("error")) return obj.get("error").getAsString();
            }
        } catch (Exception ignored) { }
        return porDefecto + " (código " + response.code() + ")";
    }
}
