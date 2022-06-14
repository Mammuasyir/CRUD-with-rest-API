package com.rival.my_packet.ui.paket

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rival.my_packet.R
import com.rival.my_packet.adapter.paket.PaketAdapter
import com.rival.my_packet.api.ApiConfig

import com.rival.my_packet.databinding.FragmentSatpamPaketBinding

import com.rival.my_packet.model.ResponsePaket
import kotlinx.android.synthetic.main.create_paket.*
import kotlinx.android.synthetic.main.create_paket.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream



class SatpamPaketFragment : Fragment() {
    private var _binding: FragmentSatpamPaketBinding? = null
    lateinit var rvSatpam: RecyclerView

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSatpamPaketBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rvSatpam = binding.rvSatpam
        getPaketSatpam()

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
            val ekspedisi = ekspedisi.text.toString()
            val status = status.text.toString()
            //val gambar = gambar.text.toString()

            ApiConfig.instanceRetrofit.inputPaket(nama, ekspedisi, status)
                .enqueue(object : Callback<ResponsePaket> {
                    override fun onFailure(call: Call<ResponsePaket>, t: Throwable) {
                        Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<ResponsePaket>,
                        response: Response<ResponsePaket>
                    ) {
                        val respon = response.body()

                        if (respon != null) {
                            if (respon.status == 0){
                                Toast.makeText(context, "Gagal nambah", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        }
                    }
                })

        }
        alertDialog.show()
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
