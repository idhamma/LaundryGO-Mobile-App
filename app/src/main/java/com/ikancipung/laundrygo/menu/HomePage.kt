package com.ikancipung.laundrygo.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun HomepagePage(navController: NavController) {
    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Homepage(navController)
        }
    }
}

@Composable
fun Homepage(navController: NavController, viewModel: HomepageViewModel = viewModel()) {
    val laundries: List<Laundry> by viewModel.laundries.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Row {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(24.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        // Section: Outlet
        Text(
            text = "Outlet",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = "Laundry Terdekat dengan Anda",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // LazyRow for displaying laundries
        LazyRow(modifier = Modifier.padding(top = 8.dp)) {
            items(laundries) { laundry ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(120.dp)
                        .width(120.dp)
                        .clickable {
                            // Navigasi ke halaman ProfileLaundry dengan data laundry
                            navController.navigate(
                                "Profilelaundry/${laundry.name}/" +
                                        "${laundry.address}/" +
                                        "${laundry.imageUrl}/" +
                                        "${laundry.services.joinToString(",")}/" +
                                        "${laundry.prices.joinToString(",")}/" +
                                        "${laundry.hours}/" +
                                        "${laundry.description}"
                            )
                        }
                ) {
                    // Display logo for each laundry
                    Image(
                        painter = painterResource(id = laundry.logoResourceId),
                        contentDescription = "Laundry Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                    // Overlay text for name
                    Text(
                        text = laundry.name,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }
}

data class Laundry(
    val name: String,
    val imageUrl: String,
    val logoResourceId: Int,
    val address: String,
    val services: List<String>,
    val prices: List<String>,
    val hours: String,
    val description: String
)
