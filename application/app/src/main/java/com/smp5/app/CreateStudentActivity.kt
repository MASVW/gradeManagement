package com.smp5.app

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class CreateStudentActivity : AppCompatActivity() {
    private val TAG = "Create Function";

    private lateinit var studentName: EditText
    private lateinit var studentAge: EditText
    private lateinit var studentClass: EditText
    private lateinit var buttonCreate: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_student)
        setupView();
        setupListener();
    }


    private fun setupView(){
        studentName = findViewById(R.id.studentName)
        studentAge = findViewById(R.id.studentAge)
        studentClass = findViewById(R.id.studentClass)
        buttonCreate = findViewById(R.id.buttonCreate)
    }
    private fun setupListener(){
        buttonCreate.setOnClickListener{
            Log.d(TAG, "Data berhasil diambil: ${studentName.text.toString()} ${studentAge.text.toString()} ${studentClass.text.toString()}")
        }
    }

}