package com.rival.my_packet.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rival.my_packet.R
import com.rival.my_packet.adapter.landing.Landing2Adapter
import com.rival.my_packet.adapter.landing.LandingAdapter
import com.rival.my_packet.adapter.paket.PaketAdapter
import com.rival.my_packet.adapter.search.SearchAdapter
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.databinding.FragmentHomeBinding
import com.rival.my_packet.model.ResponseLanding
import com.rival.my_packet.model.ResponsePaket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var rvLanding: RecyclerView
    lateinit var rvLanding2: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        rvLanding = binding.rvLanding
        rvLanding2 = binding.rvLanding2

        swipeRefresh = binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            getLanding()
            getLanding2()
        }

        searchPaket()

        getLanding()
        getLanding2()
//search


        return root
    }

    private fun searchPaket() {
        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.edtSearch.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isNotEmpty()) {
                        getLandingSearch(it)
                        rvLanding2.visibility = View.GONE
                    } else {
                        getLanding()
                        rvLanding2.visibility = View.VISIBLE
                    }
                }
                return true
            }

        })
    }

    private fun getLandingSearch(it: String) {
        ApiConfig.instanceRetrofit.searchPaket(it).enqueue(object : Callback<ResponsePaket> {
            override fun onFailure(call: Call<ResponsePaket>, t: Throwable) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ResponsePaket>,
                response: Response<ResponsePaket>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.result
                    data?.let {
                        val adapter = SearchAdapter(it)
                        rvLanding.adapter = adapter
                        rvLanding.layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        })

    }


    private fun getLanding2() {
        ApiConfig.instanceRetrofit.getlanding().enqueue(object : Callback<ResponseLanding> {

            override fun onResponse(
                call: Call<ResponseLanding>,
                response: Response<ResponseLanding>
            ) {
                if (swipeRefresh.isRefreshing) {
                    swipeRefresh.isRefreshing = false
                }
                if (response.isSuccessful) {
                    val responseLanding =
                        response.body() as ResponseLanding
                    val landing = responseLanding?.result
                    val landingAdapter = Landing2Adapter(landing)
                    rvLanding2.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        (layoutManager as LinearLayoutManager).orientation =
                            LinearLayoutManager.VERTICAL
                        landingAdapter.notifyDataSetChanged()
                        adapter = landingAdapter
                    }
                }
            }

            override fun onFailure(call: Call<ResponseLanding>, t: Throwable) {
                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getLanding() {
        ApiConfig.instanceRetrofit.getlanding().enqueue(object : Callback<ResponseLanding> {

            override fun onResponse(
                call: Call<ResponseLanding>,
                response: Response<ResponseLanding>
            ) {
                if (swipeRefresh.isRefreshing) {
                    swipeRefresh.isRefreshing = false
                }
                if (response.isSuccessful) {
                    val responseLanding =
                        response.body() as ResponseLanding
                    val landing = responseLanding?.result
                    val landingAdapter = LandingAdapter(landing)
                    rvLanding.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(activity)
                        (layoutManager as LinearLayoutManager).orientation =
                            LinearLayoutManager.VERTICAL
                        landingAdapter.notifyDataSetChanged()
                        adapter = landingAdapter
                    }
                }
            }

            override fun onFailure(call: Call<ResponseLanding>, t: Throwable) {
                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

}

