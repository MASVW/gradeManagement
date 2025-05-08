package com.smp5.app

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.smp5.app.model.APIRetrofit
import com.smp5.app.model.SubmitModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateSubjectActivity : ComponentActivity() {

    private val api by lazy { APIRetrofit().endpoint }
    private val TAG = "Create Subject Function";

    private lateinit var subjectName: EditText
    private lateinit var subjectDesc: EditText
    private lateinit var buttonCreate: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_subject)
        setupView();
        setupListener();
    }

    private fun setupView(){
        subjectName = findViewById(R.id.subject_name)
        subjectDesc = findViewById(R.id.subject_description)
        buttonCreate = findViewById(R.id.buttonCreate)
    }

    private fun setupListener(){
        buttonCreate.setOnClickListener{
            val name = subjectName.text.toString().trim()
            val subjDesc = subjectDesc.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Nama Subjek tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (subjDesc.isEmpty()) {
                Toast.makeText(this, "Deskripsi subjek tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d(TAG, "Subject: $name")
            Log.d(TAG, "Description: $subjDesc")

            api.createSubject(name, subjDesc).enqueue(object : Callback<SubmitModel> {
                override fun onResponse(
                    call: Call<SubmitModel>,
                    response: Response<SubmitModel>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreateSubjectActivity, "Siswa berhasil dibuat", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@CreateSubjectActivity, "Gagal membuat siswa", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SubmitModel>, t: Throwable) {
                    Log.e(TAG, "API call failed: ${t.message}")
                    Toast.makeText(this@CreateSubjectActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}