package com.smp5.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smp5.app.ExportItem

class ExportAdapter(
    private val items: List<ExportItem>,
    private val onDownloadClick: (ExportItem) -> Unit
) : RecyclerView.Adapter<ExportAdapter.ExportViewHolder>() {

    class ExportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val btnDownload: Button = itemView.findViewById(R.id.btnDownload)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_export, parent, false)
        return ExportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExportViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.title
        holder.btnDownload.setOnClickListener {
            onDownloadClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
