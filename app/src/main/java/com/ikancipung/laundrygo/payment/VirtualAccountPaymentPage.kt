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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ThumbUp
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikancipung.laundrygo.R

class VirtualAccountPaymentPage {

    @Composable
    fun PaymentScreen() {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            header()

            Spacer(modifier = Modifier.height(32.dp))

            // Instruction Text
            Text(
                text = "Silahkan lakukan pembayaran",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Lakukan pembayaran ke",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Virtual Account Section
            PaymentInfoRow(
                label = "BNI Virtual Account",
                value = "97999 0812345678910",
                onCopy = { copyToClipboard(context, "97999 0812345678910") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Price Section
            PaymentInfoRow(
                label = "Harga",
                value = "80.000",
                onCopy = { copyToClipboard(context, "80000") }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Payment Status Message
            Text(
                text = "Status pembayaran akan terupdate jika sudah melakukan pembayaran",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }

    @Composable
    fun PaymentInfoRow(label: String, value: String, onCopy: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFEFEFEF),
                        shape = RoundedCornerShape(8.dp)
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
                        text = value,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // Clickable Image for Copy
                    Image(
                        painter = painterResource(R.drawable.baseline_content_copy_24),
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
    fun header() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Icon on the left
            IconButton(
                onClick = { },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Left")
            }

            // Text in the center
            Text(
                text = "Pembayaran",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun VirtualAccountPaymentPreview(){
        PaymentScreen()
    }
}