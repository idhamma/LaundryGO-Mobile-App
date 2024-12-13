package com.ikancipung.laundrygo.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo
import java.util.Locale

@Composable
fun ProfileLaundry(
    navController: NavController,
    laundryName: String,
    laundryAddress: String,
    laundryLogo: String,
    services: List<String>,
    prices: List<String>,
    serviceHours: String,
    laundryDescription: String
) {
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        checkFavoriteStatus(laundryName) { favorite ->
            isFavorite = favorite
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() },
                        tint = Color.Black
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = laundryName, fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = laundryAddress,
                        )


                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Gambar Laundry
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier) {
                    Image(
                        painter = rememberImagePainter(laundryLogo),
                        contentDescription = "Laundry Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Rating",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                            .clickable {
                                toggleFavorite(
                                    laundryName,
                                    isFavorite,
                                    navController
                                ) { updatedFavorite ->
                                    isFavorite = updatedFavorite
                                }
                            },
                        tint = BlueLaundryGo
                    )
                }
            }

            // Nama dan Deskripsi
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Nama Laundry",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = laundryName,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )

                Text(
                    text = "Deskripsi",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = laundryDescription,
                    textAlign = TextAlign.Justify,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }

            // Layanan
            item {
                Text(
                    text = "Layanan",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(services.size) { index ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = services[index],
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )
                    Text(
                        text = getPriceUnit(services[index], prices[index]),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )
                }
            }

            // Jam Pelayanan
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Jam Pelayanan", fontWeight = FontWeight.Bold
                )
                Text(
                    text = serviceHours,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(vertical = 4.dp)
                )
            }

            // Tombol Pesan
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate(
                            "Orderpage/${Uri.encode(laundryName)}/${
                                Uri.encode(
                                    Gson().toJson(prices)
                                )
                            }/${Uri.encode(Gson().toJson(services))}"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(BlueLaundryGo)
                ) {
                    Text(text = "Pesan Sekarang!", color = Color.White)
                }
            }
        }

        // Floating ChatScreen
        ChatScreen(
            navController = navController,
            laundryName = laundryName,
            laundryLogo = laundryLogo,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(16.dp),
                    clip = false
                )
        )
    }
}

@Composable
fun ChatScreen(navController: NavController, laundryName: String, laundryLogo: String, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = {
            navController.navigate("chat/${Uri.encode(laundryName)}/${Uri.encode(laundryLogo)}")
        },
        modifier = modifier,
        containerColor = Color(0xFFF3F3F3),
        elevation = FloatingActionButtonDefaults.elevation(0.dp)
    ) {
        Image(painter = painterResource(id = R.drawable.chat), contentDescription = "Chat")
    }
}

@Composable
fun getPriceUnit(service: String, price: String): String {
    val serviceLower = service.toLowerCase(Locale.getDefault())

    return when (serviceLower) {
        "cuci lipat", "cuci setrika", "cuci express", "setrika" -> "$price/kg"
        "cuci selimut", "cuci karpet", "cuci topi", "cuci sprei" -> "$price/pcs"
        "cuci sepatu" -> "$price/pasang"
        else -> price
    }
}

// Fungsi untuk mengecek status favorit
fun checkFavoriteStatus(laundryName: String, callback: (Boolean) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val database = FirebaseDatabase.getInstance().reference

    if (userId != null) {
        val userFavoritesRef = database.child("users").child(userId).child("favorites")
        userFavoritesRef.get().addOnSuccessListener { snapshot ->
            val currentFavorites =
                snapshot.children.mapNotNull { it.getValue(String::class.java) }
            callback(currentFavorites.contains(laundryName))
        }.addOnFailureListener {
            callback(false) // Jika gagal memuat data, asumsikan bukan favorit
        }
    } else {
        callback(false) // Pengguna tidak terautentikasi
    }
}

// Fungsi untuk toggle status favorit
fun toggleFavorite(
    laundryName: String,
    isCurrentlyFavorite: Boolean,
    navController: NavController,
    callback: (Boolean) -> Unit
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val database = FirebaseDatabase.getInstance().reference

    if (userId != null) {
        val userFavoritesRef = database.child("users").child(userId).child("favorites")

        userFavoritesRef.get().addOnSuccessListener { snapshot ->
            val currentFavorites =
                snapshot.children.mapNotNull { it.getValue(String::class.java) }.toMutableList()

            if (isCurrentlyFavorite) {
                // Hapus dari favorit
                currentFavorites.remove(laundryName)
            } else {
                // Tambahkan ke favorit
                currentFavorites.add(laundryName)
            }

            userFavoritesRef.setValue(currentFavorites).addOnSuccessListener {
                callback(!isCurrentlyFavorite)
            }.addOnFailureListener {
                Toast.makeText(
                    navController.context,
                    "Gagal memperbarui favorit",
                    Toast.LENGTH_SHORT
                ).show()
                callback(isCurrentlyFavorite)
            }
        }.addOnFailureListener {
            Toast.makeText(
                navController.context,
                "Gagal memuat data favorit",
                Toast.LENGTH_SHORT
            ).show()
            callback(isCurrentlyFavorite)
        }
    } else {
        Toast.makeText(
            navController.context,
            "Pengguna tidak terautentikasi",
            Toast.LENGTH_SHORT
        ).show()
        callback(isCurrentlyFavorite)
    }
}

// Fungsi untuk mendapatkan nama node laundry berdasarkan nama laundry
fun getLaundryNodeName(laundryName: String): String {
    return when (laundryName) {
        "Antony Laundry" -> "laundry1"
        "Jasjus Laundry" -> "laundry2"
        "Kiyomasa Laundry" -> "laundry3"
        "Bersih Laundry" -> "laundry4"
        "Cuci Cepat" -> "laundry5"
        "Laundry Sehat" -> "laundry6"
        else -> "unknown_laundry"
    }
}


