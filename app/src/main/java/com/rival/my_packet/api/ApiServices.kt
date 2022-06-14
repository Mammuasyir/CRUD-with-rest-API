package com.rival.my_packet.api

import com.rival.my_packet.model.ResponseDashboard
import com.rival.my_packet.model.ResponseLanding
import com.rival.my_packet.model.ResponsePaket

import com.rival.my_packet.model.user.ResponseUser
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {

    @FormUrlEncoded
    @POST("registrasi")
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

    @GET("paket-status/satpam")
    fun getpaketSatpam(): Call<ResponsePaket>

    @GET("paket-status/musyrif")
    fun getpaketMusyrif(): Call<ResponsePaket>

    @GET("paket-status/selesai")
    fun getpaketSelesai(): Call<ResponsePaket>

    @FormUrlEncoded
    @POST("paket-input")
    fun inputPaket(
        @Field("nama_penerima")  nama_penerima: String,
        @Field("ekspedisi")  ekspedisi: String,
        @Field("status")  status: String,
        //@Field("img")  img: String
    ):Call<ResponsePaket>
}