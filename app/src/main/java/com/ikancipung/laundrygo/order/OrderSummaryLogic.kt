package com.ikancipung.laundrygo.order

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Daftar harga berdasarkan nama laundry
const val ANTAR_JEMPUT_COST = 10000
const val EXPRESS_COST = 10000

// Fungsi untuk menghitung subtotal
fun calculateSubtotalFromFirebase(
    orders: Map<String, OrderDetail>,
    namaLaundry: String,
    onSuccess: (Int) -> Unit,
    onError: (String) -> Unit
) {
    val database = FirebaseDatabase.getInstance()
    val laundryRef = database.getReference("laundry").child("laundry1") // Sesuaikan dengan node laundry Anda
    laundryRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val pricesSnapshot = snapshot.child("prices")
                val servicesSnapshot = snapshot.child("services")
                // Pastikan data prices dan services ada dan berbentuk list
                val prices = pricesSnapshot.children.mapNotNull { it.getValue(String::class.java)?.toIntOrNull() }
                val services = servicesSnapshot.children.mapNotNull { it.getValue(String::class.java) }
                if (prices.size == services.size) {
                    // Buat map layanan ke harga
                    val priceMap = services.zip(prices).toMap()
                    // Hitung subtotal
                    try {
                        val subtotal = orders.values.sumOf { detail ->
                            val unitPrice = detail.Price.toIntOrNull() ?: 0
                            unitPrice * detail.Quantity
                        }
                        onSuccess(subtotal)
                    } catch (e: Exception) {
                        onError("Terjadi kesalahan dalam penghitungan subtotal.")
                    }
                } else {
                    onError("Data prices dan services tidak cocok.")
                }
            } else {
                onError("Data laundry untuk $namaLaundry tidak ditemukan.")
            }
        }
        override fun onCancelled(error: DatabaseError) {
            onError("Gagal mengambil data harga: ${error.message}")
        }
    })
}

// Fungsi untuk menghitung total harga
fun calculateTotalFromFirebase(
    order: Order,
    onSuccess: (Int) -> Unit,
    onError: (String) -> Unit
) {
    calculateSubtotalFromFirebase(order.Orders, order.NamaLaundry, { subtotal ->
        val biayaAntarJemput = if (order.isAntarJemput) ANTAR_JEMPUT_COST else 0
        val biayaExpress = if (order.isExpress) EXPRESS_COST else 0
        val total = subtotal + biayaAntarJemput + biayaExpress

        println("DEBUG: Subtotal = $subtotal, Antar Jemput = $biayaAntarJemput, Express = $biayaExpress, Total = $total")

        onSuccess(total)
    }, { error ->
        println("DEBUG: Error in calculateTotalFromFirebase: $error")
        onError(error)
    })
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
        if (snapshot.exists()) {
            val order = snapshot.getValue(Order::class.java)
            if (order != null) {
                // Ambil nilai isAntarJemput dan isExpress dari Firebase
                order.isAntarJemput = snapshot.child("isAntarJemput").getValue(Boolean::class.java) ?: false
                order.isExpress = snapshot.child("isExpress").getValue(Boolean::class.java) ?: false
                onSuccess(order)
            } else {
                onError("Data order tidak valid.")
            }
        } else {
            onError("Order tidak ditemukan.")
        }
    }.addOnFailureListener { error ->
        onError("Terjadi kesalahan saat mengambil data: ${error.message}")
    }
}

// Fungsi untuk mengambil alamat laundry dari Firebase
fun fetchLaundryData(
    laundryId: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("laundry").child(laundryId).child("address")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            println("DEBUG: Snapshot exists = ${snapshot.exists()}")
            println("DEBUG: Snapshot value = ${snapshot.value}")

            val address = snapshot.getValue(String::class.java)
            if (address != null && address.isNotBlank()) {
                onSuccess(address)
            } else {
                onError("Alamat Laundry tidak ditemukan.")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError("Gagal mengambil data: ${error.message}")
        }
    })
}
