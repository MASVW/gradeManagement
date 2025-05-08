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

class EditGradeActivity : ComponentActivity() {

    private val api by lazy { APIRetrofit().endpoint }
    private val data by lazy { intent.getSerializableExtra("data") as GradeModel }
    private val TAG = "Edit Grade Function";

    private lateinit var spinnerStudent: Spinner
    private lateinit var radioGroupSubject: RadioGroup
    private lateinit var editTextScore: TextInputEditText
    private lateinit var buttonSubmit: Button

    private var students = listOf<StudentModel>()
    private var subjects = listOf<SubjectModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_grade)

        setupView()
        fetchStudents()
        fetchSubjects()

        editTextScore.setText(data.score.toString())

        buttonSubmit.setOnClickListener {
            updateGrade()
        }
    }

    private fun setupView(){
        spinnerStudent = findViewById(R.id.spinner_student)
        radioGroupSubject = findViewById(R.id.radioGroup_subject)
        editTextScore = findViewById(R.id.editText_score)
        buttonSubmit = findViewById(R.id.button_submit)
    }

    private fun fetchSubjects() {
        lifecycleScope.launch {
            try {
                Log.d(TAG, "Fetching subjects...")
                val response = withContext(Dispatchers.IO) { api.getSubject().execute() }
                if (response.isSuccessful && response.body() != null) {
                    subjects = response.body()!!
                    Log.d(TAG, "Subjects fetched: ${subjects.size}")

                    subjects.forEach {
                        Log.d(TAG, "Subject: ${it.name}, ID: ${it.id}")
                    }

                    populateSubjectRadioButtons()
                } else {
                    Log.e(TAG, "Failed to load subjects: ${response.code()} - ${response.message()}")
                    showToast("Failed to load subjects: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching subjects", e)
                showToast("Error: ${e.message}")
            }
        }
    }


    private fun fetchStudents() {
        lifecycleScope.launch {
            try {
                Log.d(TAG, "Fetching students...")
                val response = withContext(Dispatchers.IO) { api.getStudents().execute() }
                if (response.isSuccessful && response.body() != null) {
                    students = response.body()!!
                    Log.d(TAG, "Students fetched: ${students.size}")

                    // Log semua ID siswa untuk debugging
                    students.forEach {
                        Log.d(TAG, "Student: ${it.name}, ID: ${it.id}")
                    }

                    val studentNames = students.map { it.name }
                    val adapter = ArrayAdapter(this@EditGradeActivity, android.R.layout.simple_spinner_item, studentNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerStudent.adapter = adapter

                    val selectedStudentPosition = students.indexOfFirst { it.id == data.studentId }
                    if (selectedStudentPosition != -1) {
                        Log.d(TAG, "Setting student selection to: ${students[selectedStudentPosition].name}")
                        spinnerStudent.setSelection(selectedStudentPosition)
                    } else {
                        Log.e(TAG, "Could not find student with ID: ${data.studentId}")
                    }
                } else {
                    Log.e(TAG, "Failed to load students: ${response.code()} - ${response.message()}")
                    showToast("Failed to load students: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching students", e)
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun populateSubjectRadioButtons() {
        radioGroupSubject.removeAllViews()
        if (subjects.isEmpty()) {
            val warningRadioButton = RadioButton(this).apply {
                text = "Silahkan Kaitkan Dengan Mata Pelajaran"
                id = View.generateViewId()
                isEnabled = false
            }
            radioGroupSubject.addView(warningRadioButton)
            return
        }

        subjects.forEach { subject ->
            val radioButton = RadioButton(this).apply {
                text = subject.name
                id = View.generateViewId()
                tag = subject
            }
            radioGroupSubject.addView(radioButton)
        }

        val selectedIndex = subjects.indexOfFirst { it.id == data.subjectId }
        if (selectedIndex != -1) {
            val radioButton = radioGroupSubject.getChildAt(selectedIndex) as? RadioButton
            radioButton?.isChecked = true
        }
    }

    private fun updateGrade() {
        val studentPosition = spinnerStudent.selectedItemPosition
        if (studentPosition == AdapterView.INVALID_POSITION) {
            showToast("Please select a student")
            return
        }
        val selectedStudent = students[studentPosition]
        val studentId = selectedStudent.id

        val selectedRadioButtonId = radioGroupSubject.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val selectedSubject = selectedRadioButton?.tag as? SubjectModel

        if (selectedSubject == null) {
            showToast("Please select a subject")
            return
        }

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
                // Convert ID ke String untuk memastikan format yang konsisten
                val gradeIdString = data.id.toString()
                val studentIdString = studentId.toString()
                val subjectIdString = subjectId.toString()

                Log.d(TAG, "Updating grade - ID: $gradeIdString, StudentID: $studentIdString, SubjectID: $subjectIdString, Score: $score")

                val response = withContext(Dispatchers.IO) {
                    // Kirim ID sebagai String
                    api.updateGrades(gradeIdString, studentIdString, subjectIdString, score).execute()
                }

                if (response.isSuccessful) {
                    Log.d(TAG, "Grade updated successfully")
                    showToast("Grade updated successfully")
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e(TAG, "Failed to update grade: ${response.code()} - $errorBody")
                    showToast("Failed to update grade: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating grade", e)
                showToast("Error: ${e.message}")
            } finally {
                buttonSubmit.isEnabled = true
                buttonSubmit.text = "Update"
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
