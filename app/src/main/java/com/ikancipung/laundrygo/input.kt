package com.ikancipung.laundrygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.database.FirebaseDatabase

class Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Panggil fungsi untuk menambahkan node laundries
        addLaundryNode()
    }

    private fun addLaundryNode() {
        val database = FirebaseDatabase.getInstance().getReference("laundry")

        val laundries = mapOf(
            "laundry4" to mapOf(
                "name" to "Bersih Laundry",
                "imageUrl" to "https://example.com/bersih_laundry.jpg",
                "address" to "Jl. Merdeka No. 5, Yogyakarta",
                "services" to listOf("Cuci Lipat", "Cuci Setrika", "Cuci Karpet"),
                "prices" to listOf("6.000/kg", "10.000/kg", "25.000/m2"),
                "hours" to "08.00 - 17.00 WIB",
                "description" to "Layanan laundry yang ramah lingkungan dan berkualitas."
            ),

            "laundry5" to mapOf(
                "name" to "Cuci Cepat",
                "imageUrl" to "https://example.com/cuci_cepat.jpg",
                "address" to "Jl. Sudirman No. 12, Medan",
                "services" to listOf("Cuci Lipat", "Cuci Setrika", "Cuci Baju"),
                "prices" to listOf("5.500/kg", "9.500/kg", "12.000/kg"),
                "hours" to "24 Jam",
                "description" to "Layanan laundry cepat dan terpercaya, buka 24 jam."
            ),

            "laundry6" to mapOf(
                "name" to "Laundry Sehat",
                "imageUrl" to "https://example.com/laundry_sehat.jpg",
                "address" to "Jl. Siliwangi No. 8, Bandung",
                "services" to listOf("Cuci Lipat", "Cuci Selimut", "Cuci Bantal"),
                "prices" to listOf("7.000/kg", "15.000/pcs", "18.000/pcs"),
                "hours" to "09.00 - 21.00 WIB",
                "description" to "Laundry dengan produk ramah lingkungan dan aman."
            )
        )

        database.setValue(laundries).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Node laundries berhasil dibuat!")
            } else {
                println("Gagal membuat node laundries: ${task.exception}")
            }
        }
    }
}