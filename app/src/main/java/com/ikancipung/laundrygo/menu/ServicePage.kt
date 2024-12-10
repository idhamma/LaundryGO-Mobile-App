package com.ikancipung.laundrygo.menu


import android.net.Uri
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
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

@Composable
fun ServiceLaundryScreen(
    navController: NavController,
    serviceName: String // Parameter untuk layanan yang dipilih
) {
    var serviceLaundryList by remember { mutableStateOf(listOf<Laundry>()) }

    // Mengambil data laundry yang menyediakan layanan tertentu
    LaunchedEffect(serviceName) {
        fetchLaundriesByService(serviceName) { laundries ->
            serviceLaundryList = laundries
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
                Text(text = serviceName) // Menampilkan nama layanan
                Text(text = " ")
            }
            if (serviceLaundryList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Belum ada laundry dengan layanan ini", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(serviceLaundryList) { laundry ->
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


fun fetchLaundriesByService(serviceName: String, callback: (List<Laundry>) -> Unit) {
    val database = FirebaseDatabase.getInstance().reference.child("laundry")

    database.get().addOnSuccessListener { snapshot ->
        val allLaundries = snapshot.children.mapNotNull { it.getValue(Laundry::class.java) }
        val filteredLaundries = allLaundries.filter { serviceName in it.services }
        callback(filteredLaundries)
    }.addOnFailureListener {
        callback(emptyList())
    }
}
