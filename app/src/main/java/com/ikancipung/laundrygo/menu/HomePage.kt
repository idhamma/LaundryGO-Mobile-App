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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo


@Composable
fun HomepagePage(navController: NavController) {
    val outlets = remember { mutableStateListOf<Laundry>() }
    val services = remember { mutableStateListOf<Service>() }
    val database = FirebaseDatabase.getInstance().getReference("laundry")
    val databaseService = FirebaseDatabase.getInstance().getReference("Services")

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                outlets.clear()
                services.clear()
                for (data in snapshot.children) {
                    val laundry = data.getValue(Laundry::class.java)
                    if (laundry != null) {
                        outlets.add(laundry)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    LaunchedEffect(Unit) {
        databaseService.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                services.clear()
                for (data in snapshot.children) {
                    val service = data.getValue(Service::class.java)
                    if (service != null) {
                        services.add(service)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())) {
            Homepage(outlets, services, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(
    outlets: List<Laundry>,
    services: List<Service>,
    navController: NavController
) {
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredOutlets = outlets.filter { it.name.contains(searchQuery, ignoreCase = true) }
    val filteredServices = services.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Header with Search Icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSearchActive) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(text = "Cari di sini", color = Color.Gray)
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(24.dp)
                        .weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.laundrygo2_1),
                    contentDescription = "LaundryGo",
                    modifier = Modifier
                        .size(56.dp),
                )
            }

            Row {
                Icon(
                    imageVector = if (isSearchActive) Icons.Filled.Close else Icons.Filled.Search,
                    contentDescription = "Search",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            isSearchActive = !isSearchActive
                            if (!isSearchActive) {
                                searchQuery = "" // Reset search query ketika search bar ditutup
                            }
                        },
                    tint = Color.Black
                )
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorites",
                    modifier = Modifier
                        .padding(8.dp, 0.dp)
                        .size(24.dp)
                        .clickable {
                            navController.navigate("FavLaundry")
                        },
                    tint = Color.Black
                )
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }

        val bannerUrls = remember { mutableStateListOf<String>() }
        val bannerDatabase = FirebaseDatabase.getInstance().getReference("Banner/Banner")

        LaunchedEffect(Unit) {
            bannerDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bannerUrls.clear()
                    for (data in snapshot.children) {
                        val imageUrl = data.value.toString()
                        bannerUrls.add(imageUrl)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database fetch error
                }
            })
        }

        if (bannerUrls.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bannerUrls) { imageUrl ->
                    Card(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .aspectRatio(16f / 9f)// Ini memastikan elemen memenuhi layar
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Image(
                            painter = rememberImagePainter(data = imageUrl),
                            contentDescription = "Banner Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop // Gambar akan memenuhi Card dan memotong sisi yang tidak sesuai
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Memuat spanduk...", color = Color.White)
            }
        }

        // Outlet Section
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Gerai",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Laundry Terdekat dengan Anda",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ImageLazyRow(dataList = filteredOutlets, navController = navController)
        }

        // Services Section
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Layanan",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Layanan Laundry untuk Anda",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ImageLazyRowService(dataList = filteredServices, navController = navController)
        }
    }
}

@Composable
fun ImageLazyRow(dataList: List<Laundry>, navController: NavController) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dataList) { laundry ->
            Card(
                modifier = Modifier
                    .width(150.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(8.dp),
                        clip = false // Agar shadow tidak terpotong oleh bentuk kartu
                    )
                    .clickable {
                        // Navigasi ke halaman profil laundry
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
                    },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF3F3F3)
                )
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberImagePainter(laundry.imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp, 8.dp, 8.dp, 0.dp)
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = laundry.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = laundry.address,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun ImageLazyRowService(dataList: List<Service>, navController: NavController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dataList) { service ->
            Card(
                modifier = Modifier
                    .width(120.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(8.dp),
                        clip = false // Agar shadow tidak terpotong oleh bentuk kartu
                    )
                    .clickable {
                        navController.navigate("ServiceLaundryScreen/${Uri.encode(service.name)}")
                    },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF3F3F3)
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberImagePainter(service.imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp, 8.dp, 8.dp, 0.dp)
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


// Model class for Laundry
data class Laundry(
    val name: String = "",
    val address: String = "",
    val description: String = "",
    val hours: String = "",
    val imageUrl: String = "",
    val prices: List<String> = listOf(),
    val services: List<String> = listOf()
)

data class Banner(
    val imageUrl1: String = "",
    val imageUrl2: String = ""
)

data class Service(
    val name: String = "",
    val imageUrl: String = ""
)

//@Preview(showBackground = true)
//@Composable
//fun HomepagePreview(){
//    HomepagePage()
//}