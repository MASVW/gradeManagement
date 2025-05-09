package com.smp5.app

import android.content.Context
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
import com.smp5.app.model.GradeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradesFragment : Fragment() {

    private val TAG = "Grades Fragment"

    private lateinit var gradeAdapter: GradeAdapter
    private lateinit var listGrade: RecyclerView

    private val api by lazy { APIRetrofit(requireContext()).endpoint }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grades, container, false)
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
        listGrade = view.findViewById(R.id.recyclerGrade)
    }

    private fun setupAdapter() {
        gradeAdapter = GradeAdapter(requireContext(), arrayListOf(), object : GradeAdapter.OnAdapterListener {
            override fun onClick(grade: GradeModel) {
                startActivity(
                    Intent(requireContext(), EditGradeActivity::class.java)
                        .putExtra("data", grade)
                )
            }

            override fun onDelete(grade: GradeModel) {
                (activity as MainActivity).showDeleteGradeConfirmationDialog(grade)
            }
        })
        listGrade.adapter = gradeAdapter
    }

    fun refreshData() {
        lifecycleScope.launch {
            try {
                val listData = withContext(Dispatchers.IO) {
                    val response = api.getGrades().execute()
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        null
                    }
                }

                if (listData != null) {
                    Log.d(TAG, "Data berhasil diambil: ${listData.size} item")
                    gradeAdapter.setData(listData)
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