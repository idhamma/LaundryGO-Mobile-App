package com.ikancipung.laundrygo.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ikancipung.laundrygo.R

@Composable
fun ProfileLaundry() {
    var laundryName by remember { mutableStateOf("") }
    var laundryAddress by remember { mutableStateOf("") }
    var laundryRating by remember { mutableStateOf("") }

    laundryName = "Antony Laundry"
    laundryAddress =  "Klojen, Malang"
    laundryRating = "5 Stars"


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
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
                modifier = Modifier.size(24.dp).
                padding(start = 0.dp),

                )

            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = laundryName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text =laundryAddress,
                )
            }

            Text(
                text = laundryRating,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier){
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Laundry Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = "Rating",
                modifier = Modifier.size(24.dp)
                    .align(Alignment.BottomEnd),
                tint = Color.Blue
            )
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Layanan",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
        Row{
            LazyColumn {
                items(listOf(
                    "Cuci Lipat",
                    "Cuci Setrika",
                    "Cuci Express",
                    "Cuci Selimut",
                    "Cuci Sepatu"
                )) { service ->
                    Text(
                        text = service,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp).padding(vertical = 4.dp)
                    )
                }
            }

            LazyColumn {
                items(listOf(
                    "4.500/kg",
                    "8.500/kg",
                    "0.000/kg",
                    "12.500/pcs",
                    "20.000/pair"
                )) { service ->
                    Text(
                        text = service,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp).padding(vertical = 4.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Jam Pelayanan",

            fontWeight = FontWeight.Bold
        )
        Text(
            text = "08.00 - 18.00 WIB",
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp).padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
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
fun PreviewLaundryApp() {
    ProfileLaundry()
}