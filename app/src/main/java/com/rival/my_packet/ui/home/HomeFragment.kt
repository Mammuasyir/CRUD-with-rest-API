package com.rival.my_packet.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rival.my_packet.adapter.Landing2Adapter
import com.rival.my_packet.adapter.LandingAdapter
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.databinding.FragmentHomeBinding
import com.rival.my_packet.model.ResponseLanding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var rvLanding: RecyclerView
    lateinit var rvLanding2: RecyclerView

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

        getLanding()
        getLanding2()


        return root
    }

    private fun getLanding2() {
        ApiConfig.instanceRetrofit.getlanding().enqueue(object : Callback<ResponseLanding> {

            override fun onResponse(
                call: Call<ResponseLanding>,
                response: Response<ResponseLanding>
            ) {
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