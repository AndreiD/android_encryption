package com.androidadvance.encryptedapi.api;

import com.androidadvance.encryptedapi.models.User;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;


public interface MyApi {


    @GET("/json") Call<User>
    getterStuf(@Query("a_parameter") String a_parameter);

    @POST("/post_to_server") Call<Object>
    postToServer(@Body HashMap<String, Object> body);


    @Multipart
    @POST("/pusher")
    void pusherPicture(@Part("file") RequestBody file, @Part("file_xname") String file_xname, @Part("from_email") String from_email, Callback<Object> response);

}