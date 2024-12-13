package com.ikancipung.laundrygo.order

import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.menu.userId
import com.ikancipung.laundrygo.profile.checkFavoriteStatus
import com.ikancipung.laundrygo.profile.getLaundryNodeName
import com.ikancipung.laundrygo.profile.toggleFavorite
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo

@Composable
fun RatingScreen(navController: NavController, orderId: String) {
    var rating by remember { mutableStateOf(0f) } // Nilai rating default
    var isSubmitting by remember { mutableStateOf(false) } // Status saat submit

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
    var isFavorite by remember { mutableStateOf(false) }

    // Ambil data pesanan berdasarkan `orderId`
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

                                    checkFavoriteStatus(order.NamaLaundry) { favorite ->
                                        isFavorite = favorite
                                    }
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
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.keluar),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() }
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f) // Center the column evenly within the row
                    ) {
                        Text(
                            text = order.NamaLaundry,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                val displayTime = formatTimestampNotif(order.WaktuPesan)
                var formattedTime by remember { mutableStateOf("") }

                LaunchedEffect(order.OrderID) {
                    val doneTimeRef = database.child("orders").child(order.OrderID).child("Status").child("isDone").child("time")
                    doneTimeRef.get().addOnSuccessListener { dataSnapshot ->
                        val doneTime = dataSnapshot.value as? Long ?: 0L
                        formattedTime = formatTimestampNotif(doneTime)
                    }.addOnFailureListener {
                        Log.e("Firebase", "Error getting doneTime", it)
                        formattedTime = "Belum selesai"
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    InfoRow(label = "Waktu Pemesanan", value = displayTime)
                    InfoRow(label = "Waktu Selesai", value = if (formattedTime.isNotEmpty()) formattedTime else "Loading...")
                    InfoRow(label = "Order ID", value = order.OrderID)
                    InfoRow(label = "Metode Pembayaran", value = order.Pembayaran)
                    InfoRow(label = "Biaya", value = "Rp." + total.toString())
                }

                Row() {
                    Text(
                        text = "Menyukai Laundry ini? ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Tambahkan ke favorit!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            toggleFavorite(
                                laundryName = order.NamaLaundry,
                                isCurrentlyFavorite = isFavorite,
                                navController = navController,
                                callback = { updatedFavorite ->
                                    isFavorite = updatedFavorite
                                }
                            )
                        }
                    )
                }

                // Rating Section
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Komponen StarRatingBar untuk memilih bintang
                    StarRatingBar(
                        maxStars = 5,
                        rating = rating,
                        onRatingChanged = { selectedRating ->
                            rating = selectedRating
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Dengan memberikan rating, Anda membantu meningkatkan pelayanan LaundryGO.",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (isSubmitting) {
                    CircularProgressIndicator() // Indikator loading saat submit
                } else {
                    Button(
                        onClick = {
                            isSubmitting = true
                            saveRatingToDatabase(
                                laundryName = order.NamaLaundry,
                                rating = rating.toInt()
                            ) {
                                isSubmitting = false
                                navController.popBackStack() // Kembali setelah submit berhasil
                            }
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
    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (isSelected) BlueLaundryGo else Color(0xFFE0E0E0),
                modifier = Modifier
                    .clickable { onRatingChanged(i.toFloat()) }
                    .size(32.dp) // Ukuran ikon bintang
            )
        }
    }
}

// Fungsi untuk menyimpan rating ke database
fun saveRatingToDatabase(
    laundryName: String,
    rating: Int,
    onSuccess: () -> Unit = {}
) {
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid ?: "unknown_user"
    val laundryNodeName = getLaundryNodeName(laundryName)

    val database = FirebaseDatabase.getInstance().reference
    val ratingRef = database.child("laundry").child(laundryNodeName).child("Rating")

    ratingRef.child(userId).setValue(rating)
        .addOnSuccessListener {
            Log.d("Firebase", "Rating berhasil disimpan")
            onSuccess()
        }
        .addOnFailureListener { error ->
            Log.e("Firebase", "Gagal menyimpan rating: ${error.message}", error)
        }
}
//a
//@Preview(showBackground = true)
//@Composable
//fun PrevLaundryScreen() {
//    RatingScreen()
//}
