package com.rival.my_packet.ui.paket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rival.my_packet.R
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.databinding.FragmentDashboardPaketBinding
import com.rival.my_packet.model.ResponseDashboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardPaketFragment : Fragment() {
    private var _binding: FragmentDashboardPaketBinding? = null
    private val binding get() = _binding!!
    lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardPaketBinding.inflate(inflater, container, false)
        val root: View = binding.root
        swipeRefresh = binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            getData()
        }
        getData()

        return root
    }

    private fun getData() {
        ApiConfig.instanceRetrofit.getdashboard().enqueue(object : Callback<ResponseDashboard> {
            override fun onResponse(
                call: Call<ResponseDashboard>,
                response: Response<ResponseDashboard>
            ) {
                if (swipeRefresh.isRefreshing){
                    swipeRefresh.isRefreshing = false
                }
                val data = response.body()?.result
                binding.tvTotal.text = data?.totalPaketHariIni.toString()
                binding.tvSatpam.text = data?.totalPaketSatpam.toString()
                binding.tvMusyrif.text = data?.totalPaketMusyrif.toString()
                binding.tvSelesai.text = data?.totalPaketSelesai.toString()

            }

            override fun onFailure(call: Call<ResponseDashboard>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                swipeRefresh.isRefreshing = false
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}