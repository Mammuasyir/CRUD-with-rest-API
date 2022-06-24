package com.rival.my_packet.ui.paket

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isEmpty
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

private var getFile: File? = null

class SatpamPaketFragment : Fragment() {
    companion object {
        const val CAMERA_REQUEST_CODE = 1
    }

    private var _binding: FragmentSatpamPaketBinding? = null
    lateinit var rvSatpam: RecyclerView
    lateinit var roleUser: TextView
    lateinit var sph: SharedPreference
    lateinit var imgUri: Uri
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var statusList: Spinner
    var path: String? = null
    private lateinit var currentPhotoPath: String


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


//    private fun addPaket() {
//        val alertDialog = AlertDialog.Builder(requireActivity()).create()
//        val views = LayoutInflater.from(context).inflate(R.layout.create_paket, null)
//        alertDialog.setView(views)
//        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        alertDialog.setCancelable(true)
//
//        val add = views.findViewById<Button>(R.id.btn_add)
//        val namaPenerima = views.findViewById<TextView>(R.id.txt_nama_penerima)
//        val ekspedisi = views.findViewById<TextView>(R.id.txt_ekspedisi)
//        statusList = views.findViewById<Spinner>(R.id.status_paket)
//        val gambar = views.findViewById<ImageView>(R.id.img_pengiriman)
//
//
//
//
//        gambar.setOnClickListener {
//            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(i, CAMERA_REQUEST_CODE)
//        }
//        add.setOnClickListener {
//            if (getFile != null) {
//                val file = getFile as File
//
//                val namaPenerima =
//                    namaPenerima.text.toString().toRequestBody("text/plain".toMediaType())
//                val ekspedisi = ekspedisi.text.toString().toRequestBody("text/plain".toMediaType())
//                val status =
//                    statusList.selectedItem.toString().toRequestBody("text/plain".toMediaType())
//                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                    "img",
//                    file.name,
//                    requestImageFile
//                )
//
//
//
//                ApiConfig.instanceRetrofit.inputPaket(
//                    imageMultipart,
//                    namaPenerima,
//                    ekspedisi,
//                    status
//                ).enqueue(object :
//                    Callback<respon> {
//                    override fun onFailure(call: Call<respon>, t: Throwable) {
//                        Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT)
//                            .show()
//                        swipeRefresh.isRefreshing = false
//                    }
//
//                    override fun onResponse(call: Call<respon>, response: Response<respon>) {
//                        if (response.isSuccessful) {
//                            var response = response.body()
//                            if (response != null) {
//                                if (swipeRefresh.isRefreshing) {
//                                    swipeRefresh.isRefreshing = false
//                                }
//                                progressbar.visibility = View.VISIBLE
//                                if (response.status == 1) {
//                                    Toast.makeText(context, "${response.pesan}", Toast.LENGTH_SHORT)
//                                        .show()
//
//                                    activity?.let { getPaketSatpam() }
//                                } else {
//                                    Toast.makeText(context, "${response.pesan}", Toast.LENGTH_SHORT)
//                                        .show()
//                                }
//                            }
//                        } else {
//                            Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                })
//
//            } else {
//                Toast.makeText(
//                    activity,
//                    "Silakan masukkan berkas gambar terlebih dahulu.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//        alertDialog.show()
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap


            val imageView = view?.findViewById<ImageView>(R.id.img_pengiriman)

            imageView?.setImageBitmap(imageBitmap)


        }
    }

    private fun getPaketSatpam() {
        ApiConfig.instanceRetrofit.getpaketSatpam().enqueue(object : Callback<ResponsePaket> {
            override fun onResponse(
                call: Call<ResponsePaket>,
                response: Response<ResponsePaket>
            ) {
                if (swipeRefresh.isRefreshing) {
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



