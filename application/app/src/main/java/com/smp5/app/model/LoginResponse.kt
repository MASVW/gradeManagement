package com.smp5.app.model

data class LoginResponse(
    val success: Boolean?,
    val token: String?,
    val message: String?,
    val error: String?
)