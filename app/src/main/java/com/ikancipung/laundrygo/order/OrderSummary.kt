package com.ikancipung.laundrygo.order

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TitleLaundryScreen(navController: NavController, orderId: String) {
    var orderData by remember { mutableStateOf<Order?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var subtotal by remember { mutableStateOf(0) }
    var total by remember { mutableStateOf(0) }

    LaunchedEffect(orderId) {
        if (orderId.isNotEmpty()) {
            fetchOrderData(
                orderId = orderId,
                onSuccess = { order ->
                    orderData = order
                    // Hitung subtotal terlebih dahulu
                    calculateSubtotalFromFirebase(
                        orders = order.Orders,
                        namaLaundry = order.NamaLaundry,
                        onSuccess = { calculatedSubtotal ->
                            subtotal = calculatedSubtotal
                            // Setelah subtotal, hitung total
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
                // Header Information
                Text(
                    text = order.NamaLaundry,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Order ID: ${order.OrderID}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Waktu Pesanan: ${formatTimestamp(order.WaktuPesan)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Metode Pembayaran: ${order.Pembayaran}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Address Information
                Text("Alamat Pengambilan: ${order.AlamatPemesanan}")
                Text("Alamat Laundry: ${order.AlamatLaundry}")

                Spacer(modifier = Modifier.height(16.dp))

                // Order Details
                Text("Rincian Pesanan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
//                order.Orders.forEach { (key, detail) ->
//                    if (detail.Quantity > 0) { // Only show services with quantities > 0
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 4.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(
//                                text = "${detail.Service} (${detail.Quantity}x)",
//                                fontSize = 14.sp,
//                                color = Color.Gray
//                            )
//                            Text(
//                                text = "Rp.${detail.Price.toIntOrNull()?.times(detail.Quantity) ?: 0}",
//                                fontSize = 14.sp,
//                                color = Color.Black
//                            )
//                        }
//                    }
//                }
                order.Orders.filter { it.value.Quantity > 0 }.forEach { (key, detail) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${detail.Service} (${detail.Quantity}x)",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Rp.${detail.Price.toIntOrNull()?.times(detail.Quantity) ?: 0}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                // Price Calculation
                Text("Subtotal: Rp.${subtotal}")
                Text("Total: Rp.${total}", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Button
                Button(
                    onClick = { /* Implement payment logic */ },
                    modifier = Modifier.fillMaxWidth()
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


