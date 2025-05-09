package com.smp5.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.smp5.app.model.APIRetrofit
import com.smp5.app.model.GradeModel
import com.smp5.app.model.StudentModel
import com.smp5.app.model.SubjectModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateGradeActivity : ComponentActivity() {

    private lateinit var spinnerStudent: Spinner
    private lateinit var radioGroupSubject: RadioGroup
    private lateinit var editTextScore: TextInputEditText
    private lateinit var buttonSubmit: Button

    private val api by lazy { APIRetrofit(this).endpoint }

    private var students = listOf<StudentModel>()
    private var subjects = listOf<SubjectModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_grade)

        spinnerStudent = findViewById(R.id.spinner_student)
        radioGroupSubject = findViewById(R.id.radioGroup_subject)
        editTextScore = findViewById(R.id.editText_score)
        buttonSubmit = findViewById(R.id.button_submit)

        fetchStudents()
        fetchSubjects()

        buttonSubmit.setOnClickListener {
            submitGrade()
        }
    }

    private fun fetchStudents() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { api.getStudents().execute() }
                if (response.isSuccessful) {
                    val studentList = response.body()
                    if (!studentList.isNullOrEmpty()) {
                        students = studentList
                        val studentNames = students.map { it.name }
                        val adapter = ArrayAdapter(this@CreateGradeActivity, android.R.layout.simple_spinner_item, studentNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Update UI on Main thread
                        withContext(Dispatchers.Main) {
                            spinnerStudent.adapter = adapter
                        }
                    } else {
                        showToast("No students found")
                        Log.e("CreateGradeActivity", "Student list is empty or null")
                    }
                } else {
                    showToast("Failed to load students: ${response.code()}")
                    Log.e("Create Grade Activity", "Response unsuccessful: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.localizedMessage}")
                Log.e("CreateGradeActivity", "Exception occurred", e)
            }
        }
    }


    private fun fetchSubjects() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { api.getSubject().execute() }
                if (response.isSuccessful && response.body() != null) {
                    subjects = response.body()!!
                    populateSubjectRadioButtons()
                } else {
                    showToast("Failed to load subjects")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun populateSubjectRadioButtons() {
        radioGroupSubject.removeAllViews()
        subjects.forEach { subject ->
            val radioButton = RadioButton(this).apply {
                text = subject.name
                id = View.generateViewId()
                tag = subject
            }
            radioGroupSubject.addView(radioButton)
        }
    }

    private fun submitGrade() {
        val studentPosition = spinnerStudent.selectedItemPosition
        if (studentPosition == AdapterView.INVALID_POSITION) {
            showToast("Please select a student")
            return
        }
        val selectedStudent = students[studentPosition]

        val studentId = selectedStudent.id

        val selectedRadioButtonId = radioGroupSubject.checkedRadioButtonId
        if (selectedRadioButtonId == -1) {
            showToast("Please select a subject")
            return
        }
        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val selectedSubject = selectedRadioButton.tag as SubjectModel

        val subjectId = selectedSubject.id

        val scoreText = editTextScore.text.toString()
        if (scoreText.isEmpty()) {
            showToast("Please enter a score")
            return
        }
        val score = scoreText.toIntOrNull()
        if (score == null || score < 0 || score > 100) {
            showToast("Please enter a valid score (0-100)")
            return
        }


        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { api.createGrade(studentId, subjectId, score).execute() }
                if (response.isSuccessful) {
                    showToast("Grade submitted successfully")
                    finish()
                } else {
                    showToast("Failed to submit grade")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
