package com.rival.my_packet.adapter.paket


import android.annotation.SuppressLint
import android.app.ProgressDialog
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
import com.rival.my_packet.model.respon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler

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

// hide button edit and delete
        sph = SharedPreference(context as FragmentActivity)
        val user = sph.getUser()
        if (user?.role == "Admin" || user?.role == "Satpam") {
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnEdit.visibility = View.VISIBLE
        } else {
            holder.btnEdit.visibility = View.GONE
            holder.btnDelete.visibility = View.GONE
        }

        // progress bar


        holder.btnEdit.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context).create()
            val views = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null)
            alertDialog.setView(views)
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setCancelable(false)

            val nama = views.findViewById<TextView>(R.id.tv_nama_dtl)
            val tanggal = views.findViewById<TextView>(R.id.tv_tanggal_dtl)
            statusList = views.findViewById<Spinner>(R.id.status_paket)
            var pengambil = views.findViewById<EditText>(R.id.edt_pengambil_paket)
            val update = views.findViewById<Button>(R.id.btn_update)

            nama.text = data?.nama_penerima
            tanggal.text = data?.tanggal_input
            // spiner selected
            statusList[R.array.Status] = data?.status
            pengambil.setText(data?.penerima_paket)
            val progressDialog: ProgressDialog by lazy {
                ProgressDialog(context)
            }
            update.setOnClickListener {
                val status = statusList.selectedItem.toString()
                val pengambil = pengambil.text.toString()
                val builder = AlertDialog.Builder(context)

                progressDialog.setMessage("Mengupdate...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                ApiConfig.instanceRetrofit.updatePaket(
                    data?.id!!,
                    status,
                    pengambil
                ).enqueue(object : Callback<respon> {
                    override fun onFailure(call: Call<respon>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<respon>,
                        response: Response<respon>
                    ) {
                        progressDialog.dismiss()
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()

                            paket?.get(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, paket!!.size)
                            // clear list data
                            // get data from server
                            ApiConfig.instanceRetrofit.getpaketSatpam().enqueue(object :
                                Callback<ResponsePaket> {
                                override fun onResponse(
                                    call: Call<ResponsePaket>,
                                    response: Response<ResponsePaket>
                                ) {
                                    if (response.isSuccessful) {
                                        paket = response.body()?.result
                                        notifyDataSetChanged()
                                    }
                                }
                                override fun onFailure(
                                    call: Call<ResponsePaket>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(
                                        context,
                                        "${t.localizedMessage}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })

                        } else {
                            Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }

            views.findViewById<Button>(R.id.btn_close).setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
        val progressDialog: ProgressDialog by lazy {
            ProgressDialog(context)
        }



        // set onclick listener to activity detail
        holder.btnDelete.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Hapus")
            builder.setMessage("Apakakah yakin hapus paket ${data?.nama_penerima} ?")
            builder.setPositiveButton("Ya") { dialog, which ->

                progressDialog.setMessage("Loading...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                ApiConfig.instanceRetrofit.deletePaket(data?.id!!).enqueue(object :
                    Callback<ResponsePaket> {
                    override fun onResponse(
                        call: Call<ResponsePaket>,
                        response: Response<ResponsePaket>
                    ) {


                        progressDialog.dismiss()
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {

                                Toast.makeText(
                                    context,
                                    "${response.body()?.pesan}",
                                    Toast.LENGTH_SHORT
                                ).show()
                         // clear list data
                                paket?.get(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, paket!!.size)
                                // clear list data
                                // get data from server
                                ApiConfig.instanceRetrofit.getpaketSatpam().enqueue(object :
                                    Callback<ResponsePaket> {
                                    override fun onResponse(
                                        call: Call<ResponsePaket>,
                                        response: Response<ResponsePaket>
                                    ) {
                                        if (response.isSuccessful) {
                                            paket = response.body()?.result
                                            notifyDataSetChanged()
                                        }
                                    }
                                    override fun onFailure(
                                        call: Call<ResponsePaket>,
                                        t: Throwable
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "${t.localizedMessage}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })



                            } else {
                                Toast.makeText(context, "Gagal hapus paket", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponsePaket>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
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
            val pengambil = views.findViewById<TextView>(R.id.tv_pengambil_paket)
            val UrlImage = "https://paket.idnbs-smk-bogor.my.id/storage/${data?.img}"

            Glide.with(context).load(UrlImage)
                .into(views.findViewById<ImageView>(R.id.img_paket_dtl))
            nama.text = data?.nama_penerima
            tanggal.text = data?.tanggal_input
            // status from array

            status.text = data?.status
            pengambil.text = data?.penerima_paket

            views.findViewById<Button>(R.id.btn_close).setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    private fun notifyDataSetChanged(position: Int, size: Int) {
paket?.get(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, size)
    }


    override fun getItemCount() = paket?.size ?: 0

    private operator fun Spinner.set(status: Int, value: String?) {
        val spinnerAdapter = adapter as ArrayAdapter<String>
        val index = spinnerAdapter.getPosition(value)
        setSelection(index)
    }
}





