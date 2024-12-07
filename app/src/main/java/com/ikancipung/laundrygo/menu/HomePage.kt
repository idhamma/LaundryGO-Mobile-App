package com.ikancipung.laundrygo.menu

import com.ikancipung.laundrygo.order.myOrder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikancipung.laundrygo.R

@Composable
fun HomepagePage(navController: NavController) {
    val outlets = remember { mutableStateListOf<Laundry>() }
    val services = remember { mutableStateListOf<Laundry>() }
    val database = FirebaseDatabase.getInstance().getReference("laundry")

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                outlets.clear()
                services.clear()
                for (data in snapshot.children) {
                    val laundry = data.getValue(Laundry::class.java)
                    if (laundry != null) {
                        outlets.add(laundry) // Anggap outlets dan services sama
                        services.add(laundry) // Contoh: gunakan service yang sama
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
        Box(modifier = Modifier.padding(innerPadding)) {
            Homepage(outlets, services)
        }
    }
}

@Composable
fun Homepage(outlets: List<Laundry>, services: List<Laundry>) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth()
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
                    contentDescription = "Favorites",
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

        // Banner Image
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Laundry Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Outlet Section
        Column(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
            Text(
                text = "Outlet",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Laundry Terdekat dengan Anda",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            ImageLazyRow(dataList = outlets)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Services Section
        Column(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
            Text(
                text = "Layanan",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Layanan Laundry untuk Anda",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            ImageLazyRow(dataList = services)
        }
    }
}

@Composable
fun ImageLazyRow(dataList: List<Laundry>) {
    LazyRow {
        items(dataList) { item ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(60.dp)
                    .width(120.dp)
            ) {
                Image(
                    painter = rememberImagePainter(item.imageUrl),
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                )

                Text(
                    text = item.name,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
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

//@Preview(showBackground = true)
//@Composable
//fun HomepagePreview(){
//    HomepagePage()
//}