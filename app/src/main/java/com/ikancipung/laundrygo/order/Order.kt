package com.ikancipung.laundrygo.order

import com.google.firebase.database.Exclude

data class Order(
    val id: String? = null,
    val LaundryName: String = "",
    val isCompleted: Boolean = false,
    val time: String = "",
    val date: String = "",
    val category: Int = 0,
    val process: Int = 0,
    val processTimes: List<Pair<String, String>> = emptyList(),

    // Properti tambahan dari struktur database
    val AlamatLaundry: String = "",
    val AlamatPemesanan: String = "",
    val CuciKiloanOption: String = "",
    val IDPemesan: String = "",
    val NamaLaundry: String = "",
    val NamaPemesan: String = "",
    val OrderID: String = "",
    val Orders: Map<String, OrderDetail> = emptyMap(),
    val Pembayaran: String = "",
    val Status: LaundryStatus = LaundryStatus(),
    val WaktuPesan: Long = 0L,
    @Exclude var isAntarJemput: Boolean = false, // Nilai default tidak akan menimpa data Firebase
    @Exclude var isExpress: Boolean = false
)
data class OrderDetail(
    val Service: String = "",
    val Price: String = "",
    val Quantity: Int = 0
)

data class LaundryStatus(
    val isDone: Boolean = false,
    val isInLaundry: Boolean = false,
    val isPaid: Boolean = false,
    val isReceived: Boolean = false,
    val isSent: Boolean = false,
    val isWashing: Boolean = false,
    val isWeighted: Boolean = false
)
