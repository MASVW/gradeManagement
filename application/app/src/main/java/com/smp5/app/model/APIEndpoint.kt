package com.smp5.app.model

import retrofit2.Call
import retrofit2.http.GET

interface APIEndpoint {

    @GET("students")
    fun getStudents(): Call<List<StudentModel>>
}