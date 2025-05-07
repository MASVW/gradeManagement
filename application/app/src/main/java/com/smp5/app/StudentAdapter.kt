package com.smp5.app

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.smp5.app.model.StudentModel

class StudentAdapter(
    private val context: Context,
    private val students: MutableList<StudentModel>
) : BaseAdapter() {
    private val TAG = "Student Adapter"
    fun setData(newData: List<StudentModel>) {
        students.clear()
        students.addAll(newData)
        Log.d(TAG, "Data berhasil diambil: ${students} item")
        notifyDataSetChanged()
    }

    override fun getCount(): Int = students.size

    override fun getItem(position: Int): Any = students[position]

    override fun getItemId(position: Int): Long = students[position].id.toLong()

    // Method untuk mengatur tampilan setiap item di ListView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.adapter_student, parent, false)
        val student = students[position]
        val textStudent = view.findViewById<TextView>(R.id.textStudent)
        textStudent.text = student.name
        return view
    }
}
