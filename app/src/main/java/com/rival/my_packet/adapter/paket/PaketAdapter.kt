package com.rival.my_packet.adapter.paket


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rival.my_packet.R
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.helper.SharedPreference
import com.rival.my_packet.model.ResponsePaket
import com.rival.my_packet.model.ResultItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaketAdapter(var paket: List<ResultItem?>? = listOf()) :
    RecyclerView.Adapter<PaketAdapter.MyViewHolder>() {
    lateinit var sph: SharedPreference
    lateinit var statusList: Spinner

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val tvTitle = view.findViewById<TextView>(R.id.txt_title)
        val btnEdit = view.findViewById<ImageButton>(R.id.btn_edit)
        val btnDetail = view.findViewById<ImageButton>(R.id.btn_detail)
        val btnDelete = view.findViewById<ImageButton>(R.id.btn_delete)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_satpam, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val context = holder.itemView.context
        val data = paket?.get(position)
        holder.tvTitle.text = data?.nama_penerima

// hide button detail
//        sph = SharedPreference(context as FragmentActivity)
//        val user = sph.getUser()
//        if (user?.role != "Musyrif") {
//            holder.btnDelete.visibility = View.VISIBLE
//        } else {
//            holder.btnDelete.visibility = View.GONE
//        }

        holder.btnEdit.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context).create()
            val views = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null)
            alertDialog.setView(views)
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false)

            val nama = views.findViewById<TextView>(R.id.tv_nama_dtl)
            val tanggal = views.findViewById<TextView>(R.id.tv_taggal_dtl)
            statusList = views.findViewById<Spinner>(R.id.status_paket)
            var pengambil = views.findViewById<EditText>(R.id.edt_pengambil_paket)

            nama.text = data?.nama_penerima
            tanggal.text = data?.tanggal_input
            statusList.adapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf("Satpam", "Musyrif", "Selesai")
            )
            pengambil.setText(data?.penerima_paket)

            views.findViewById<Button>(R.id.btn_close).setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }

        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Hapus")
            builder.setMessage("Apakakah yakin hapus paket ${data?.nama_penerima} ?")
            builder.setPositiveButton("Ya") { dialog, which ->
                ApiConfig.instanceRetrofit.deletePaket(data?.id!!).enqueue(object :
                    Callback<ResponsePaket> {
                    override fun onResponse(
                        call: Call<ResponsePaket>,
                        response: Response<ResponsePaket>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                Toast.makeText(context, "Berhasil hapus paket", Toast.LENGTH_SHORT)
                                    .show()

                                paket?.get(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, itemCount + 1)

                            }
                        } else {
                            Toast.makeText(context, "Gagal hapus paket", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<ResponsePaket>, t: Throwable) {
                        Toast.makeText(context, "Gagal hapus paket", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            builder.setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }


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

            Glide.with(context).load(UrlImage)
                .into(views.findViewById<ImageView>(R.id.img_paket_dtl))
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

    fun clearData() {
        listOf(paket)
        notifyDataSetChanged()
    }
}