package com.rival.my_packet.api

import com.rival.my_packet.model.ResponseDashboard
import com.rival.my_packet.model.ResponseLanding
import com.rival.my_packet.model.user.ResponseUser
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {

    @FormUrlEncoded
    @POST("regis")
    fun registrasi(
        @Field("name")  name: String,
        @Field("email")  email: String,
        @Field("telp")  telp: String,
        @Field("password")  password: String
    ):Call<ResponseUser>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email")  email: String,
        @Field("password")  password: String
    ):Call<ResponseUser>

    @GET("landing")
    fun getlanding(): Call<ResponseLanding>

    @GET("dashboard")
    fun getdashboard(): Call<ResponseDashboard>
}