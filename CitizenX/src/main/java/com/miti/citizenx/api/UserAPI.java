package com.miti.citizenx.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miti.citizenx.model.UserEntity;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.http.*;
import retrofit.mime.TypedFile;

/**
 * mrfreitas
 * Date: 22/07/2015
 * Time: 13:30
 */
public interface UserAPI
{
    @Multipart
    @POST("/user/register")
    void register(@Part("userImage") TypedFile photo,
                  @Part("userData") String user,
                  Callback<JsonObject> response);

    @POST("/user/login")
    void login(@Body JsonObject data, Callback<JsonObject> response);


}
