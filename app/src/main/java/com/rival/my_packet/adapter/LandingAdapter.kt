package com.rival.my_packet.adapter

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rival.my_packet.R
import com.rival.my_packet.model.PaketItem
import com.rival.my_packet.model.PaketselesaiItem
import com.rival.my_packet.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LandingAdapter(var landingItem: Result?) :
    RecyclerView.Adapter<LandingAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvNama = itemView.findViewById<TextView>(R.id.nama)
        val tvTanggal = itemView.findViewById<TextView>(R.id.tanggal)
        val tvStatus = itemView.findViewById<TextView>(R.id.status)

//        val tvNamaSelesai = itemView.findViewById<TextView>(R.id.nama)
//        val tvTanggalSelesai = itemView.findViewById<TextView>(R.id.tanggal)
//        val tvStatusSelesai = itemView.findViewById<TextView>(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_landing, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = landingItem?.paket?.get(position)
        val context = holder.itemView.context
        holder.tvNama.text = data?.nama_penerima
        holder.tvTanggal.text = data?.tanggal_input
        holder.tvStatus.text = data?.status

        if (data?.status == "satpam") {
            holder.tvStatus.setTextColor(context.resources.getColor(R.color.purple_700))
        } else {
            holder.tvStatus.setTextColor(context.resources.getColor(R.color.teal_700))
        }


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
            nama.text = data?.nama_penerima
            tanggal.text = data?.tanggal_input
            status.text = data?.status

            views.findViewById<Button>(R.id.btn_close).setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    override fun getItemCount() = landingItem?.paket?.size ?: 0


}

