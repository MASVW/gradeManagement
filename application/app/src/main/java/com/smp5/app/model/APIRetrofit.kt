package com.smp5.app.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIRetrofit {

    val endpoint: APIEndpoint
        get(){
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(APIEndpoint::class.java)
        }
}