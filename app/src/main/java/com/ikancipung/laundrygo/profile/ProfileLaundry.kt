package com.ikancipung.laundrygo.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ikancipung.laundrygo.R

@Composable
fun ProfileLaundry(
    navController: NavController,
    laundryName: String,
    laundryAddress: String,
    laundryRating: String,
    laundryLogo: Int, // Gambar logo lokal menggunakan resource ID
    services: List<String>,
    prices: List<String>,
    serviceHours: String,
    laundryDescription: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
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
                    text = laundryName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = laundryAddress,
                )
            }

            Text(
                text = laundryRating,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        // Gambar Laundry
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier) {
            Image(
                painter = painterResource(id = laundryLogo),
                contentDescription = "Laundry Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = "Rating",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.BottomEnd),
                tint = Color.Blue
            )
        }

        // Nama dan Deskripsi
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
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )

        // Layanan
        Text(
            text = "Layanan",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Row {
            LazyColumn {
                items(services) { service ->
                    Text(
                        text = service,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(vertical = 4.dp)
                    )
                }
            }

            LazyColumn {
                items(prices) { price ->
                    Text(
                        text = price,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

        // Jam Pelayanan
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Jam Pelayanan",
            fontWeight = FontWeight.Bold
        )
        Text(
            text = serviceHours,
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Tombol Pesan
        Button(
            onClick = { /* Handle booking action */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(Color.Blue)
        ) {
            Text(text = "Pesan Sekarang!", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileLaundry() {
    ProfileLaundry(
        navController = rememberNavController(),
        laundryName = "Antony Laundry",
        laundryAddress = "Klojen, Malang",
        laundryRating = "5 Stars",
        laundryLogo = R.drawable.antony_laundry, // Contoh logo lokal
        services = listOf(
            "Cuci Lipat",
            "Cuci Setrika",
            "Cuci Express",
            "Cuci Selimut",
            "Cuci Sepatu"
        ),
        prices = listOf(
            "4.500/kg",
            "8.500/kg",
            "10.000/kg",
            "12.500/pcs",
            "20.000/pair"
        ),
        serviceHours = "08.00 - 18.00 WIB",
        laundryDescription = "Laundry terpercaya dengan layanan berkualitas tinggi."
    )
}
