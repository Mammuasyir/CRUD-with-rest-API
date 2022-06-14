package com.rival.my_packet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseSatpam(

	@field:SerializedName("result")
	val result: List<ResultItem?>? = null,

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class ResultItem(

	@field:SerializedName("penerima_paket")
	val penerima_paket: String? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("tanggal_input")
	val tanggal_input: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("nama_penerima")
	val nama_penerima: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("ekspedisi")
	val ekspedisi: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
