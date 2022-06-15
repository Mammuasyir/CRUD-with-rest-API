package com.rival.my_packet.ui.paket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rival.my_packet.R
import com.rival.my_packet.adapter.paket.PaketAdapter
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.databinding.FragmentSatpamPaketBinding
import com.rival.my_packet.helper.SharedPreference
import com.rival.my_packet.model.ResponsePaket
import com.rival.my_packet.model.respon
import kotlinx.android.synthetic.main.create_paket.*
import kotlinx.android.synthetic.main.create_paket.view.*
import kotlinx.android.synthetic.main.fragment_satpam_paket.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SatpamPaketFragment : Fragment() {
    private var _binding: FragmentSatpamPaketBinding? = null
    lateinit var rvSatpam: RecyclerView
    lateinit var roleUser : TextView
    lateinit var sph : SharedPreference

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSatpamPaketBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sph = SharedPreference(requireActivity())

        rvSatpam = binding.rvSatpam
        getPaketSatpam()


        val user = sph.getUser()
        if (user?.role != null) {
            if (user.role == "Admin"){
                binding.fab.visibility = View.VISIBLE
            } else {
               binding.fab.visibility = View.GONE
            }
        }  else {
            binding.fab.visibility = View.GONE
        }


        binding.fab.setOnClickListener {
            addPaket()
        }

        return root
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
        val status = views.findViewById<TextView>(R.id.txt_status)
        val gambar = views.findViewById<Button>(R.id.btn_input_image)

        add.setOnClickListener {
            val nama = namaPenerima.text.toString() 
            val eks = ekspedisi.text.toString()
            val stat = status.text.toString()
            //val gambar = gambar.text.toString()

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
            if (stat.isEmpty()) {
                status.error = "Status tidak boleh kosong"
                status.requestFocus()
                return@setOnClickListener
            }

            ApiConfig.instanceRetrofit.inputPaket(nama, eks, stat)
                .enqueue(object : Callback<respon> {
                    override fun onResponse(call: Call<respon>, response: Response<respon>) {
                      var response = response.body()
                        if (response != null) {
                            progressbar.visibility = View.VISIBLE
                            if (response.status == 1) {
                                Toast.makeText(context, "${response.pesan}", Toast.LENGTH_SHORT).show()
                                alertDialog.dismiss()
                                progressbar.visibility = View.GONE
                                activity?.let { getPaketSatpam() }
                            } else {
                                Toast.makeText(context, "${response.pesan}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<respon>, t: Throwable) {
                        Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show()
                    }
                })

        }
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        getPaketSatpam()
    }

    fun onRefresh() {
        getPaketSatpam()
    }


    private fun getPaketSatpam() {
        ApiConfig.instanceRetrofit.getpaketSatpam().enqueue(object : Callback<ResponsePaket> {
            override fun onResponse(
                call: Call<ResponsePaket>,
                response: Response<ResponsePaket>
            ) {
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
            }
        })
    }
}
