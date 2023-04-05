package com.rival.my_packet.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.rival.my_packet.model.user.DataUser

class SharedPreference(activity: FragmentActivity){

    val login = "Login"
    val myPref = "Main_Pref"
    val sharedPreference: SharedPreferences

    val user = "User"


    init {
        sharedPreference = activity.getSharedPreferences(myPref, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status: Boolean){
        sharedPreference.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin():Boolean{
        return sharedPreference.getBoolean(login, false)
    }

    fun setUser(value : DataUser){
        //ubah dari data object ke dta string
        val data = Gson().toJson(value, DataUser::class.java)
        sharedPreference.edit().putString(user, data).apply()
    }

    fun getUser(): DataUser?{
        //ubah dari data string ke data object
        val data = sharedPreference.getString(user, null) ?: return null
        return Gson().fromJson<DataUser>(data, DataUser::class.java)
    }



    //delete data user
    fun deleteUser(){
        sharedPreference.edit().remove(user).apply()
    }

}