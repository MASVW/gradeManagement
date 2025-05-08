package com.smp5.app.model

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface APIEndpoint {

    @GET("students")
    fun getStudents(): Call<List<StudentModel>>

    @FormUrlEncoded
    @POST("students")
    fun createStudent(
        @Field("name") name: String,
        @Field("age") age: Int,
        @Field("class") `class`: String
    ): Call<SubmitModel>

    @FormUrlEncoded
    @PUT("students/{id}")
    fun updateStudent(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("age") age: Int,
        @Field("class") `class`: String
    ): Call<SubmitModel>

    @DELETE("students/{id}")
    fun deleteStudent(
        @Path("id") id: Int
    ): Call<SubmitModel>

    //SUBJECT API

    @GET("subject")
    fun getSubject(): Call<List<SubjectModel>>

    @FormUrlEncoded
    @POST("subject")
    fun createSubject(
        @Field("name") name: String,
        @Field("description") description: String,
    ): Call<SubmitModel>

    @FormUrlEncoded
    @PUT("subject/{id}")
    fun updateSubject(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("description") description: String,
    ): Call<SubmitModel>

    @DELETE("subject/{id}")
    fun deleteSubject(
        @Path("id") id: Int
    ): Call<SubmitModel>


    //GRADE API

    @GET("grades")
    fun getGrades(): Call<List<GradeModel>>

    @FormUrlEncoded
    @POST("grades")
    fun createGrade(
        @Field("studentId") studentId: Int,
        @Field("subjectId") subjectId: Int,
        @Field("score") score: Int,
    ): Call<SubmitModel>

    @FormUrlEncoded
    @PUT("grades/{id}")
    fun updateGrades(
        @Path("id") id: String,       // Ubah Any menjadi String
        @Field("studentId") studentId: String,  // Ubah Any menjadi String
        @Field("subjectId") subjectId: String,  // Ubah Any menjadi String
        @Field("score") score: Int
    ): Call<Any>

    @DELETE("grades/{id}")
    fun deleteGrades(
        @Path("id") id: Int
    ): Call<SubmitModel>
}