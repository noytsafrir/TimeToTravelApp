package com.example.miniapppointsofinterest.api;

import com.example.miniapppointsofinterest.model.miniappCommand.MiniAppCommandBoundary;
import com.example.miniapppointsofinterest.model.object.ObjectBoundary;
import com.example.miniapppointsofinterest.model.user.NewUserBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MiniAppCommandApi {

    @POST("/superapp/miniapp/{miniAppName}?async={asyncFlag}")
    Call<Object> invokeCommand(@Path("miniAppName") String miniAppName,
                                               @Path("asyncFlag") boolean asyncFlag,
                                               @Body MiniAppCommandBoundary miniAppCommandBoundary);
}