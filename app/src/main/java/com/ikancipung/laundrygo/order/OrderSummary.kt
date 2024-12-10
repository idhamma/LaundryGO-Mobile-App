package com.ikancipung.laundrygo.order

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TitleLaundryScreen(navController: NavController, orderId: String) {
    val context = LocalContext.current

    // State untuk menyimpan data pesanan
    var orderData by remember { mutableStateOf<Order?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Ambil data order dari Firebase
    LaunchedEffect(orderId) {
        fetchOrderData(
            orderId = orderId,
            onSuccess = { order ->
                orderData = order // Menyimpan data order ke state
                isLoading = false
            },
            onError = { error ->
                errorMessage = error // Menyimpan pesan kesalahan
                isLoading = false
            }
        )
    }

    if (isLoading) {
        // Tampilkan indikator loading
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        // Tampilkan pesan kesalahan
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = errorMessage ?: "Terjadi kesalahan.")
        }
    } else {
        // Tampilkan data pesanan
        orderData?.let { order ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
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

                // Rincian Pesanan
                Text("Rincian Pemesanan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Alamat: ${order.AlamatPemesanan}")
                Text("Waktu Pemesanan: ${formatTimestamp(order.WaktuPesan)}")
                Text("Antar Jemput: ${if (order.isAntarJemput) "Ya" else "Tidak"}")
                Text("Express: ${if (order.isExpress) "Ya" else "Tidak"}")

                Spacer(modifier = Modifier.height(16.dp))

                // Detail Layanan
                Text("Layanan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                order.layanan.forEach { (layananName, jumlah) ->
                    val unitPrice = priceList[order.NamaLaundry]?.get(layananName) ?: 0
                    val totalPrice = unitPrice * jumlah
                    ServiceRow(layananName, jumlah, unitPrice, totalPrice)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Total Harga
                Column {
                    SubtotalInfo("Subtotal", "Rp.${calculateSubtotal(order.layanan, order.NamaLaundry)}")
                    SubtotalInfo("Biaya Antar Jemput", if (order.isAntarJemput) "Rp.$ANTAR_JEMPUT_COST" else "Rp.0")
                    SubtotalInfo("Biaya Express", if (order.isExpress) "Rp.$EXPRESS_COST" else "Rp.0")
                    Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                    SubtotalInfo("Total", "Rp.${calculateTotal(order)}", isBold = true)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol Bayar
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

@Composable
fun ServiceRow(layanan: String, jumlah: Int, unitPrice: Int, totalPrice: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$layanan ($jumlah x Rp.$unitPrice)", fontSize = 14.sp, color = Color.Gray)
        Text(text = "Rp.$totalPrice", fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun SubtotalInfo(title: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (isBold) Color.Black else Color.Gray,
            fontSize = if (isBold) 16.sp else 14.sp
        )
        Text(
            text = value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (isBold) Color.Black else Color.Gray,
            fontSize = if (isBold) 16.sp else 14.sp
        )
    }
}

// Fungsi untuk memformat timestamp ke dalam format tanggal yang mudah dibaca
fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault())
    val date = java.util.Date(timestamp)
    return sdf.format(date)
}
