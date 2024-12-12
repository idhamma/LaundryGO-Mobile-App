package com.ikancipung.laundrygo.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.ui.theme.GreenPrimary
import com.ikancipung.laundrygo.ui.theme.GreenLight
import com.ikancipung.laundrygo.ui.theme.WhiteText

class DonebayarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply {
            setContent {
                MaterialTheme {
                    // Tidak digunakan jika navigasi Jetpack Compose digunakan sepenuhnya
                }
            }
        })
    }
}

@Composable
fun DonebayarScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = GreenLight // Menggunakan warna hijau muda dari Color.kt
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val checkIcon = painterResource(id = R.drawable.checklist)

            // Gambar Checklist
            Image(
                painter = checkIcon,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            // Teks "Pembayaran Berhasil"
            Text(
                text = "Pembayaran Berhasil",
                fontSize = 24.sp,
                color = GreenPrimary,
                modifier = Modifier.padding(top = 16.dp)
            )


            // Tombol Kembali ke Beranda
            Button(
                onClick = { navController.navigate("Homepage") },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(0.5f),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text(
                    text = "Kembali ke Beranda",
                    color = WhiteText
                )
            }
        }
    }
}
