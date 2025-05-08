package com.smp5.app.model

import com.google.gson.annotations.SerializedName

data class SubmitModel (
    val id: Int,
    val name: String,
    val age: Int,
    @SerializedName("class") val className: String,
    val description: String,
    val message: String
)