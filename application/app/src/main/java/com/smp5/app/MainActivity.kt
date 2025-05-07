package com.smp5.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smp5.app.model.APIRetrofit
import com.smp5.app.model.StudentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private val api by lazy { APIRetrofit().endpoint }

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var listStudent: RecyclerView
    private lateinit var fabCreate: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupList()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getStudent()
    }

    private fun setupView(){
        listStudent = findViewById(R.id.recyclerStudent)
        fabCreate = findViewById(R.id.createStudent)
    }

    private fun setupList() {
        studentAdapter = StudentAdapter(this, arrayListOf())
        listStudent.adapter = studentAdapter
    }

    private fun setupListener(){
        fabCreate.setOnClickListener{
            startActivity(Intent(this, CreateStudentActivity::class.java))
        }
    }


    private fun getStudent() {
        lifecycleScope.launch {
            try {
                val listData = withContext(Dispatchers.IO) {
                    val response = api.getStudents().execute()
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        null
                    }
                }

                if (listData != null) {
                    Log.d(TAG, "Data berhasil diambil: ${listData.size} item")
                    studentAdapter.setData(listData)
                } else {
                    Log.e(TAG, "Gagal mengambil data")
                    Toast.makeText(this@MainActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Terjadi kesalahan: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
