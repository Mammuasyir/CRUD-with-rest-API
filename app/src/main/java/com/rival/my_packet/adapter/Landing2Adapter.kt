package com.rival.my_packet.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.rival.my_packet.R
import com.rival.my_packet.model.Result

class Landing2Adapter(var landingItem: Result?) : RecyclerView.Adapter<Landing2Adapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val tvNamaSelesai = itemView.findViewById<TextView>(R.id.nama)
        val tvTanggalSelesai = itemView.findViewById<TextView>(R.id.tanggal)
        val tvStatusSelesai = itemView.findViewById<TextView>(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_landing, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataSelesai = landingItem?.paketselesai?.get(position)

        holder.tvNamaSelesai.text = dataSelesai?.nama_penerima
        holder.tvTanggalSelesai.text = dataSelesai?.tanggal_input
        holder.tvStatusSelesai.text = dataSelesai?.status

        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context).create()
            val views = LayoutInflater.from(context).inflate(R.layout.detail_dialog, null)
            alertDialog.setView(views)
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false)

            val nama = views.findViewById<TextView>(R.id.tv_nama_dtl)
            val tanggal = views.findViewById<TextView>(R.id.tv_taggal_dtl)
            val status = views.findViewById<TextView>(R.id.tv_status_paket)
            val UrlImage = "https://paket.siyap.co.id/storage/${landingItem?.paket?.get(position)?.img}"

            Glide.with(context).load(UrlImage).into(views.findViewById<ImageView>(R.id.img_paket_dtl))
            nama.text = dataSelesai?.nama_penerima
            tanggal.text = dataSelesai?.tanggal_input
            status.text = dataSelesai?.status

            views.findViewById<Button>(R.id.btn_close).setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }

    }

    override fun getItemCount(): Int {
        return landingItem?.paketselesai?.size ?: 0

    }

}