package com.example.miniapppointsofinterest.api;

import com.example.miniapppointsofinterest.model.user.NewUserBoundary;
import com.example.miniapppointsofinterest.model.user.UserBoundary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {

    @POST("superapp/users")
    Call<UserBoundary> createUser(@Body NewUserBoundary newUserBoundary);

    @GET("superapp/users/login/{superapp}/{email}")
    Call<UserBoundary> login(@Path("superapp") String superapp, @Path("email") String email);

    @PUT("superapp/users/{superapp}/{email}")
    //TODO: delete if not use
    void updateUser(@Path("superapp") String superapp, @Path("email") String email, @Body UserBoundary userBoundary);
}