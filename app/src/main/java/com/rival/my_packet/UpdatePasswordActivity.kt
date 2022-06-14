package com.rival.my_packet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.rival.my_packet.databinding.ActivityUpdatePasswordBinding
import com.rival.my_packet.helper.SharedPreference

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePasswordBinding
    lateinit var sph: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sph = SharedPreference(this)
        val user = sph.getUser()

        binding.cardVerifyPassword.visibility = View.VISIBLE
        binding.cardUpdatePassword.visibility = View.GONE
        binding.btnOtorisasiPassword.setOnClickListener {
            val pass = binding.edtNowPassword.text.toString()
            if (pass.isEmpty()) {
                binding.edtNowPassword.error = "Password tidak boleh kosong"
                binding.edtNowPassword.requestFocus()
                return@setOnClickListener
            }
        }

        binding.btnUpdatePassword.setOnClickListener updatePassword@{

            val passBaru = binding.edtNewPassword.text.toString()
            val passKonfirmasi = binding.edtNewPasswordConfirm.text.toString()

            if (passBaru.isEmpty()){
                binding.edtNewPassword.error = "Password baru dibutuhkan!"
                binding.edtNewPassword.requestFocus()
                return@updatePassword
            }

            if (passBaru.length < 4){
                binding.edtNewPassword.error = "Password minimal 6 karakter!"
                binding.edtNewPassword.requestFocus()
                return@updatePassword
            }


            if (passBaru != passKonfirmasi){
                binding.edtNewPasswordConfirm.error = "Password tidak sama!"
                binding.edtNewPasswordConfirm.requestFocus()
                return@updatePassword
            }

        }
    }
}