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

@Composable
fun VAPaymentScreen(navController: NavController) {

    val context = LocalContext.current

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
            text = "Lakukan pembayaran ke",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Virtual Account Section
        VAPaymentInfoRow(label = "BNI Virtual Account",
            value = "97999 0812345678910",
            onCopy = { copyToClipboard(context, "97999 0812345678910") })

        Spacer(modifier = Modifier.height(8.dp))

        // Price Section
        VAPaymentInfoRow(label = "Harga",
            value = "80.000",
            onCopy = { copyToClipboard(context, "80000") })

        Spacer(modifier = Modifier.height(24.dp))

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

@Composable
fun VAPaymentInfoRow(label: String, value: String, onCopy: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
    ) {
        Text(
            modifier = Modifier.padding(0.dp, 8.dp),
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFEFEFEF), shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Value Text
                Text(
                    text = value, style = MaterialTheme.typography.bodyMedium
                )
                // Clickable Image for Copy
                Image(painter = painterResource(R.drawable.baseline_content_copy_24),
                    contentDescription = "Copy",
                    modifier = Modifier
                        .size(24.dp) // Set the size of the image
                        .clickable { onCopy() } // Add clickable functionality
                )
            }
        }
    }
}


fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
}

@Composable
fun Header() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Icon on the left
        IconButton(
            onClick = { }, modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Left",
                tint = BlueLaundryGo
            )
        }

        // Text in the center
        Text(
            text = "Pembayaran",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun VirtualAccountPaymentPreview() {
//    VAPaymentScreen()
//}
