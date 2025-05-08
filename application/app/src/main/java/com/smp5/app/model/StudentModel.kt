package com.smp5.app.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StudentModel(
    val id: Int,
    val name: String,
    val age: Int,
    @SerializedName("class") val className: String,
    val createdAt: String,
    val updatedAt: String
): Serializable

