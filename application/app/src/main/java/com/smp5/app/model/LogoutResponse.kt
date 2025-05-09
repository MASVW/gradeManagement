package com.smp5.app.model

import android.telecom.Call.Details

data class LogoutResponse (
    val details: Details?,
    val error: String?,
    val message: String?,
)
