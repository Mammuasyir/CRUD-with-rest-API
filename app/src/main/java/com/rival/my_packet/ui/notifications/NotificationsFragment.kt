package com.rival.my_packet.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rival.my_packet.R
import com.rival.my_packet.WelcomeActivity
import com.rival.my_packet.databinding.FragmentNotificationsBinding
import com.rival.my_packet.helper.SharedPreference

class NotificationsFragment : Fragment() {

    lateinit var sph : SharedPreference
    lateinit var btnKeluar : TextView
    lateinit var namaUser : TextView
    lateinit var emailUser : TextView
    lateinit var telpUser : TextView
    lateinit var img : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_notifications, container,false)
        init(view)
        sph = SharedPreference(requireActivity())
        btnKeluar.setOnClickListener {
            sph.setStatusLogin(false)
            Toast.makeText(activity, "Anda Berhasil Keluar", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, WelcomeActivity::class.java))
            activity?.finish()
        }

        setUser()

        return view

    }

    private fun setUser() {
        val user = sph.getUser()
        namaUser.text = user?.name
        emailUser.text = user?.email
        telpUser.text = user?.telp

        Glide.with(requireActivity())
            .load("https://ui-avatars.com/api/?name=${user?.name}&background=0D8ABC&color=fff&size=128")
            .into(img)
    }

    private fun init (view: View){
        btnKeluar = view.findViewById<TextView>(R.id.tv_logout)
        namaUser = view.findViewById<TextView>(R.id.tv_namaUser)
        emailUser = view.findViewById<TextView>(R.id.tv_emailUser)
        telpUser = view.findViewById<TextView>(R.id.tv_telpUser)
        img = view.findViewById<ImageView>(R.id.profile_image)
    }
}