package com.rival.my_packet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rival.my_packet.databinding.ActivityWelcomeBinding
import com.rival.my_packet.databinding.FragmentHomeBinding
import com.rival.my_packet.databinding.FragmentWelcomeBinding
import com.rival.my_packet.helper.SharedPreference


class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private lateinit var sPH: SharedPreference

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sPH = SharedPreference(requireActivity())
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))

//            sPH.setStatusLogin(true)
//            Toast.makeText(this,"Selamat Datang !", Toast.LENGTH_SHORT).show()
        }

        return root
    }

}