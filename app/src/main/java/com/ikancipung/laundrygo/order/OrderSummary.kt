package com.ikancipung.laundrygo.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo

@Composable
fun TitleLaundryScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Header Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(end = 20.dp)
        ) {
            // Back Button
            Image(
                painter = painterResource(R.drawable.keluar),
                contentDescription = "Back Button",
                modifier = Modifier.size(24.dp)
            )
//            Spacer(modifier = Modifier.width(8.dp))

            // Title and Subtitle Column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(0.1f)

            ) {
                Text(
                    text = "Antony Laundry",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    fontSize = 15.sp
                )
                Text(
                    text = "Klojen, Malang",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        // Other content remains the same
        Spacer(modifier = Modifier.height(16.dp))

        // Order Details Section
        Column(modifier = Modifier.fillMaxWidth()) {
            OrderInfoRow(title = "Waktu Pemesanan", value = "14 Oct 2024, 09:39")
            OrderInfoRow(title = "Order ID", value = "L-93V832NM102")
            OrderInfoRow(title = "Metode Pembayaran", value = "QRIS")

            Spacer(modifier = Modifier.height(16.dp))

            // Address
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_location_alamat),
                    contentDescription = "alamat",
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Jl. Green Andara Residences Blok B3 No. 19, Malang",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Image(
                    painter = painterResource(R.drawable.baseline_location_destination),
                    contentDescription = "destinasi",
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Jl. Sigura-gura II Blok C2 No. 20, Klojen",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // The rest of your UI (Order items, total price, etc.)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Rincian Pesanan",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OrderItemRow(item = "5 Kg", description = "Cuci Lipat", price = "Rp.22.500")
        OrderItemRow(item = "2 Pcs", description = "Sepatu", price = "Rp.40.000")

        // Price Details Section
        Spacer(modifier = Modifier.height(16.dp))

        Column {
            SubtotalInfo(title = "Subtotal", value = "Rp.62.500")
            SubtotalInfo(title = "Biaya Pengantaran", value = "Rp.10.000")
            SubtotalInfo(title = "Biaya Pemesanan", value = "Rp.7.500")
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            SubtotalInfo(title = "Total", value = "Rp.80.000", isBold = true)
        }

        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = AnnotatedString("Laporkan Kendala!"),
            onClick = { /* Handle click */ },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium.copy(color = BlueLaundryGo)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action Button
        Button(
            onClick = { /* Handle click */ },
            colors = ButtonDefaults.buttonColors(containerColor = BlueLaundryGo),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Bayar", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun OrderInfoRow(title: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 14.sp, // Atur ukuran font
            color = Color.Black,
            fontWeight = FontWeight.Bold // Gaya huruf tebal
        )
        Text(
            text = value,
            fontSize = if (isBold) 16.sp else 14.sp, // Ukuran font dinamis
            color = if (isBold) Color.Black else Color.Gray,
            fontWeight = FontWeight.Bold // Gaya huruf tebal untuk semua teks
        )
    }
}


@Composable
fun OrderItemRow(item: String, description: String, price: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = item,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Text(
            text = price,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
fun SubtotalInfo(title: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 14.sp, // Ukuran font tetap
            color = Color.Black,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal // Bold hanya untuk "Total"
        )
        Text(
            text = value,
            fontSize = if (isBold) 16.sp else 14.sp, // Ukuran font dinamis untuk "Total"
            color = if (isBold) Color.Black else Color.Gray,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal // Bold hanya untuk "Total"
        )
    }
}



//@Preview(showBackground = true)
//@Composable
//fun TitleLaundryPreview() {
//    TitleLaundryScreen()
//}
