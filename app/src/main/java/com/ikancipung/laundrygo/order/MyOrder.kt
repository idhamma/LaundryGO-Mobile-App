package com.ikancipung.laundrygo.order

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.menu.Footer
import com.ikancipung.laundrygo.menu.NavigationItem
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikancipung.laundrygo.menu.ProfileSettingsScreen
import com.ikancipung.laundrygo.menu.userId
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo

@Composable
fun myOrderPage(navController: NavController) {
    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            myOrder(navController)
        }
    }
}



@Composable
fun myOrder(navController: NavController) {
    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    var expandedOrderId by remember { mutableStateOf<String?>(null) }
    val database = FirebaseDatabase.getInstance()
    val ordersRef = database.getReference("orders")

    // Mendengarkan data Firebase
    LaunchedEffect(Unit) {

        val ordersQuery = FirebaseDatabase.getInstance().getReference("Orders")
            .orderByChild("IDUser")
            .equalTo(userId)  // Make sure this matches the user's ID in the database

        ordersQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = snapshot.children.mapNotNull { it.getValue(Order::class.java) }
                Log.d("FirebaseData", "Fetched orders: $orders")

                // You can now pass this data to your UI (e.g., LiveData, state, etc.)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Error fetching orders: $error")
            }
        })

        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedOrders = mutableListOf<Order>()
                Log.d("FirebaseData", "onDataChange: snapshot children count: ${snapshot.childrenCount}")

                snapshot.children.forEach { orderSnapshot ->
                    Log.d("FirebaseData", "Order ID: ${orderSnapshot.key}") // Log order ID to ensure data is being fetched
                    val order = orderSnapshot.getValue(Order::class.java)
                    if (order != null) {
                        // Log the status to check if the values are being parsed correctly
                        Log.d("FirebaseData", "Order status: ${order.Status}")
                        fetchedOrders.add(order)
                    }
                }
                orders = fetchedOrders
                Log.d("FirebaseData", "Fetched orders: $fetchedOrders")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Error: ${error.message}")
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(orders) { order ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .background(color = BlueLaundryGo, shape = RoundedCornerShape(8.dp))
                        .clickable { /* Tidak ada aksi utama */ },
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_category_order_1),
                            contentDescription = "Laundry Icon",
                            modifier = Modifier
                                .size(36.dp)
                                .padding(end = 8.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = order.NamaLaundry,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall
                            )

                            // Menampilkan status pertama (Pesanan diterima) dengan waktu pemesanan
                            Text(
                                text = "${formatTimestamp(order.WaktuPesan)} - Pesanan sudah diterima",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // Ikon titik tiga
                        Icon(
                            painter = painterResource(id = R.drawable.icon_more),
                            contentDescription = "More Icon",
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 8.dp)
                                .clickable {
                                    expandedOrderId = if (expandedOrderId == order.OrderID) null else order.OrderID
                                }
                        )
                    }

                    // Dropdown untuk notifikasi status
                    if (expandedOrderId == order.OrderID) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray)
                                .padding(8.dp)
                        ) {
                            val statusMessages = mapOf(
                                "isDone" to "Pesanan selesai",
                                "isInLaundry" to "Pakaian sedang dicuci",
                                "isPaid" to "Pesanan telah dibayar",
                                "isReceived" to "Pesanan sudah diterima",
                                "isSent" to "Pesanan sedang dikirim",
                                "isWashing" to "Pakaian sedang dicuci",
                                "isWeighted" to "Pakaian selesai ditimbang"
                            )

                            // Use safe calls or null checks to avoid NullPointerException
                            if (order.Status.isDone?.value == true) {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - ${statusMessages["isDone"]}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - Status not updated",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            if (order.Status.isInLaundry?.value == true) {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - ${statusMessages["isInLaundry"]}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - Status not updated",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            if (order.Status.isPaid?.value == true) {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - ${statusMessages["isPaid"]}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - Status not updated",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            if (order.Status.isReceived?.value == true) {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - ${statusMessages["isReceived"]}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - Status not updated",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            if (order.Status.isSent?.value == true) {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - ${statusMessages["isSent"]}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - Status not updated",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            if (order.Status.isWashing?.value == true) {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - ${statusMessages["isWashing"]}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - Status not updated",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            if (order.Status.isWeighted?.value == true) {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - ${statusMessages["isWeighted"]}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            } else {
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - Status not updated",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Log.d("Order Status", "Status: ${order.Status}")

                        }
                    }

                }
            }
        }
    }
}
//@Composable
//fun Footer() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//            .background(Color.White)
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Tombol Explore
//        NavigationItem(icon = Icons.Filled.Person, title = "Explore") {
//            // Tindakan yang akan dilakukan saat tombol di klik
//        }
//
//        // Tombol My Order
//        NavigationItem(icon = Icons.Filled.Person, title = "My Order") {
//            // Tindakan yang akan dilakukan saat tombol di klik
//        }
//
//        // Tombol Profile
//        NavigationItem(icon = Icons.Filled.Person, title = "Profile") {
//            // Tindakan yang akan dilakukan saat tombol di klik
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun MyOrderPreview(){
//    myOrderPage()
//}