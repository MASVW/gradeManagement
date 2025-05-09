package com.smp5.app.model

import android.content.Context
import android.content.Intent
import com.smp5.app.AuthPrefs
import com.smp5.app.ui.LoginActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIRetrofit(private val context: Context) {

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/api/v1/"
    }

    val endpoint: APIEndpoint
        get() {
            val authPrefs = AuthPrefs(context)
            val token = authPrefs.getToken() // Ambil token langsung dari AuthPrefs

            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
                addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val requestBuilder = originalRequest.newBuilder()

                    // Menambahkan token ke header Authorization jika ada
                    token?.let {
                        requestBuilder.header("Authorization", "Bearer $it")
                    }

                    val response = chain.proceed(requestBuilder.build())

                    // Cek apakah token expired, jika 401 maka logout
                    if (response.code == 401) {
                        authPrefs.clearToken() // Clear token jika expired
                        // Redirect ke LoginActivity kalau token sudah expired
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }

                    response
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
