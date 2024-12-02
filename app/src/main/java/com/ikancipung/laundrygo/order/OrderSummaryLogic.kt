//package com.ikancipung.laundrygo.order
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.ClickableText
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.google.firebase.database.*
//import com.ikancipung.laundrygo.R
//import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo
//
//private val database = FirebaseDatabase.getInstance().reference
//
//@Composable
//fun TitleLaundryScreen(orderId: String) {
//    var orderData by remember { mutableStateOf<OrderData?>(null) }
//
//    LaunchedEffect(orderId) {
//        println("Memulai pengambilan data untuk orderId: $orderId") // Log awal
//
//        database.child("orders").child(orderId)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        println("Data ditemukan: ${snapshot.value}") // Log saat data ditemukan
//                        try {
//                            orderData = snapshot.getValue(OrderData::class.java)
//                            println("Data berhasil diparsing: $orderData")
//                        } catch (e: Exception) {
//                            println("Error parsing data: ${e.message}")
//                        }
//                    } else {
//                        println("Data tidak ditemukan di Firebase.") // Log jika data tidak ada
//                        orderData = null
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    println("Firebase Error: ${error.message}") // Log jika terjadi error
//                    orderData = null
//                }
//            })
//    }
//
//
//    orderData?.let {
//        LaundryUI(orderData = it)
//    } ?: run {
//        Text(
//            text = "Data tidak ditemukan.",
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.bodyMedium
//        )
//    }
//}
//
//@Composable
//fun LaundryUI(orderData: OrderData) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Header
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Image(
//                painter = painterResource(R.drawable.keluar),
//                contentDescription = "Back Button",
//                modifier = Modifier.size(24.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Column {
//                Text(text = "Antony Laundry", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//                Text(text = "Klojen, Malang", fontSize = 12.sp, color = Color.Gray)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Order Info
//        OrderInfoRow(title = "Waktu Pemesanan", value = orderData.waktuPemesanan)
//        OrderInfoRow(title = "Order ID", value = orderData.orderId)
//        OrderInfoRow(title = "Metode Pembayaran", value = orderData.metodePembayaran)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Address
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Image(
//                painter = painterResource(R.drawable.baseline_location_alamat),
//                contentDescription = "Alamat",
//                modifier = Modifier.size(18.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = orderData.alamat,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                style = MaterialTheme.typography.bodySmall,
//                color = Color.Gray
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Image(
//                painter = painterResource(R.drawable.baseline_location_destination),
//                contentDescription = "Destinasi",
//                modifier = Modifier.size(18.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = orderData.destinasi,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                style = MaterialTheme.typography.bodySmall,
//                color = Color.Gray
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Order Details
//        Text(text = "Rincian Pesanan", fontWeight = FontWeight.Bold)
//        OrderItemRow(item = "- Kg", description = "Cuci Lipat", price = "-")
//        OrderItemRow(item = "2 Pcs", description = "Sepatu", price = "Rp.40.000")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Price Details
//        SubtotalInfo(title = "Subtotal", value = orderData.subtotal)
//        SubtotalInfo(title = "Biaya Pengantaran", value = orderData.biayaPengantaran)
//        SubtotalInfo(title = "Biaya Pemesanan", value = orderData.biayaPemesanan)
//        Divider(color = Color.Gray, thickness = 1.dp)
//        SubtotalInfo(title = "Total", value = orderData.total, isBold = true)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        ClickableText(
//            text = AnnotatedString("Laporkan Kendala!"),
//            onClick = { /* Handle */ },
//            style = MaterialTheme.typography.bodyMedium.copy(color = BlueLaundryGo)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Action Button
//        Button(
//            onClick = { /* Handle Payment */ },
//            enabled = orderData.isChecked,
//            colors = ButtonDefaults.buttonColors(
//                containerColor = if (orderData.isChecked) BlueLaundryGo else Color.Gray
//            ),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(
//                text = if (orderData.isChecked) "Bayar" else "Pakaian Belum Ditimbang",
//                color = Color.White,
//                fontSize = 16.sp
//            )
//        }
//    }
//}
//
//@Composable
//fun OrderInfoRow(title: String, value: String) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(text = title, fontWeight = FontWeight.Bold)
//        Text(text = value, color = Color.Gray)
//    }
//}
//
//@Composable
//fun OrderItemRow(item: String, description: String, price: String) {
//    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//        Text(text = "$item $description")
//        Text(text = price)
//    }
//}
//
//@Composable
//fun SubtotalInfo(title: String, value: String, isBold: Boolean = false) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(text = title, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
//        Text(text = value, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
//    }
//}
//
//// Data Model
//data class OrderData(
//    val alamat: String = "",
//    val biayaPemesanan: String = "",
//    val biayaPengantaran: String = "",
//    val destinasi: String = "",
//    val isChecked: Boolean = false,
//    val metodePembayaran: String = "",
//    val orderId: String = "",
//    val subtotal: String = "",
//    val total: String = "",
//    val waktuPemesanan: String = ""
//)
