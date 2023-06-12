package com.rival.my_packet.ui.notifications

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rival.my_packet.MainActivity
import com.rival.my_packet.R
import com.rival.my_packet.api.ApiConfig
import com.rival.my_packet.databinding.FragmentLoginBinding
import com.rival.my_packet.databinding.FragmentMusyrifPaketBinding
import com.rival.my_packet.helper.SharedPreference
import com.rival.my_packet.model.user.ResponseUser

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    lateinit var sph: SharedPreference
    val progressDialog: ProgressDialog by lazy {
        ProgressDialog(context)
    }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        sph = SharedPreference(requireActivity())

        binding.btnLogin.setOnClickListener {
            login()
        }

        return binding.root
    }

    @SuppressLint("ShowToast")
    private fun login() {
        val email = binding.edtLoginEmail.text.toString()
        val pass = binding.edtLoginPass.text.toString()

        if (email.isNullOrBlank()) {
            binding.edtLoginEmail.error = "Isi dulu !"
            return
        }

        if (pass.isNullOrBlank()) {
            binding.edtLoginPass.error = "Isi dulu !"
            return
        }

        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()




        ApiConfig.instanceRetrofit.login(email, pass)
            .enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {

                    val respon = response.body()

                    if (response.isSuccessful) {
                        if (respon?.status == 0) {
                            progressDialog.dismiss()
                            Toast.makeText(activity, "${respon.pesan}", Toast.LENGTH_SHORT).show()
                        } else {

                            sph.setStatusLogin(true)
                            sph.setUser(respon?.result!!)

                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                            Toast.makeText(requireActivity(), respon?.pesan, Toast.LENGTH_SHORT).show()
                            requireActivity().finish()
                        }

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(activity, "Email atau Password Salah", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Terjadi kesalahan saat login: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
    }


}