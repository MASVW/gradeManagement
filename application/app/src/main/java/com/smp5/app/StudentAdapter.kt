package com.smp5.app

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.smp5.app.model.StudentModel

class StudentAdapter(
    private val context: Context,
    private val students: ArrayList<StudentModel>
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
    private val TAG = "Student Adapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_student, parent, false)
    )

    override fun onBindViewHolder(holder: StudentAdapter.ViewHolder, position: Int) {
        val student = students[position]
        holder.textStudent.text = student.name
    }

    override fun getItemCount(): Int {
        val long = students.size
        return long
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textStudent = view.findViewById<TextView>(R.id.textStudent)
        val imageDelete = view.findViewById<ImageView>(R.id.imageDelete)
    }

    public fun setData(data: List<StudentModel>){
        students.clear()
        students.addAll(data)
        Log.d(TAG, "Data berhasil diambil: ${students}")
        notifyDataSetChanged()
    }
}
