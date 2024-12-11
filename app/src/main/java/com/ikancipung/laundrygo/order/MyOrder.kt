package com.ikancipung.laundrygo.order

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
        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedOrders = mutableListOf<Order>()
                snapshot.children.forEach { orderSnapshot ->
                    val order = orderSnapshot.getValue(Order::class.java)
                    if (order != null) {
                        fetchedOrders.add(order)
                    }
                }
                orders = fetchedOrders
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani error
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

                            val statuses = order.Status::class.java.declaredFields.map {
                                it.isAccessible = true
                                it.name to it.get(order.Status) as Boolean
                            }.filter { it.second }

                            statuses.forEach { (status, _) ->
                                Text(
                                    text = "${formatTimestamp(order.WaktuPesan)} - ${statusMessages[status]}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun Footer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tombol Explore
        NavigationItem(icon = Icons.Filled.Person, title = "Explore") {
            // Tindakan yang akan dilakukan saat tombol di klik
        }

        // Tombol My Order
        NavigationItem(icon = Icons.Filled.Person, title = "My Order") {
            // Tindakan yang akan dilakukan saat tombol di klik
        }

        // Tombol Profile
        NavigationItem(icon = Icons.Filled.Person, title = "Profile") {
            // Tindakan yang akan dilakukan saat tombol di klik
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MyOrderPreview(){
//    myOrderPage()
//}