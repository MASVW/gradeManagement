package com.smp5.app

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.smp5.app.model.APIRetrofit
import com.smp5.app.model.StudentModel
import com.smp5.app.model.SubmitModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditStudentActivity : ComponentActivity() {

    private val api by lazy { APIRetrofit(this).endpoint }
    private val data by lazy { intent.getSerializableExtra("data") as StudentModel }
    private val TAG = "Edit Student Function";

    private lateinit var studentName: EditText
    private lateinit var studentAge: EditText
    private lateinit var studentClass: EditText
    private lateinit var buttonUpdate: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)
        setupView();
        setupListener();
    }

    private fun setupView(){
        studentName = findViewById(R.id.studentName)
        studentAge = findViewById(R.id.studentAge)
        studentClass = findViewById(R.id.studentClass)
        buttonUpdate = findViewById(R.id.buttonUpdate)

        studentName.setText( data.name )
        studentAge.setText( data.age.toString() )
        studentClass.setText( data.className )
    }

    private fun setupListener() {
        buttonUpdate.setOnClickListener {
            val id = data.id
            val name = studentName.text.toString().trim()
            val ageStr = studentAge.text.toString().trim()
            val studentClassStr = studentClass.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Nama Siswa tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (ageStr.isEmpty()) {
                Toast.makeText(this, "Umur tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (studentClassStr.isEmpty()) {
                Toast.makeText(this, "Kelas tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = try {
                ageStr.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Umur harus berupa angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d(TAG, "Nama: $name")
            Log.d(TAG, "Umur: $age")
            Log.d(TAG, "Kelas: $studentClassStr")

            api.updateStudent(id, name, age, studentClassStr).enqueue(object : Callback<SubmitModel> {
                override fun onResponse(
                    call: Call<SubmitModel>,
                    response: Response<SubmitModel>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EditStudentActivity,
                            "Siswa berhasil diupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditStudentActivity,
                            "Gagal mengupdate data siswa",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SubmitModel>, t: Throwable) {
                    Log.e(TAG, "API call failed: ${t.message}")
                    Toast.makeText(
                        this@EditStudentActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}