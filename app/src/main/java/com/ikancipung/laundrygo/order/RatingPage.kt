package com.ikancipung.laundrygo.order

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo

@Composable
fun RatingScreen(navController: NavController, orderId: String) {
    var rating by remember { mutableStateOf(1f) }

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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    // Back Icon
                    Image(
                        painter = painterResource(R.drawable.keluar),
                        contentDescription = "Copy",
                        modifier = Modifier
                            .size(24.dp) // Set the size of the image
                            .clickable { } // Add clickable functionality
                    )

                    // Title and Location
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f) // Center the column evenly within the row
                    ) {
                        Text(
                            text = order.NamaLaundry,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = order.AlamatLaundry,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Add a Spacer for alignment
                    Spacer(modifier = Modifier.width(48.dp)) // Matches the size of IconButton
                }
                // Order Details Section
                val displayTime = formatTimestampNotif(order.WaktuPesan)
                val doneTime = getDoneStatusTime(order)
                val formattedTime = doneTime?.let { formatTimestampNotif(it) } ?: "Time not available"

                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    InfoRow(label = "Waktu Pemesanan", value = displayTime)
                    InfoRow(label = "Waktu Selesai", value = formattedTime)
                    InfoRow(label = "Order ID", value = order.OrderID)
                    InfoRow(label = "Metode Pembayaran", value = order.Pembayaran)
                    InfoRow(label = "Biaya", value = "Rp." + total.toString())
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Rating Section
                Row() {
                    Text(
                        text = "Menyukai Laundry ini? ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Tambahkan ke favorit!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Blue,
                        modifier = Modifier.clickable { /* Handle favorite action */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StarRatingBar(
                        maxStars = 5,
                        rating = rating,
                        onRatingChanged = { rating = it }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dengan memberikan rating, anda dapat membantu meningkatkan pelayanan LaundryGO",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Laporkan Kendala!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Blue,
                    modifier = Modifier.clickable { /* Handle report issue */ }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Handle submit action
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text(text = "Submit", color = Color.White)
                }
            }
        }
    }
}

fun getDoneStatusTime(order: Order): Long? {
    val statusMapping = listOf(
        order.Status.isDone to "Pesanan selesai",  // Assuming `isDone` is of type `LaundryStatusDetail`
        order.Status.isSent to "Pesanan sedang dikirim",
        order.Status.isWashing to "Pakaian sedang dicuci",
        order.Status.isInLaundry to "Pakaian sedang di laundry",
        order.Status.isWeighted to "Pakaian selesai ditimbang",
        order.Status.isPaid to "Pesanan telah dibayar",
        order.Status.isReceived to "Pesanan sudah diterima"
    )

    // Find the first status where `value` is true and extract the `time` for that status
    val doneStatusTime = statusMapping
        .firstOrNull { it.first.value == true } // Find the first status where 'value' is true
        ?.first?.time // Return the time associated with that status

    return doneStatusTime
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

//Credit to imitiyaz125
@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    val density = LocalDensity.current.density
    val starSize = (12f * density).dp
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.Star
            val iconTintColor = if (isSelected) BlueLaundryGo else Color(0xF0E0E0E0)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onRatingChanged(i.toFloat())
                        }
                    )
                    .width(starSize).height(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PrevLaundryScreen() {
//    RatingScreen()
//}
