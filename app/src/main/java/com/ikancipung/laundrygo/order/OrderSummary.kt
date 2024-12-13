package com.ikancipung.laundrygo.order

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ikancipung.laundrygo.R

@Composable
fun TitleLaundryScreen(navController: NavController, orderId: String) {
    var orderData by remember { mutableStateOf<Order?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var subtotal by remember { mutableStateOf(0) }
    var total by remember { mutableStateOf(0) }
    var userAddress by remember { mutableStateOf("") }
    var isBayarEnabled by remember { mutableStateOf(false) }
    var pembayaran by remember { mutableStateOf("") }

    val statusName = "isWeighted" // We explicitly use isWeighted as statusName
    val database = Firebase.database.reference

    LaunchedEffect(orderId) {
        if (orderId.isNotEmpty()) {
            fetchOrderData(
                orderId = orderId,
                onSuccess = { order ->
                    orderData = order
                    userAddress = order.AlamatPemesanan
                    pembayaran = order.Pembayaran
                    calculateSubtotalFromFirebase(
                        orders = order.Orders,
                        namaLaundry = order.NamaLaundry,
                        onSuccess = { calculatedSubtotal ->
                            subtotal = calculatedSubtotal
                            calculateTotalFromFirebase(
                                order = order,
                                onSuccess = { calculatedTotal ->
                                    total = calculatedTotal
                                    // Once we have all order data loaded, now we read Status/$statusName/value
                                    val valueRef =
                                        database.child("orders").child(orderId).child("Status")
                                            .child(statusName).child("value")
                                    valueRef.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val weightedValue =
                                                snapshot.getValue(Boolean::class.java) ?: false
                                            isBayarEnabled = weightedValue
                                            isLoading = false
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            errorMessage =
                                                "Gagal membaca status $statusName: ${error.message}"
                                            isLoading = false
                                        }
                                    })
                                },
                                onError = { error ->
                                    errorMessage = error
                                    isLoading = false
                                }
                            )
                        },
                        onError = { error ->
                            errorMessage = error
                            isLoading = false
                        }
                    )
                },
                onError = { error ->
                    errorMessage = error
                    isLoading = false
                }
            )
        } else {
            errorMessage = "Order ID tidak ditemukan."
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = errorMessage ?: "Terjadi kesalahan.")
        }
    } else {
        orderData?.let { order ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() },
                        tint = Color.Black
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = order.NamaLaundry,
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(end = 96.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Order ID:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "${order.OrderID}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Waktu Pesanan:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "${formatTimestamp(order.WaktuPesan)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Metode Pembayaran:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = if (order.Pembayaran == "cash") "Tunai"
                            else if (order.Pembayaran == "Virtual Account") "Akun Virtual"
                            else order.Pembayaran,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_location_alamat),
                        contentDescription = "Alamat Pengambilan",
                        tint = Color(0xFF6438E2),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Alamat Pengambilan: ${orderData?.AlamatPemesanan ?: "Tidak tersedia"}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_location_destination),
                        contentDescription = "Alamat Laundry",
                        tint = Color(0xFFE2000B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Alamat Laundry: ${order.AlamatLaundry}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Rincian Pesanan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        order.Orders.filter { it.value.Quantity > 0 }.forEach { (_, detail) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${detail.Service} (${detail.Quantity}x)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Rp.${
                                        detail.Price.toIntOrNull()?.times(detail.Quantity) ?: 0
                                    }",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal:", fontSize = 14.sp, color = Color.Gray)
                        Text("Rp.$subtotal", fontSize = 14.sp, color = Color.Gray)
                    }

                    if (order.isAntarJemput) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Biaya Antar Jemput:", fontSize = 14.sp, color = Color.Gray)
                            Text("Rp.10000", fontSize = 14.sp, color = Color.Gray)
                        }
                    }

                    if (order.isExpress) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Biaya Kilat:", fontSize = 14.sp, color = Color.Gray)
                            Text("Rp.10000", fontSize = 14.sp, color = Color.Gray)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            "Rp.$total",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Now isBayarEnabled is obtained from reading "Status/$statusName/value"
                Button(
                    onClick = {
                        if (isBayarEnabled) {
                            val donebayarValue = total
                            val laundryName = order.NamaLaundry
                            val targetNode = when (laundryName) {
                                "Antony Laundry" -> "laundry1"
                                "Jasjus Laundry" -> "laundry2"
                                "Kiyomasa Laundry" -> "laundry3"
                                "Bersih Laundry" -> "laundry4"
                                "Cuci Cepat" -> "laundry5"
                                "Laundry Sehat" -> "laundry6"
                                else -> null
                            }

                            if (targetNode != null) {
                                val donebayarRef =
                                    database.child("laundry").child(targetNode).child("donebayar")
                                donebayarRef.get().addOnSuccessListener { snapshot ->
                                    val orderCount = snapshot.childrenCount
                                    val newOrderKey = "Order ${(orderCount + 1)}"
                                    if (pembayaran.equals("QRIS")) {
                                        donebayarRef.child(newOrderKey).setValue(donebayarValue)
                                            .addOnSuccessListener {
                                                navController.navigate("Qris/${Uri.encode(orderId)}")
                                            }
                                    }
                                    if (pembayaran.equals("Virtual Account")) {
                                        donebayarRef.child(newOrderKey).setValue(donebayarValue)
                                            .addOnSuccessListener {
                                                navController.navigate(
                                                    "Vapayment/${
                                                        Uri.encode(
                                                            orderId
                                                        )
                                                    }/${Uri.encode(total.toString())}"
                                                )
                                            }
                                    }
                                    if (pembayaran.equals("Cash")) {
                                        val statusRef =
                                            database.child("orders").child(orderId).child("Status")
                                                .child("isPaid")

                                        statusRef.child("value").setValue(true)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    navController.navigate("Donebayar")
                                                }
                                            }
                                    }
                                }.addOnFailureListener {
                                    navController.navigate("Donebayar")
                                }
                            } else {
                                navController.navigate("Donebayar")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = isBayarEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBayarEnabled) Color(0xFF007BFF) else Color.Gray
                    )
                ) {
                    Text(text = "Bayar", color = Color.White)
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
    val date = java.util.Date(timestamp)
    return sdf.format(date)
}