package com.thirdspare.thirdsparemain.utilities;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext context) {
        if (localDateTime == null) {
            return JsonNull.INSTANCE;
        }
        return new JsonPrimitive(localDateTime.format(FORMATTER));
    }

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) 
            throws JsonParseException {
        if (jsonElement.isJsonNull()) {
            return null;
        }
        
        try {
            return LocalDateTime.parse(jsonElement.getAsString(), FORMATTER);
        } catch (Exception e) {
            throw new JsonParseException("Failed to parse LocalDateTime: " + jsonElement.getAsString(), e);
        }
    }
}