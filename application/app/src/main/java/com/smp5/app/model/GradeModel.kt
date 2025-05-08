package com.smp5.app.model

import java.io.Serializable

data class GradeModel(
    val id: Int,
    val studentId: Int,
    val subjectId: Int?,
    val score: Int,
    val createdAt: String,
    val updatedAt: String,
    val student: StudentModel,
    val subject: SubjectModel
): Serializable