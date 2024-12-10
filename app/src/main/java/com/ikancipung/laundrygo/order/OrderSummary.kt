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
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TitleLaundryScreen(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val orderId = backStackEntry?.arguments?.getString("orderId") ?: ""

    var orderData by remember { mutableStateOf<Order?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(orderId) {
        if (orderId.isNotEmpty()) {
            fetchOrderData(
                orderId = orderId,
                onSuccess = { order ->
                    orderData = order
                    isLoading = false
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
                Text(
                    text = order.NamaLaundry,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Pemesan: ${order.NamaPemesan}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Rincian Pemesanan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Alamat: ${order.AlamatPemesanan}")
                Text("Waktu Pesan: ${formatTimestamp(order.WaktuPesan)}")
                Text("Antar Jemput: ${if (order.isAntarJemput) "Ya" else "Tidak"}")
                Text("Express: ${if (order.isExpress) "Ya" else "Tidak"}")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Detail Layanan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                order.Orders.forEach { (key, detail) ->
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

                Text("Status Pesanan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Selesai: ${if (order.Status.isDone) "Ya" else "Tidak"}")
                Text("Sedang di Laundry: ${if (order.Status.isInLaundry) "Ya" else "Tidak"}")
                Text("Sudah Dibayar: ${if (order.Status.isPaid) "Ya" else "Tidak"}")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Logika pembayaran */ },
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


