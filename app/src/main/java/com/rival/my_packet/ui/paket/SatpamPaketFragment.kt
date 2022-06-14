package com.rival.my_packet.ui.paket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.rival.my_packet.adapter.SatpamAdapter
import com.rival.my_packet.api.ApiConfig

import com.rival.my_packet.databinding.FragmentSatpamPaketBinding

import com.rival.my_packet.model.ResponseSatpam
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        return root
    }

    private fun getPaketSatpam() {
        ApiConfig.instanceRetrofit.getpaketSatpam().enqueue(object : Callback<ResponseSatpam> {
            override fun onResponse(
                call: Call<ResponseSatpam>,
                response: Response<ResponseSatpam>
            ) {
                if (response.isSuccessful) {
                    val responseSatpam =
                        response.body() as ResponseSatpam
                    val landing = responseSatpam.result
                    val landingAdapter = SatpamAdapter(landing)
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

            override fun onFailure(call: Call<ResponseSatpam>, t: Throwable) {
                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
