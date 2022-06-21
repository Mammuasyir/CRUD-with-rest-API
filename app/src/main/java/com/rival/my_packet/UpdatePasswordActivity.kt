package com.rival.my_packet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.databinding.ActivityUpdatePasswordBinding
import com.rival.my_packet.helper.SharedPreference
import com.rival.my_packet.model.user.ResponseUser
import kotlinx.android.synthetic.main.activity_update_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePasswordBinding
    lateinit var sph: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sph = SharedPreference(this)
        val user = sph.getUser()

        binding.btnUpdatePassword.setOnClickListener {
            updatePass()
        }

    }

    private fun updatePass() {
        val oldPass = binding.edtOldPassword.text.toString()
        val newPass = binding.edtNewPassword.text.toString()
        val confirmPass = binding.edtNewPasswordConfirm.text.toString()

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            edt_old_password.error = "Password Lama Tidak Boleh Kosong"
            edt_new_password.error = "Password Baru Tidak Boleh Kosong"
            edt_new_password_confirm.error = "Konfirmasi Password Tidak Boleh Kosong"
        } else if (newPass != confirmPass) {
            edt_new_password.error = "Password Baru Tidak Sama"
            edt_new_password_confirm.error = "Konfirmasi Password Tidak Sama"
        } else if (oldPass == newPass) {
            edt_old_password.error = "Password Baru Tidak Boleh Sama Dengan Password Lama"
            edt_new_password.error = "Password Baru Tidak Boleh Sama Dengan Password Lama"
            edt_new_password_confirm.error =
                "Konfirmasi Password Tidak Boleh Sama Dengan Password Lama"
        } else {
            val user = sph.getUser()
            user?.id?.let {
                ApiConfig.instanceRetrofit.changePassword(it, oldPass, newPass)
                    .enqueue(object : Callback<ResponseUser> {
                        override fun onResponse(
                            call: Call<ResponseUser>,
                            response: Response<ResponseUser>
                        ) {
                            val body = response.body()
                            if (response.isSuccessful) {
                                if (body?.status == 1) {
                                    Toast.makeText(
                                        this@UpdatePasswordActivity,
                                        "${body.pesan}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }

                            } else {
                                Toast.makeText(
                                    this@UpdatePasswordActivity,
                                    "Password Salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                            Toast.makeText(
                                this@UpdatePasswordActivity,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }

        }


    }
}
