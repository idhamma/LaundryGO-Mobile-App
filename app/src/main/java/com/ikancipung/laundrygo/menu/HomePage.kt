package com.ikancipung.laundrygo.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@Composable
fun HomepagePage(navController: NavController) {
    Scaffold(
        bottomBar = { Footer() }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Homepage(navController)
        }
    }
}

@Composable
fun Homepage(navController: NavController) {
    val laundries = remember { mutableStateListOf<Laundry>() }

    // Fetch data from Firebase
    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().getReference("laundries")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                laundries.clear()
                for (child in snapshot.children) {
                    val name = child.child("name").value.toString()
                    val imageUrl = child.child("imageUrl").value.toString()
                    laundries.add(Laundry(name, imageUrl))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

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
                            // Navigate to detail page with the selected laundry
                            navController.navigate("laundryDetail/${laundry.name}")
                        }
                ) {
                    Image(
                        painter = rememberImagePainter(data = laundry.imageUrl),
                        contentDescription = "Laundry Image",
                        modifier = Modifier.fillMaxSize()
                    )
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

data class Laundry(val name: String, val imageUrl: String)

