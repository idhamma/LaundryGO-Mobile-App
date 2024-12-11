package com.ikancipung.laundrygo.order

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.menu.Footer
import com.ikancipung.laundrygo.menu.NavigationItem
import androidx.navigation.NavController
import com.ikancipung.laundrygo.menu.ProfileSettingsScreen
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo

@Composable
fun myOrderPage(navController: NavController) {
    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            myOrder(navController)
        }
    }
}



@Composable
fun myOrder(navController: NavController){

    var historyWindow by remember { mutableStateOf(false) }

    val dummyOrders = listOf(
        Order(id = "1", LaundryName = "Antony Laundry", isCompleted = true, time = "10:00 AM", date = "2024-10-01", category = 1, process = 4, processTimes = listOf(
            Pair("10:00 AM", "Laundry diterima"),
            Pair("11:30 AM", "Laundry selesai ditimbang"),
            Pair("01:00 PM", "Pakaian selesai dicuci"),
            Pair("02:30 PM", "Pakaian sudah siap diambil")
        )),
        Order(id = "2", LaundryName = "Clean & Fresh", isCompleted = true, time = "11:30 AM", date = "2024-10-02", category = 2, process = 3, processTimes = listOf(
            Pair("11:30 AM", "Laundry diterima"),
            Pair("01:00 PM", "Laundry selesai ditimbang"),
            Pair("03:00 PM", "Pakaian sudah selesai dicuci")
        )),
        Order(id = "3", LaundryName = "Laundry Express", isCompleted = true, time = "09:15 AM", date = "2024-10-03", category = 1, process = 2, processTimes = listOf(
            Pair("09:15 AM", "Laundry diterima"),
            Pair("10:30 AM", "Pakaian selesai dicuci")
        )),
        Order(id = "4", LaundryName = "Quick Wash", isCompleted = true, time = "01:00 PM", date = "2024-10-04", category = 3, process = 1, processTimes = listOf(
            Pair("01:00 PM", "Laundry diterima")
        )),
        Order(id = "5", LaundryName = "Eco Laundry", isCompleted = false, time = "08:45 AM", date = "2024-10-05", category = 2, process = 0, processTimes = listOf(
            Pair("08:45 AM", "Laundry diterima")
        )),
        Order(id = "6", LaundryName = "Luxury Laundry", isCompleted = true, time = "02:30 PM", date = "2024-10-06", category = 3, process = 4, processTimes = listOf(
            Pair("02:30 PM", "Laundry diterima"),
            Pair("03:45 PM", "Laundry selesai ditimbang"),
            Pair("05:00 PM", "Pakaian selesai dicuci"),
            Pair("06:30 PM", "Pakaian sudah siap diambil")
        )),
        Order(id = "7", LaundryName = "Speedy Laundry", isCompleted = false, time = "03:00 PM", date = "2024-10-07", category = 1, process = 2, processTimes = listOf(
            Pair("03:00 PM", "Laundry diterima"),
            Pair("04:15 PM", "Pakaian selesai ditimbang")
        )),
        Order(id = "8", LaundryName = "The Laundry Room", isCompleted = true, time = "12:00 PM", date = "2024-10-08", category = 2, process = 3, processTimes = listOf(
            Pair("12:00 PM", "Laundry diterima"),
            Pair("01:30 PM", "Laundry selesai ditimbang"),
            Pair("03:00 PM", "Pakaian sudah selesai dicuci")
        )),
        Order(id = "9", LaundryName = "Fresh Start Laundry", isCompleted = false, time = "10:30 AM", date = "2024-10-09", category = 1, process = 1, processTimes = listOf(
            Pair("10:30 AM", "Laundry diterima")
        )),
        Order(id = "10", LaundryName = "City Laundry", isCompleted = true, time = "04:00 PM", date = "2024-10-10", category = 3, process = 4, processTimes = listOf(
            Pair("04:00 PM", "Laundry diterima"),
            Pair("05:30 PM", "Laundry selesai ditimbang"),
            Pair("07:00 PM", "Pakaian selesai dicuci"),
            Pair("08:30 PM", "Pakaian sudah siap diambil")
        ))
    )

    var expandedOrderId by remember { mutableStateOf<String?>(null) } // Melacak pesanan yang diperluas


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
                    val completedOrders = dummyOrders.filter { it.isCompleted }

                    items(completedOrders) { order ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .background(color = BlueLaundryGo, shape = RoundedCornerShape(8.dp))
                                .clickable(onClick = {navController.navigate("Rating")}),
                            verticalArrangement = Arrangement.Center
                        ) {
                            val categoryId = when (order.category) {
                                0 -> R.drawable.icon_category_order_0
                                1 -> R.drawable.icon_category_order_1
                                2 -> R.drawable.icon_category_order_2

                                else -> R.drawable.icon_category_order_0
                            }

                            Row(
                                modifier = Modifier
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = categoryId),
                                    contentDescription = "Laundry Icon",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .padding(end = 8.dp), // Menambahkan padding horizontal di sebelah kanan icon
                                    colorFilter = ColorFilter.tint(Color.White)
                                )

                                Column {
                                    Text(
                                        text = order.LaundryName,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(bottom = 2.dp) // Mengurangi jarak antara nama laundry dan waktu
                                    )

                                    Text(
                                        text = "${order.time}, ${order.date}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                            }


                        }
                    }


                }
            }else{
                LazyColumn {
                    val processingOrder = dummyOrders.filter { !it.isCompleted }

                    items(processingOrder) { order ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .background(color = BlueLaundryGo, shape = RoundedCornerShape(8.dp))
                                .clickable(onClick = {navController.navigate("Ordersum")}),
                            verticalArrangement = Arrangement.Center
                        ) {
                            val categoryId = when (order.category) {
                                0 -> R.drawable.icon_category_order_0
                                1 -> R.drawable.icon_category_order_1
                                2 -> R.drawable.icon_category_order_2

                                else -> R.drawable.icon_category_order_0
                            }
                            val processDescriptions = listOf(
                                "Pesanan telah diterima",
                                "Pakaian telah sampai di laundry",
                                "Pakaian telah selesai ditimbang",
                                "Pakaian masih dalam proses pencucian",
                                "Pesanan telah selesai",
                                "Pesanan menunggu dibayar",
                                "Pesanan sedang dikirim ke alamat tujuan",
                                "Pesanan telah selesai"
                            )

                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Image(
                                    painter = painterResource(id = categoryId),
                                    contentDescription = "Category Icon",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .padding(end = 8.dp),
                                    colorFilter = ColorFilter.tint(Color.White)
                                )

                                Column (
                                    modifier = Modifier.weight(1f)
                                ){
                                    Text(
                                        text = order.LaundryName,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )

                                    Text(
                                        text = "${processDescriptions[order.process]}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Image(
                                    painter = painterResource(id = R.drawable.icon_more),
                                    contentDescription = "More Icon",
                                    modifier = Modifier
                                        .size(28.dp)
                                        .padding(end = 8.dp)
                                        .clickable {
                                            expandedOrderId = if (expandedOrderId == order.id) null else order.id
                                        },
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                            }

                            if (expandedOrderId == order.id) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.LightGray)
                                        .padding(8.dp)
                                ) {
                                    order.processTimes.reversed().forEach { processTime ->
                                        Text(
                                            text = "${processTime.first} - ${processTime.second}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
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

//@Preview(showBackground = true)
//@Composable
//fun MyOrderPreview(){
//    myOrderPage()
//}