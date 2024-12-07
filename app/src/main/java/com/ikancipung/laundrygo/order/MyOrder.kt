package com.ikancipung.laundrygo.order

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ikancipung.laundrygo.menu.Footer
import com.ikancipung.laundrygo.menu.NavigationItem
import com.ikancipung.laundrygo.menu.ProfileSettingsScreen

@Composable
fun myOrderPage() {
    Scaffold(
        bottomBar = { Footer() }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            myOrder()
        }
    }
}



@Composable
fun myOrder(){

    var historyWindow by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.fillMaxSize(),

    ) {

        Box(modifier = Modifier.height(24.dp))


        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .height(48.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = "Dalam Proses",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    historyWindow = false
                }
            )

            Spacer(modifier = Modifier.width(96.dp))

            Text(
                text = "Riwayat",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    historyWindow = true
                }
            )

        }

        Column {
            if(historyWindow){
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
            }else{
                LazyColumn {
                    items(listOf(
                        "Antony Laundry",
                        "Saat ini sedang dicuci"
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
            }
    }
}

@Composable
fun Footer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tombol Explore
        NavigationItem(icon = Icons.Filled.Person, title = "Explore") {
            // Tindakan yang akan dilakukan saat tombol di klik
        }

        // Tombol My Order
        NavigationItem(icon = Icons.Filled.Person, title = "My Order") {
            // Tindakan yang akan dilakukan saat tombol di klik
        }

        // Tombol Profile
        NavigationItem(icon = Icons.Filled.Person, title = "Profile") {
            // Tindakan yang akan dilakukan saat tombol di klik
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyOrderPreview(){
    myOrderPage()
}