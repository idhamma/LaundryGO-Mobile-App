package com.ikancipung.laundrygo.menu

import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

@Composable
fun FavoriteLaundryScreen(
    navController: NavController
) {
    // State untuk menyimpan daftar laundry favorit
    var favoriteLaundryList by remember { mutableStateOf(listOf<Laundry>()) }

    // Mengambil data laundry favorit saat layar pertama kali dimuat
    LaunchedEffect(Unit) {
        fetchFavoriteLaundry { favorites ->
            favoriteLaundryList = favorites
        }
    }

    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
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
                Text(text = "Laundry Favorite")
                Text(text = " ")
            }
            if (favoriteLaundryList.isEmpty()) {
                // Jika daftar favorit kosong, tampilkan pesan
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Belum ada laundry favorit.", color = Color.Gray)
                }
            } else {
                // Menampilkan daftar laundry favorit
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(favoriteLaundryList) { laundry ->
                        LaundryCard(
                            laundry = laundry,
                            onClick = {
                                navController.navigate(
                                    "ProfileLaundry/${Uri.encode(laundry.name)}/${Uri.encode(laundry.address)}/${
                                        Uri.encode(
                                            laundry.imageUrl
                                        )
                                    }/${Uri.encode(laundry.description)}/${Uri.encode(laundry.hours)}/${
                                        Uri.encode(
                                            Gson().toJson(laundry.prices)
                                        )
                                    }/${Uri.encode(Gson().toJson(laundry.services))}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

// Komponen Card untuk menampilkan detail laundry
@Composable
fun LaundryCard(laundry: Laundry, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(data = laundry.imageUrl),
                contentDescription = "Laundry Logo",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = laundry.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = laundry.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Fungsi untuk mengambil daftar laundry favorit dari Firebase
fun fetchFavoriteLaundry(callback: (List<Laundry>) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val database = FirebaseDatabase.getInstance().reference

    if (userId != null) {
        val userFavoritesRef = database.child("users").child(userId).child("favorites")
        val laundryRef = database.child("laundry")

        userFavoritesRef.get().addOnSuccessListener { snapshot ->
            val favoriteNames = snapshot.children.mapNotNull { it.getValue(String::class.java) }

            laundryRef.get().addOnSuccessListener { laundrySnapshot ->
                val allLaundries =
                    laundrySnapshot.children.mapNotNull { it.getValue(Laundry::class.java) }
                val favoriteLaundries = allLaundries.filter { it.name in favoriteNames }
                callback(favoriteLaundries)
            }.addOnFailureListener {
                callback(emptyList())
            }
        }.addOnFailureListener {
            callback(emptyList())
        }
    } else {
        callback(emptyList())
    }
}