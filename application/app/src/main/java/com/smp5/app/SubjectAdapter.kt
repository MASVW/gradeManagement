package com.smp5.app

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smp5.app.model.SubjectModel

class SubjectAdapter (
    private val context: Context,
    private val subject: ArrayList<SubjectModel>,
    private val listener: OnAdapterlistener
): RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {
    private val TAG = "Subject Adapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_subject, parent, false)
    )

    override fun onBindViewHolder(holder: SubjectAdapter.ViewHolder, position: Int) {
        val subject = subject[position]
        holder.subject_name.text = subject.name.toString()
        holder.subject_description.text = subject.description.toString()
        holder.itemView.setOnClickListener{
            listener.onClick( subject )
        }

        holder.imageDelete.setOnClickListener {
            listener.onDelete( subject )
        }
    }

    override fun getItemCount() = subject.size


    public class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val subject_name = view.findViewById<TextView>(R.id.subject_name)
        val subject_description = view.findViewById<TextView>(R.id.subject_descripton)
        val imageDelete = view.findViewById<ImageView>(R.id.imageDelete)
    }

    public fun setData(data: List<SubjectModel>){
        subject.clear()
        subject.addAll(data)
        Log.d(TAG, "Data berhasil diambil: ${subject}")
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        subject.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, subject.size)
    }

    interface OnAdapterlistener{
        fun onClick(subject: SubjectModel)
        fun onDelete(subject: SubjectModel)
    }

}