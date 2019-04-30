package com.avantika.alumni.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceClient {
    public static EndPoints getRetroFit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints service = retrofit.create(EndPoints.class);
        return service;
    }
}
