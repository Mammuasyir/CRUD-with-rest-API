package com.rival.my_packet.model

import com.google.gson.annotations.SerializedName

class respon {
    var status: Int? = null
    var pesan: String? = null
    var result: DataPaket? = null

    inner class DataPaket {

        val penerima_paket: String? = null
        val img: String? = null
        val tanggal_input: String? = null
        val updatedAt: String? = null
        val nama_penerima: String? = null
        val createdAt: String? = null
        val ekspedisi: String? = null
        val id: Int? = null
        val status: String? = null
    }
}