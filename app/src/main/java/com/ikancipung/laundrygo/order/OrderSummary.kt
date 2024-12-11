package com.ikancipung.laundrygo.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ikancipung.laundrygo.R

@Composable
fun TitleLaundryScreen(navController: NavController, orderId: String) {
    var orderData by remember { mutableStateOf<Order?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var subtotal by remember { mutableStateOf(0) }
    var total by remember { mutableStateOf(0) }
    var userAddress by remember { mutableStateOf("") }

    LaunchedEffect(orderId) {
        if (orderId.isNotEmpty()) {
            fetchOrderData(
                orderId = orderId,
                onSuccess = { order ->
                    orderData = order
                    userAddress = order.AlamatPemesanan // Ambil alamat pengambilan dari order
                    calculateSubtotalFromFirebase(
                        orders = order.Orders,
                        namaLaundry = order.NamaLaundry,
                        onSuccess = { calculatedSubtotal ->
                            subtotal = calculatedSubtotal
                            calculateTotalFromFirebase(
                                order = order,
                                onSuccess = { calculatedTotal ->
                                    total = calculatedTotal
                                    isLoading = false
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
                // Header Row with Back Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.NamaLaundry,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Order Information
                Text(
                    text = "Order ID: ${order.OrderID}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Waktu Pesanan: ${formatTimestamp(order.WaktuPesan)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Metode Pembayaran: ${order.Pembayaran}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Address Information
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

                // Order Details
                Text("Rincian Pesanan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        order.Orders.filter { it.value.Quantity > 0 }.forEach { (key, detail) ->
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
                                    text = "Rp.${detail.Price.toIntOrNull()?.times(detail.Quantity) ?: 0}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price Calculation
                Text("Subtotal: Rp.${subtotal}", fontSize = 14.sp, color = Color.Gray)
                if (order.isAntarJemput) {
                    Text("Biaya Antar Jemput: Rp.10000", fontSize = 14.sp, color = Color.Gray)
                }

                if (order.isExpress) {
                    Text("Biaya Express: Rp.10000", fontSize = 14.sp, color = Color.Gray)
                }
                Text("Total: Rp.${total}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Button
                Button(
                    onClick = { /* Implement payment logic */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
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