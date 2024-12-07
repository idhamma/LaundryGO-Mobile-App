package com.ikancipung.laundrygo.menu

import com.ikancipung.laundrygo.order.myOrder
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ikancipung.laundrygo.R

@Composable
fun HomepagePage(navController: NavController) {
    Scaffold(
        bottomBar = { Footer() }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Homepage()
        }
    }
}

@Composable
fun Homepage(){
    val ImageIdOutlet = listOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        // Tambahkan lebih banyak ID gambar sesuai kebutuhan
    )

    val ImageIdService = listOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        // Tambahkan lebih banyak ID gambar sesuai kebutuhan
    )




    Column  (

        modifier = Modifier.fillMaxSize().padding(8.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .height(48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Rating",
                modifier = Modifier.size(24.dp).padding(start = 0.dp),
                tint = Color.Black
            )

            Row {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Rating",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )

                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Rating",
                    modifier = Modifier.size(24.dp).padding(end = 0.dp),
                    tint = Color.Black
                )
            }

        }

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Laundry Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column (
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ){
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

            ImageLazyRow(imageResIds = ImageIdOutlet)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column (
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ){
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

            ImageLazyRow(imageResIds = ImageIdService)
        }



    }
}

@Composable
fun ImageLazyRow(imageResIds: List<Int>) {
    LazyRow {
        items(imageResIds) { imageResId ->
            Box(
                modifier = Modifier
                    .padding(8.dp) // Padding antar gambar
                    .height(60.dp)
                    .width(120.dp)
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Laundry Image",
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth() // Memastikan gambar memenuhi box
                )

                Text(
                    text = "lokasi",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun HomepagePreview(){
//    HomepagePage()
//}