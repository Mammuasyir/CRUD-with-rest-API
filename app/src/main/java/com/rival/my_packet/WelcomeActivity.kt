package com.rival.my_packet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rival.my_packet.databinding.ActivityWelcomeBinding
import com.rival.my_packet.helper.SharedPreference

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding
    private lateinit var sPH: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sPH = SharedPreference(this)
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))

//            sPH.setStatusLogin(true)
//            Toast.makeText(this,"Selamat Datang !", Toast.LENGTH_SHORT).show()
        }

        binding.btnRegistrasi.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}