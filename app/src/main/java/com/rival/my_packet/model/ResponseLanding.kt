package com.rival.my_packet.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseLanding(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class Result(

	@field:SerializedName("paketselesai")
	val paketselesai: List<PaketselesaiItem?>? = null,

	@field:SerializedName("paket")
	val paket: List<PaketItem?>? = null
) : Parcelable

@Parcelize
data class PaketselesaiItem(

	val penerima_paket: String? = null,
	val img: String? = null,
	val tanggal_input: String? = null,
	val updatedAt: String? = null,
	val nama_penerima: String? = null,
	val createdAt: String? = null,
	val ekspedisi: String? = null,
	val id: Int? = null,
	val status: String? = null
) : Parcelable

@Parcelize
data class PaketItem(

	val penerima_paket: String? = null,
	val img: String? = null,
	val tanggal_input: String? = null,
	val updatedAt: String? = null,
	val nama_penerima: String? = null,
	val createdAt: String? = null,
	val ekspedisi: String? = null,
	val id: Int? = null,
	val status: String? = null
) : Parcelable
