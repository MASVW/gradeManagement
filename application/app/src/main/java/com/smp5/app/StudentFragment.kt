package com.smp5.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.smp5.app.model.APIRetrofit
import com.smp5.app.model.StudentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentFragment : Fragment() {
    private val TAG = "Student Fragment"

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var listStudent: RecyclerView

    private val api by lazy { APIRetrofit(requireContext()).endpoint }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        setupAdapter()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun setupRecyclerView(view: View) {
        listStudent = view.findViewById(R.id.recyclerStudent)
    }

    private fun setupAdapter() {
        studentAdapter = StudentAdapter(requireContext(), arrayListOf(), object : StudentAdapter.OnAdapterlistener {
            override fun onClick(student: StudentModel) {
                startActivity(
                    Intent(requireContext(), EditStudentActivity::class.java)
                        .putExtra("data", student)
                )
            }

            override fun onDelete(student: StudentModel) {
                (activity as MainActivity).showDeleteStudentConfirmationDialog(student)
            }
        })
        listStudent.adapter = studentAdapter
    }

    fun refreshData() {
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
                    Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Terjadi kesalahan: ${e.message}", e)
                Toast.makeText(requireContext(), "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}