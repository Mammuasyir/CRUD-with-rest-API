package com.rival.my_packet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.helper.SharedPreference
import com.rival.my_packet.model.user.ResponseUser
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var sph : SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sph = SharedPreference(this)

        btn_login.setOnClickListener {
            login()
        }

    }

    private fun login() {
        val email = edt_login_email.text.toString()
        val pass = edt_login_pass.text.toString()

        if (email.isEmpty()){
            edt_login_email.error = "Isi dulu !"
            return
        }

        if (pass.isEmpty()){
            edt_login_pass.error = "Isi dulu !"
            return
        }

        ApiConfig.instanceRetrofit.login(email, pass)
            .enqueue(object : Callback<ResponseUser> {
                override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {

                    val respon = response.body()

                    if (respon != null) {
                        if (respon.status == 0) {
                            Toast.makeText(this@LoginActivity, respon.pesan, Toast.LENGTH_SHORT).show()
                        } else {

                            sph.setStatusLogin(true)
                            sph.setUser(respon.result!!)

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            Toast.makeText(this@LoginActivity, respon.pesan, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                }

                override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}