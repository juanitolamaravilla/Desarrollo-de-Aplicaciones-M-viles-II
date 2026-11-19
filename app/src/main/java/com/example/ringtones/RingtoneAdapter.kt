package com.example.ringtones

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RingtoneAdapter(
    private val items: List<RingtoneItem>,
    private val onPlayClick: (RingtoneItem) -> Unit,
    private val onDownloadClick: (RingtoneItem) -> Unit
) : RecyclerView.Adapter<RingtoneAdapter.RingtoneViewHolder>() {

    class RingtoneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnPlay: ImageButton = view.findViewById(R.id.btnPlay)
        val tvNombre: TextView = view.findViewById(R.id.tvNombreRingtone)
        val btnDownload: ImageButton = view.findViewById(R.id.btnDownload)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingtoneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ringtone, parent, false)
        return RingtoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: RingtoneViewHolder, position: Int) {
        val item = items[position]
        holder.tvNombre.text = item.titulo
        holder.btnPlay.setOnClickListener { onPlayClick(item) }
        holder.btnDownload.setOnClickListener { onDownloadClick(item) }
    }

    override fun getItemCount(): Int = items.size
}