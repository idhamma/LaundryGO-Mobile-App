package com.ikancipung.laundrygo.order

import com.google.firebase.database.FirebaseDatabase

// Data class untuk Order
data class Order(
    val IDPemesan: String = "",
    val NamaLaundry: String = "",
    val NamaPemesan: String = "",
    val AlamatPemesanan: String = "",
    val WaktuPesan: Long = 0L,
    val isAntarJemput: Boolean = false,
    val isExpress: Boolean = false,
    val Status: Status = Status(),
    val layanan: Map<String, Int> = mapOf() // Layanan dan jumlahnya
)

// Data class untuk status
data class Status(
    val isDone: Boolean = false,
    val isInLaundry: Boolean = false,
    val isPaid: Boolean = false,
    val isReceived: Boolean = false,
    val isSent: Boolean = false,
    val isWashing: Boolean = false,
    val isWeighted: Boolean = false
)

// Daftar harga berdasarkan nama laundry
val priceList = mapOf(
    "Antony Laundry" to mapOf(
        "Cuci lipat" to 4500,
        "Cuci setrika" to 8500,
        "selimut" to 10000,
        "sprei" to 12500,
        "topi" to 10000,
        "sepatu" to 7500
    ),
    "Jasjus Laundry" to mapOf(
        "Cuci lipat" to 5000,
        "Cuci setrika" to 12000,
        "selimut" to 20000,
        "sprei" to 22500,
        "topi" to 12500,
        "sepatu" to 8000
    ),
    "Kiyomasa Laundry" to mapOf(
        "Cuci lipat" to 4000,
        "Cuci setrika" to 9000,
        "selimut" to 15000,
        "sprei" to 12500,
        "topi" to 10000,
        "sepatu" to 6500
    ),
    "Bersih Laundry" to mapOf(
        "Cuci lipat" to 6000,
        "Cuci setrika" to 10000,
        "selimut" to 25000,
        "sprei" to 15000,
        "topi" to 6500,
        "sepatu" to 8000
    ),
    "Cuci Cepat" to mapOf(
        "Cuci lipat" to 5500,
        "Cuci setrika" to 9500,
        "selimut" to 12000,
        "sprei" to 9000,
        "topi" to 5500,
        "sepatu" to 8500
    ),
    "Laundry Sehat" to mapOf(
        "Cuci lipat" to 7000,
        "Cuci setrika" to 15000,
        "selimut" to 13000,
        "sprei" to 11500,
        "topi" to 5000,
        "sepatu" to 7500
    )
)

const val ANTAR_JEMPUT_COST = 10000
const val EXPRESS_COST = 10000

// Fungsi untuk menghitung subtotal
fun calculateSubtotal(layanan: Map<String, Int>, namaLaundry: String): Int {
    val laundryPrices = priceList[namaLaundry] ?: return 0
    return layanan.entries.sumOf { (layananName, jumlah) ->
        val unitPrice = laundryPrices[layananName] ?: 0
        unitPrice * jumlah
    }
}

// Fungsi untuk menghitung total harga
fun calculateTotal(order: Order): Int {
    val subtotal = calculateSubtotal(order.layanan, order.NamaLaundry)
    val biayaAntarJemput = if (order.isAntarJemput) ANTAR_JEMPUT_COST else 0
    val biayaExpress = if (order.isExpress) EXPRESS_COST else 0
    return subtotal + biayaAntarJemput + biayaExpress
}

// Fungsi untuk mengambil data order dari Firebase
fun fetchOrderData(
    orderId: String,
    onSuccess: (Order) -> Unit,
    onError: (String) -> Unit
) {
    val database = FirebaseDatabase.getInstance()
    val orderRef = database.getReference("orders").child(orderId)

    orderRef.get().addOnSuccessListener { snapshot ->
        val order = snapshot.getValue(Order::class.java)
        if (order != null) {
            onSuccess(order)
        } else {
            onError("Order tidak ditemukan.")
        }
    }.addOnFailureListener { error ->
        onError(error.message ?: "Terjadi kesalahan saat mengambil data.")
    }
}
