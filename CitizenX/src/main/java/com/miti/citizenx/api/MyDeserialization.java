package com.miti.citizenx.api;

import com.google.gson.*;
import io.realm.RealmObject;

/**
 * mrfreitas
 * Date: 01/08/2015
 * Time: 22:27
 *
 * Because the bug of GSON 2.3.1 it's not possible to deserialize (normal way) REALM objects into JsonObjects and vice versa.
 * This class is a quick fix. But in next versions of the REALM or GSON check if the probelem as ben solved
 */
public class MyDeserialization
{
    public JsonObject deserializeToJasonObject(Object obj)
    {

        Gson gson = customGson();
        String u = gson.toJson(obj);
        JsonParser parser = new JsonParser();
        return (JsonObject)parser.parse(u);
    }

    public String deserializeToString(Object obj)
    {
        Gson gson = customGson();
        return gson.toJson(obj);
    }

    private Gson customGson()
    {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy()
        {
            @Override
            public boolean shouldSkipField(FieldAttributes f)
            {
                return f.getDeclaringClass().equals(RealmObject.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz)
            {
                return false;
            }
        })
                .create();

    }
}
