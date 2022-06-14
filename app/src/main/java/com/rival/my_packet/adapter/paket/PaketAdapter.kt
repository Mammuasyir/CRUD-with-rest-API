package com.rival.my_packet.adapter.paket


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rival.my_packet.R
import com.rival.my_packet.model.ResultItem

class PaketAdapter(var paket: List<ResultItem?>?) : RecyclerView.Adapter<PaketAdapter.MyViewHolder>()  {
    class MyViewHolder (val view:View) : RecyclerView.ViewHolder(view) {

        val tvTitle = view.findViewById<TextView>(R.id.txt_title)
        val btnDetail = view.findViewById<ImageButton>(R.id.btn_detail)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_satpam, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.itemView.context
        val data = paket?.get(position)
        holder.tvTitle.text = data?.nama_penerima

        holder.btnDetail.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context).create()
            val views = LayoutInflater.from(context).inflate(R.layout.detail_dialog, null)
            alertDialog.setView(views)
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false)

            val nama = views.findViewById<TextView>(R.id.tv_nama_dtl)
            val tanggal = views.findViewById<TextView>(R.id.tv_taggal_dtl)
            val status = views.findViewById<TextView>(R.id.tv_status_paket)
            val UrlImage = "https://paket.siyap.co.id/storage/${data?.img}"

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

    override fun getItemCount() = paket?.size ?: 0
}