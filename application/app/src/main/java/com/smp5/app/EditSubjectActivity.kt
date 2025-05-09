package com.smp5.app

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.smp5.app.model.APIRetrofit
import com.smp5.app.model.SubjectModel
import com.smp5.app.model.SubmitModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditSubjectActivity : ComponentActivity() {

    private val api by lazy { APIRetrofit(this).endpoint }
    private val data by lazy { intent.getSerializableExtra("data") as SubjectModel }
    private val TAG = "Edit Function";

    private lateinit var subjectName: EditText
    private lateinit var subjectDescription: EditText
    private lateinit var buttonUpdate: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_subject)
        setupView();
        setupListener();
    }

    private fun setupView(){
        subjectName = findViewById(R.id.subject_name)
        subjectDescription = findViewById(R.id.subject_description)
        buttonUpdate = findViewById(R.id.buttonUpdate)

        subjectName.setText( data.name )
        subjectDescription.setText( data.description )
    }

    private fun setupListener() {
        buttonUpdate.setOnClickListener {
            val id = data.id
            val name = subjectName.text.toString().trim()
            val subjDesc = subjectDescription.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Nama subjek tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (subjDesc.isEmpty()) {
                Toast.makeText(this, "Deskripsi subjek tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d(TAG, "Subject Name: $name")
            Log.d(TAG, "Decription: $subjDesc")

            api.updateSubject(id, name, subjDesc).enqueue(object : Callback<SubmitModel> {
                override fun onResponse(
                    call: Call<SubmitModel>,
                    response: Response<SubmitModel>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EditSubjectActivity,
                            "Subjek berhasil diupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditSubjectActivity,
                            "Gagal mengupdate data subjek",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SubmitModel>, t: Throwable) {
                    Log.e(TAG, "API call failed: ${t.message}")
                    Toast.makeText(
                        this@EditSubjectActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}