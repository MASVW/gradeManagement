package com.smp5.app.model

import com.smp5.app.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

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
        @Path("id") id: String,
        @Field("studentId") studentId: String,
        @Field("subjectId") subjectId: String,
        @Field("score") score: Int
    ): Call<Any>

    @DELETE("grades/{id}")
    fun deleteGrades(
        @Path("id") id: Int
    ): Call<SubmitModel>

    // Login API

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    // REGISTER API

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    // LOGOUT API

    @POST("auth/logout")
    fun logout(
    ): Call<LogoutResponse>
}