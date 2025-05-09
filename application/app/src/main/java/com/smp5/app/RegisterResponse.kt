package com.smp5.app

import java.io.Serializable

data class RegisterResponse (
    var id: Int,
    var name: String,
    var username: String,
    var email: String,
    var password: String
): Serializable