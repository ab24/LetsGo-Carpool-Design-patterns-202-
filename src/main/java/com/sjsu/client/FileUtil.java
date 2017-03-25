package com.sjsu.client;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.sjsu.member.Driver;
import com.sjsu.member.Rider;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileUtil {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

    private static Map<Class, String> classToFileName = new HashMap<>();

    static {
        classToFileName.put(Rider.class, "RiderDetails");
        classToFileName.put(Driver.class, "DriverDetails");
    }

    public static <T> List<T> fromFile(Class<T> classType) throws FileNotFoundException {
        String fileName = getFileName(classType);

        File jsonFile = new File("./datafiles/" + fileName + ".json");
        FileReader dataFileReader = new FileReader(jsonFile);

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(dataFileReader).getAsJsonArray();

        List<T> toReturn = Lists.newArrayList();
        for (JsonElement element : jsonArray) {
            T fromJson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter()).create().fromJson(element, classType);
            toReturn.add(fromJson);
        }

        return toReturn;
    }

    private static <T> String getFileName(Class<T> classType) {
        if (classToFileName.containsKey(classType)) {
            return classToFileName.get(classType);
        }
        return classType.getSimpleName();
    }

    public static <T> void toFile(T toWrite) throws IOException {
        FileWriter fileWriter = new FileWriter("../datafiles/" + getFileName(toWrite.getClass()) + ".json");
        gson.toJson(toWrite, fileWriter);
        fileWriter.close();
    }

    static class DateTimeTypeAdapter implements JsonSerializer<DateTime>,
            JsonDeserializer<DateTime> {
        @Override
        public DateTime deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
            return DateTime.parse(json.getAsString());
        }

        @Override
        public JsonElement serialize(DateTime src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(ISODateTimeFormat
                    .dateTimeNoMillis()
                    .print(src));
        }
    }


}






