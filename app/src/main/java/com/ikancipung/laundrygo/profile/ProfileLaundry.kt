package com.ikancipung.laundrygo.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

import com.ikancipung.laundrygo.R

@Composable
fun ProfileLaundry(
    navController: NavController? = null, // NavController opsional untuk preview
    laundryName: String,
    laundryAddress: String,
    laundryRating: String,
    laundryLogo: String, // Ubah dari resource ID menjadi URL
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
                    .clickable { navController?.popBackStack() }, // Navigasi kembali
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
            AsyncImage(
                model = laundryLogo, // URL gambar dari Firebase
                contentDescription = "Laundry Image",
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
            // Menampilkan Layanan
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

            // Menampilkan Harga dengan Satuan yang Tepat
            LazyColumn {
                itemsIndexed(prices) { index, price ->
                    // Mengambil unit yang sesuai berdasarkan service dan price
                    val priceWithUnit = getPriceUnit(services[index], price)

                    Text(
                        text = priceWithUnit,
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
            onClick = { navController.navigate("Orderpage") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(Color.Blue)
        ) {
            Text(text = "Pesan Sekarang!", color = Color.White)
        }
    }
}

@Composable
fun getPriceUnit(service: String, price: String): String {
    return when (service) {
        "Cuci Lipat", "Cuci Setrika", "Cuci Express", -> "$price/kg"
        "Cuci Selimut", "Cuci Sepatu","CUci Karpet" -> "$price/pcs"
        else -> price
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewProfileLaundry() {
//    ProfileLaundry(
//        navController = rememberNavController(),
//        laundryName = "Antony Laundry",
//        laundryAddress = "Klojen, Malang",
//        laundryRating = "5 Stars",
//        laundryLogo = R.drawable.antony_laundry, // Contoh logo lokal
//        services = listOf(
//            "Cuci Lipat",
//            "Cuci Setrika",
//            "Cuci Express",
//            "Cuci Selimut",
//            "Cuci Sepatu"
//        ),
//        prices = listOf(
//            "4.500/kg",
//            "8.500/kg",
//            "10.000/kg",
//            "12.500/pcs",
//            "20.000/pair"
//        ),
//        serviceHours = "08.00 - 18.00 WIB",
//        laundryDescription = "Laundry terpercaya dengan layanan berkualitas tinggi."
//    )
//}

