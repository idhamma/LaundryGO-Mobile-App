package com.ikancipung.laundrygo.order

import com.google.firebase.database.Exclude

data class Order(
    var id: String? = null,
    var LaundryName: String = "",
    var isCompleted: Boolean = false,
    var time: String = "",
    var date: String = "",
    var category: Int = 0,
    var process: Int = 0,
    var processTimes: List<Pair<String, String>> = emptyList(),

    // Properti tambahan dari struktur database
    var AlamatLaundry: String = "",
    var AlamatPemesanan: String = "",
    var CuciKiloanOption: String = "",
    var IDPemesan: String = "",
    var NamaLaundry: String = "",
    var NamaPemesan: String = "",
    var OrderID: String = "",
    var Orders: Map<String, OrderDetail> = emptyMap(),
    var Pembayaran: String = "",
    var Status: LaundryStatus = LaundryStatus(),
    var WaktuPesan: Long = 0L,
    @Exclude var isAntarJemput: Boolean = false, // Nilai default tidak akan menimpa data Firebase
    @Exclude var isExpress: Boolean = false
)

data class OrderDetail(
    var Service: String = "",
    var Price: String = "",
    var Quantity: Int = 0
)

data class LaundryStatus(
    var isDone: LaundryStatusDetail = LaundryStatusDetail(),
    var isInLaundry: LaundryStatusDetail = LaundryStatusDetail(),
    var isPaid: LaundryStatusDetail = LaundryStatusDetail(),
    var isReceived: LaundryStatusDetail = LaundryStatusDetail(),
    var isSent: LaundryStatusDetail = LaundryStatusDetail(),
    var isWashing: LaundryStatusDetail = LaundryStatusDetail(),
    var isWeighted: LaundryStatusDetail = LaundryStatusDetail()
)

data class LaundryStatusDetail(
    var value: Boolean = false,
    var time: Long? = null
)