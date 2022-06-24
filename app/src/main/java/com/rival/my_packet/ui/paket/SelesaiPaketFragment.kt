package com.rival.my_packet.ui.paket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rival.my_packet.adapter.paket.PaketAdapter
import com.rival.my_packet.adapter.paket.SelesaiAdapter
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.databinding.FragmentSelesaiPaketBinding
import com.rival.my_packet.model.ResponsePaket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SelesaiPaketFragment : Fragment() {
    private var _binding: FragmentSelesaiPaketBinding? = null
    lateinit var rvSelesai: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentSelesaiPaketBinding.inflate(inflater, container, false)
        val root: View = binding.root
        swipeRefresh = binding.swipeRefreshLayout
        rvSelesai = binding.rvSelesai
        getPaketSelesai()

        swipeRefresh.setOnRefreshListener {
            getPaketSelesai()
        }
        return root
    }

    private fun getPaketSelesai() {
        ApiConfig.instanceRetrofit.getpaketSelesai().enqueue(object : Callback<ResponsePaket> {
            override fun onResponse(call: Call<ResponsePaket>, response: Response<ResponsePaket>) {
                if (swipeRefresh.isRefreshing){
                    swipeRefresh.isRefreshing = false
                }
                if (response.isSuccessful) {
                    val ResponsePaket =
                        response.body() as ResponsePaket
                    val landing = ResponsePaket.result
                    val landingAdapter = SelesaiAdapter(landing)
                    rvSelesai.apply {
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