package com.rival.my_packet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseDashboard(

	@field:SerializedName("result")
	val result: Result2? = null,

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class Result2(

	@field:SerializedName("total_paket_musyrif")
	val totalPaketMusyrif: Int? = null,

	@field:SerializedName("total_paket_selesai")
	val totalPaketSelesai: Int? = null,

	@field:SerializedName("total_user")
	val totalUser: Int? = null,

	@field:SerializedName("total_paket_satpam")
	val totalPaketSatpam: Int? = null,

	@field:SerializedName("total_paket")
	val totalPaket: Int? = null,

	@field:SerializedName("total_paket_hari_ini")
	val totalPaketHariIni: Int? = null
) : Parcelable
