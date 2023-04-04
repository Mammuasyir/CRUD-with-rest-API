package com.rival.my_packet.api

import com.rival.my_packet.model.ResponseDashboard
import com.rival.my_packet.model.ResponseLanding
import com.rival.my_packet.model.ResponsePaket
import com.rival.my_packet.model.respon
import com.rival.my_packet.model.user.ResponseUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiServices {

    @FormUrlEncoded
    @POST("registrasi")
    fun registrasi(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("telp") telp: String,
        @Field("password") password: String
    ): Call<ResponseUser>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseUser>


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

    @GET("paket-status/{paket}")
    fun getpaket(
        @Path("paket") paket: String
    ): Call<ResponsePaket>

    @FormUrlEncoded
    @POST("paket-input")
    fun inputPaket(
        @Field("nama_penerima") nama_penerima: String,
        @Field("ekspedisi") ekspedisi: String,
        @Field("status") status: String,
//        @Field("img")  img: String
    ): Call<respon>

    @FormUrlEncoded
    @POST("paket-update/{id}")
    fun updatePaket(
        @Path("id") id: Int,
        @Field("status") status: String,
        @Field("penerima_paket") penerima_paket: String,
    ): Call<respon>

    @DELETE("paket-delete/{id}")
    fun deletePaket(
        @Path("id") id: Int
    ): Call<ResponsePaket>

    @FormUrlEncoded
    @PUT("change-password/{id}")
    fun changePassword(
        @Path("id") id: Int,
        @Field("password_lama") password_lama: String,
        @Field("password_baru") password_baru: String
    ): Call<ResponseUser>

    @Multipart
    @POST("paket-input")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("nama_penerima") nama_penerima: RequestBody,
        @Part("ekspedisi") ekspedisi: RequestBody,
        @Part("status") status: RequestBody,
    ): Call<respon>

    //search
    @GET("paket-search")
    fun searchPaket(
        @Query("cari") nama: String
    ): Call<ResponseLanding>

}