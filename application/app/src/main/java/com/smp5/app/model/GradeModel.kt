package com.smp5.app.model

data class GradeModel(
    val id: Int,
    val studentId: Int,
    val subjectId: Int,
    val score: Double,
    val createdAt: String,
    val updatedAt: String,
    val student: StudentModel,
    val subject: SubjectModel
)