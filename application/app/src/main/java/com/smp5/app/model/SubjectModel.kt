package com.smp5.app.model

import java.io.Serializable

data class SubjectModel(
    val id: Int,
    val name: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String
): Serializable
