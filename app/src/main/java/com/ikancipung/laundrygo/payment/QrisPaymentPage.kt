package com.ikancipung.laundrygo.payment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo
import com.ikancipung.laundrygo.ui.theme.RedLaundryGo

@Composable
fun QrisPaymentScreen(navController: NavController) {
    val context = LocalContext.current

    // Countdown Timer
    var remainingTime by remember { mutableStateOf(5 * 60) } // 5 menit dalam detik

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            kotlinx.coroutines.delay(1000L) // Delay 1 detik
            remainingTime--
        }
    }

    val minutes = remainingTime / 60
    val seconds = remainingTime % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Header()

        Spacer(modifier = Modifier.height(24.dp))

        // Instruction Text
        Text(
            modifier = Modifier
                .padding(16.dp, 8.dp)
                .align(Alignment.Start),
            text = "Silakan lakukan\npembayaran",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        Text(
            modifier = Modifier
                .padding(16.dp, 0.dp)
                .align(Alignment.Start),
            text = "Anda dapat scan atau download QR di bawah ini",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Gambar QRIS dari Drawable
        Image(
            painter = painterResource(id = R.drawable.qris), // Ganti dengan ID drawable QRIS Anda
            contentDescription = "QRIS",
            modifier = Modifier
                .size(400.dp)
                .padding(16.dp)
        )

        // Teks untuk mendownload QRIS
        Row(
            modifier = Modifier
                .clickable {
                    // Implementasikan fungsi download di sini
                    Toast.makeText(context, "Download QRIS", Toast.LENGTH_SHORT).show()
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ikon Download
            Icon(
                painter = painterResource(id = R.drawable.download), // Ganti dengan ID drawable ikon download Anda
                contentDescription = "Download Icon",
                tint = Color.Black, // Sesuaikan warna ikon
                modifier = Modifier.size(20.dp) // Ukuran ikon
            )

            Spacer(modifier = Modifier.width(8.dp)) // Jarak antara ikon dan teks

            // Teks Download
            Text(
                text = "Download QRIS",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Countdown Timer
        Text(
            modifier = Modifier.padding(0.dp, 16.dp),
            text = String.format("Waktu tersisa: %02d:%02d", minutes, seconds),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = RedLaundryGo
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Button
        Button(
            onClick = {
                // Navigasi ke halaman lain
                // navController.navigate("confirmation_screen")  Ganti dengan route sesuai
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueLaundryGo)
        ) {
            Text(
                text = "Saya Sudah Bayar",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun QrisPaymentPreview() {
//    QrisPaymentScreen()
//}