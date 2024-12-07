package com.ikancipung.laundrygo.mitra

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DetailLaundryPage(name: String, imageUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gambar Laundry
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = "Laundry Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp)
        )

        // Nama Laundry
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Deskripsi Laundry (Dummy atau dari database jika ada)
        Text(
            text = "Alamat: Jl. Contoh No.123, Kota Laundry",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Rating: 4.5/5",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Layanan yang tersedia: Cuci Kering, Setrika, Antar Jemput",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun DetailLaundryPagePreview() {
    DetailLaundryPage(
        name = "Laundry A",
        imageUrl = "https://via.placeholder.com/300"
    )
}