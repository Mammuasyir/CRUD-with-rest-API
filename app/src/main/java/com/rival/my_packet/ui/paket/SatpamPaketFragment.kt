package com.rival.my_packet.ui.paket

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rival.my_packet.Create_Activity
import com.rival.my_packet.R
import com.rival.my_packet.adapter.paket.PaketAdapter
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.databinding.FragmentSatpamPaketBinding
import com.rival.my_packet.helper.SharedPreference
import com.rival.my_packet.model.ResponsePaket
import com.rival.my_packet.model.respon
import kotlinx.android.synthetic.main.fragment_dashboard_paket.*
import kotlinx.android.synthetic.main.fragment_satpam_paket.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class SatpamPaketFragment : Fragment() {
    private var _binding: FragmentSatpamPaketBinding? = null
    lateinit var rvSatpam: RecyclerView
    lateinit var roleUser: TextView
    lateinit var sph: SharedPreference
    lateinit var imgUri: Uri
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var statusList: Spinner
    var path: String? = null


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSatpamPaketBinding.inflate(inflater, container, false)

        swipeRefresh = binding.swipeRefreshLayout
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            100
        )


        sph = SharedPreference(requireActivity())

        rvSatpam = binding.rvSatpam
        getPaketSatpam()


        val user = sph.getUser()


        if (user?.role != null) {
            if (user.role != "Musyrif") {
                binding.fab.visibility = View.VISIBLE
            } else {
                binding.fab.visibility = View.GONE
            }
        } else {
            binding.fab.visibility = View.GONE
        }


        binding.fab.setOnClickListener {
            startActivity(Intent(requireActivity(), Create_Activity::class.java))
            activity?.finish()
        }
        swipeRefresh.setOnRefreshListener {
            getPaketSatpam()
        }


        return binding.root
    }

    private fun addPaket() {
        val alertDialog = AlertDialog.Builder(requireActivity()).create()
        val views = LayoutInflater.from(context).inflate(R.layout.create_paket, null)
        alertDialog.setView(views)
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.setCancelable(true)

        val add = views.findViewById<Button>(R.id.btn_add)
        val namaPenerima = views.findViewById<TextView>(R.id.txt_nama_penerima)
        val ekspedisi = views.findViewById<TextView>(R.id.txt_ekspedisi)
        statusList = views.findViewById<Spinner>(R.id.status_paket)
        val gambar = views.findViewById<Button>(R.id.btn_input_image)

        gambar.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 0)
        }
        add.setOnClickListener {
            val nama = namaPenerima.text.toString()
            val eks = ekspedisi.text.toString()
//            statusList.adapter = ArrayAdapter(
//                requireContext(),
//                android.R.layout.simple_spinner_dropdown_item,
//                arrayOf("Satpam", "Musyrif", "Selesai")
//            )
            val status = statusList.selectedItem.toString()




            if (nama.isEmpty()) {
                namaPenerima.error = "Nama Penerima tidak boleh kosong"
                namaPenerima.requestFocus()
                return@setOnClickListener
            }
            if (eks.isEmpty()) {
                ekspedisi.error = "Ekspedisi tidak boleh kosong"
                ekspedisi.requestFocus()
                return@setOnClickListener
            }
            if (status.isEmpty()) {
                return@setOnClickListener
            }

            ApiConfig.instanceRetrofit.inputPaket(nama, eks, status )
                .enqueue(object : Callback<respon> {
                    override fun onResponse(call: Call<respon>, response: Response<respon>) {
                        var response = response.body()
                        if (response != null) {
                            if (swipeRefresh.isRefreshing){
                                swipeRefresh.isRefreshing = false
                            }
                            progressbar.visibility = View.VISIBLE
                            if (response.status == 1) {
                                Toast.makeText(context, "${response.pesan}", Toast.LENGTH_SHORT)
                                    .show()
                                alertDialog.dismiss()
                                progressbar.visibility = View.GONE
                                activity?.let { getPaketSatpam() }
                            } else {
                                Toast.makeText(context, "${response.pesan}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<respon>, t: Throwable) {
                        Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT)
                            .show()
                        swipeRefresh.isRefreshing = false
                    }
                })
        }
        alertDialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        }
    }

    private fun getPaketSatpam() {
        ApiConfig.instanceRetrofit.getpaketSatpam().enqueue(object : Callback<ResponsePaket> {
            override fun onResponse(
                call: Call<ResponsePaket>,
                response: Response<ResponsePaket>
            ) {
                if (swipeRefresh.isRefreshing){
                    swipeRefresh.isRefreshing = false
                }
                if (response.isSuccessful) {
                    val ResponsePaket =
                        response.body() as ResponsePaket
                    val landing = ResponsePaket.result
                    val landingAdapter = PaketAdapter(landing)
                    rvSatpam.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        (layoutManager as LinearLayoutManager).orientation =
                            LinearLayoutManager.VERTICAL
                        landingAdapter.notifyDataSetChanged()
                        adapter = landingAdapter

                    }
                }
            }

            override fun onFailure(call: Call<ResponsePaket>, t: Throwable) {
                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                swipeRefresh.isRefreshing = false
            }
        })
    }
}