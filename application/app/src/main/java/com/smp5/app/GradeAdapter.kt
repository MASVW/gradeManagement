package com.smp5.app

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smp5.app.model.GradeModel

class GradeAdapter (
    private val context: Context,
    private val grades: ArrayList<GradeModel>,
    private val listener: OnAdapterListener
): RecyclerView.Adapter<GradeAdapter.ViewHolder>(){
    private val TAG = " Grade Adaptor"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_grade, parent, false)
    )

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val studentName = view.findViewById<TextView>(R.id.student_name)
        val className = view.findViewById<TextView>(R.id.student_class)
        val subjectName = view.findViewById<TextView>(R.id.subject_name)
        val subjectDesc = view.findViewById<TextView>(R.id.subject_description)
        val score = view.findViewById<TextView>(R.id.score)
        val imageDelete = view.findViewById<ImageView>(R.id.imageDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grade = grades[position]
        holder.studentName.text = grade.student.name.toString()

        val ccWarning = "Silahkan Kaitkan Dengan Mata Pelajaran"
        if (grade.subject != null) {
            holder.subjectName.text = grade.subject?.name ?: ccWarning
            holder.subjectDesc.text = grade.subject?.description ?: ccWarning
        } else {
            holder.subjectName.text = ccWarning
            holder.subjectDesc.text = ccWarning
        }
        val nilai: String

        if (holder.score != null){
             nilai = "Nilai ${grade.score.toString()}"
        }else {
            nilai = "Nilai belum dimasukkan"
        }

        holder.className.text = "Kelas: ${grade.student.className}"
        holder.score.text = nilai

        holder.itemView.setOnClickListener{
            listener.onClick( grade )
        }

        holder.imageDelete.setOnClickListener{
            listener.onDelete( grade )
        }
    }

    override fun getItemCount(): Int {
        val count = grades.size
        return count
    }

    public fun setData(data: List<GradeModel>){
        grades.clear()
        grades.addAll(data)
        Log.d(TAG, "Data berhasil diambil: ${grades}")
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        grades.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, grades.size)
    }

    interface OnAdapterListener{
        fun onClick(grade: GradeModel)
        fun onDelete(grade: GradeModel)
    }

}