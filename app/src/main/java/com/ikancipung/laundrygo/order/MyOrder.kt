package com.ikancipung.laundrygo.order

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.menu.Footer
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo
import java.text.SimpleDateFormat
import java.util.Locale

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

fun updateStatusTimeIfNeeded(orderId: String, statusName: String, statusDetail: LaundryStatusDetail) {
    if (statusDetail.value && statusDetail.time == null) {
        val currentTime = System.currentTimeMillis()
        val updates = mapOf(
            "Status/$statusName/time" to currentTime
        )
        val orderRef = FirebaseDatabase.getInstance().getReference("orders").child(orderId)
        orderRef.updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseData", "Set time for $statusName in order $orderId to $currentTime")
            } else {
                Log.e("FirebaseData", "Failed to set time for $statusName in order $orderId: ${task.exception?.message}")
            }
        }
    }
}

@Composable
fun myOrder(navController: NavController) {
    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    val database = FirebaseDatabase.getInstance()
    val ordersRef = database.getReference("orders")
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(uid) {
        if (uid != null) {
            ordersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val fetchedOrders = mutableListOf<Order>()
                    for (orderSnapshot in snapshot.children) {
                        val idPemesan = orderSnapshot.child("IDPemesan").getValue(String::class.java) ?: ""
                        if (idPemesan == uid) {
                            val orderId = orderSnapshot.key ?: continue
                            val order = Order()
                            order.id = orderId
                            order.LaundryName = orderSnapshot.child("LaundryName").getValue(String::class.java) ?: ""
                            order.LaundryName = orderSnapshot.child("NamaLaundry").getValue(String::class.java) ?: ""
                            order.AlamatLaundry = orderSnapshot.child("AlamatLaundry").getValue(String::class.java) ?: ""
                            order.AlamatPemesanan = orderSnapshot.child("AlamatPemesanan").getValue(String::class.java) ?: ""
                            order.CuciKiloanOption = orderSnapshot.child("CuciKiloanOption").getValue(String::class.java) ?: ""
                            order.IDPemesan = idPemesan
                            order.NamaLaundry = orderSnapshot.child("NamaLaundry").getValue(String::class.java) ?: ""
                            order.NamaPemesan = orderSnapshot.child("NamaPemesan").getValue(String::class.java) ?: ""
                            order.OrderID = orderSnapshot.child("OrderID").getValue(String::class.java) ?: ""
                            order.Pembayaran = orderSnapshot.child("Pembayaran").getValue(String::class.java) ?: ""
                            order.WaktuPesan = orderSnapshot.child("WaktuPesan").getValue(Long::class.java) ?: 0L
                            order.isAntarJemput = orderSnapshot.child("isAntarJemput").getValue(Boolean::class.java) ?: false
                            order.isExpress = orderSnapshot.child("isExpress").getValue(Boolean::class.java) ?: false

                            val ordersMap = mutableMapOf<String, OrderDetail>()
                            val ordersChild = orderSnapshot.child("Orders")
                            for (orderDetailSnapshot in ordersChild.children) {
                                val service = orderDetailSnapshot.child("Service").getValue(String::class.java) ?: ""
                                val price = orderDetailSnapshot.child("Price").getValue(String::class.java) ?: ""
                                val quantity = orderDetailSnapshot.child("Quantity").getValue(Int::class.java) ?: 0
                                val od = OrderDetail(Service = service, Price = price, Quantity = quantity)
                                ordersMap[orderDetailSnapshot.key ?: ""] = od
                            }
                            order.Orders = ordersMap

                            val statusSnapshot = orderSnapshot.child("Status")
                            val status = LaundryStatus(
                                isDone = LaundryStatusDetail(
                                    value = statusSnapshot.child("isDone").child("value").getValue(Boolean::class.java) ?: false,
                                    time = statusSnapshot.child("isDone").child("time").getValue(Long::class.java)
                                ),
                                isInLaundry = LaundryStatusDetail(
                                    value = statusSnapshot.child("isInLaundry").child("value").getValue(Boolean::class.java) ?: false,
                                    time = statusSnapshot.child("isInLaundry").child("time").getValue(Long::class.java)
                                ),
                                isPaid = LaundryStatusDetail(
                                    value = statusSnapshot.child("isPaid").child("value").getValue(Boolean::class.java) ?: false,
                                    time = statusSnapshot.child("isPaid").child("time").getValue(Long::class.java)
                                ),
                                isReceived = LaundryStatusDetail(
                                    value = statusSnapshot.child("isReceived").child("value").getValue(Boolean::class.java) ?: false,
                                    time = statusSnapshot.child("isReceived").child("time").getValue(Long::class.java)
                                ),
                                isSent = LaundryStatusDetail(
                                    value = statusSnapshot.child("isSent").child("value").getValue(Boolean::class.java) ?: false,
                                    time = statusSnapshot.child("isSent").child("time").getValue(Long::class.java)
                                ),
                                isWashing = LaundryStatusDetail(
                                    value = statusSnapshot.child("isWashing").child("value").getValue(Boolean::class.java) ?: false,
                                    time = statusSnapshot.child("isWashing").child("time").getValue(Long::class.java)
                                ),
                                isWeighted = LaundryStatusDetail(
                                    value = statusSnapshot.child("isWeighted").child("value").getValue(Boolean::class.java) ?: false,
                                    time = statusSnapshot.child("isWeighted").child("time").getValue(Long::class.java)
                                )
                            )
                            order.Status = status

                            // Update times if needed
                            updateStatusTimeIfNeeded(orderId, "isDone", status.isDone)
                            updateStatusTimeIfNeeded(orderId, "isInLaundry", status.isInLaundry)
                            updateStatusTimeIfNeeded(orderId, "isPaid", status.isPaid)
                            updateStatusTimeIfNeeded(orderId, "isReceived", status.isReceived)
                            updateStatusTimeIfNeeded(orderId, "isSent", status.isSent)
                            updateStatusTimeIfNeeded(orderId, "isWashing", status.isWashing)
                            updateStatusTimeIfNeeded(orderId, "isWeighted", status.isWeighted)

                            fetchedOrders.add(order)
                        }
                    }
                    orders = fetchedOrders
                    Log.d("FirebaseData", "Orders updated: $fetchedOrders")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseData", "Error fetching orders: ${error.message}")
                }
            })
        } else {
            Log.e("FirebaseAuth", "User is not logged in.")
            navController.navigate("Login")
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(orders) { order ->
                OrderCard(order, navController)
            }
        }
    }
}

fun getTrueStatuses(order: Order): List<Pair<String, Long?>> {
    val statusMapping = listOf(
        order.Status.isDone to "Pesanan selesai",
        order.Status.isSent to "Pesanan sedang dikirim",
        order.Status.isWashing to "Pakaian sedang dicuci",
        order.Status.isInLaundry to "Pakaian sedang di laundry",
        order.Status.isWeighted to "Pakaian selesai ditimbang",
        order.Status.isPaid to "Pesanan telah dibayar",
        order.Status.isReceived to "Pesanan sudah diterima"
    )

    val trueStatuses = statusMapping.mapNotNull { (detail, message) ->
        if (detail?.value == true) {
            val t = detail.time
            message to t
        } else null
    }

    return trueStatuses.sortedByDescending { it.second ?: Long.MIN_VALUE }
}

@Composable
fun OrderCard(order: Order, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    val trueStatuses = getTrueStatuses(order)
    val (currentStatusMessage, currentStatusTime) = if (trueStatuses.isNotEmpty()) {
        trueStatuses.first()
    } else {
        "Status not updated" to null
    }

    val displayTime = if (currentStatusTime != null) formatTimestampNotif(currentStatusTime) else "No timestamp available"

    // The blue rectangle container (not clickable anymore for expanding)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(color = BlueLaundryGo, shape = RoundedCornerShape(8.dp)),
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
                // Laundry name clickable: Navigate to the order page
                Text(
                    text = order.NamaLaundry,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.clickable {
                        // Navigate to the same order
                        navController.navigate("Ordersum/${order.id}")
                    }
                )

                Text(
                    text = "$displayTime - $currentStatusMessage",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // icon_more to toggle dropdown
            Image(
                painter = painterResource(id = R.drawable.icon_more),
                contentDescription = "More Options",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { expanded = !expanded },
                colorFilter = ColorFilter.tint(Color.White)
            )
        }

        if (expanded) {
            StatusList(order = order)
        }
    }
}

@Composable
fun StatusList(order: Order) {
    val trueStatuses = getTrueStatuses(order)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        if (trueStatuses.isEmpty()) {
            Text(
                text = "No updated status",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        } else {
            trueStatuses.forEach { (message, time) ->
                val displayTime = if (time != null) formatTimestampNotif(time) else "No timestamp available"
                val statusText = "$displayTime - $message"
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

fun formatTimestampNotif(time: Long): String {
    val date = java.util.Date(time)
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}