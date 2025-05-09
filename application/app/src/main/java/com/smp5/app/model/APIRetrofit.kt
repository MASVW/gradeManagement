package com.smp5.app.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIRetrofit {

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/api/v1/"
        private var token: String? = null

        fun setToken(newToken: String) {
            token = newToken
        }

        fun clearToken() {
            token = null
        }
    }

    val endpoint: APIEndpoint
        get() {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
                addInterceptor { chain ->
                    val originalRequest = chain.request()
                    token?.let {
                        val newRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer $it")
                            .build()
                        return@addInterceptor chain.proceed(newRequest)
                    }
                    chain.proceed(originalRequest)
                }
            }.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIEndpoint::class.java)
        }
}