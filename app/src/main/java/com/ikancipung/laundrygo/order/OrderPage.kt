package com.ikancipung.laundrygo.order

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikancipung.laundrygo.profile.Profile
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo

@Composable
fun LaundryOrderScreen(
    navController: NavController,
    laundryName: String,
    services: List<String>,
    prices: List<String>
) {
    var isCuciKiloanExpanded by remember { mutableStateOf(false) }
    var isTempatTidurExpanded by remember { mutableStateOf(false) }
    var isAksesorisExpanded by remember { mutableStateOf(false) }

    // State for radio button selections
    var antarJemput by remember { mutableStateOf("Ya") }
    var tipeLaundry by remember { mutableStateOf("Regular") }
    var pembayaran by remember { mutableStateOf("Cash") }
    var selectedCuciKiloanOption by remember { mutableStateOf("") }

    // State for text fields
    var username by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    DisposableEffect(uid) {
        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users").child(uid)

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.let {
                        username = it.child("name").getValue(String::class.java) ?: ""
                        address = it.child("address").getValue(String::class.java) ?: ""
                        phoneNumber =
                            it.child("phoneNumber").getValue(String::class.java) ?: ""
                    }
                    isLoading = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        context, "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT
                    ).show()
                    isLoading = false
                }
            }

            userRef.addValueEventListener(listener)

            // Hapus listener saat composable dihancurkan
            onDispose {
                userRef.removeEventListener(listener)
            }
        } else {
            navController.navigate("Login")
        }

        onDispose { } // Dibutuhkan oleh DisposableEffect meskipun tidak ada tambahan logika
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Header()
        }

        item {
            Text("Nama")
            TextField(
                value = username,
                onValueChange = { username = it },
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFE4E4E4)),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text("Alamat")
            TextField(
                value = address,
                onValueChange = { address = it },
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFE4E4E4)),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Map")
                }
            )
        }

        item {
            Text("No. HP")
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFE4E4E4)),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            AccordionSection(
                title = "Cuci Kiloan",
                isExpanded = isCuciKiloanExpanded,
                onToggle = { isCuciKiloanExpanded = !isCuciKiloanExpanded }
            ) {
                Box(modifier = Modifier.background(Color.LightGray)) {
                    RadioButtonOption(
                        options = services.zip(prices)
                            .filter { it.first.contains("Cuci") }
                            .map { "${it.first} - ${it.second}" },
                        selectedOption = selectedCuciKiloanOption,
                        onOptionSelected = { selectedCuciKiloanOption = it }
                    )
                }
            }
        }

        item {
            AccordionSection(
                title = "Tempat Tidur",
                isExpanded = isTempatTidurExpanded,
                onToggle = { isTempatTidurExpanded = !isTempatTidurExpanded }
            ) {
                services.zip(prices)
                    .filter { it.first.contains("Selimut") || it.first.contains("Sprei") }
                    .forEach { (service, price) ->
                        QuantityOption(name = service, price = price)
                    }
            }
        }

        item {
            AccordionSection(
                title = "Aksesoris",
                isExpanded = isAksesorisExpanded,
                onToggle = { isAksesorisExpanded = !isAksesorisExpanded }
            ) {
                services.zip(prices)
                    .filter { it.first.contains("Topi") || it.first.contains("Sepatu") }
                    .forEach { (service, price) ->
                        QuantityOption(name = service, price = price)
                    }
            }
        }

        item {
            Text("Antar Jemput")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = antarJemput == "Ya", colors = RadioButtonColors(
                    selectedColor = BlueLaundryGo,
                    unselectedColor = Color.Black,
                    disabledSelectedColor = Color.Black,
                    disabledUnselectedColor = Color.Black
                ), onClick = { antarJemput = "Ya" })
                Text("Ya")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = antarJemput == "Tidak", colors = RadioButtonColors(
                        selectedColor = BlueLaundryGo,
                        unselectedColor = Color.Black,
                        disabledSelectedColor = Color.Black,
                        disabledUnselectedColor = Color.Black
                    ),
                    onClick = { antarJemput = "Tidak" })
                Text("Tidak")
            }
        }

        item {
            Text("Tipe Laundry")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipeLaundry == "Regular", colors = RadioButtonColors(
                        selectedColor = BlueLaundryGo,
                        unselectedColor = Color.Black,
                        disabledSelectedColor = Color.Black,
                        disabledUnselectedColor = Color.Black
                    ),
                    onClick = { tipeLaundry = "Regular" })
                Text("Regular")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = tipeLaundry == "Express", colors = RadioButtonColors(
                        selectedColor = BlueLaundryGo,
                        unselectedColor = Color.Black,
                        disabledSelectedColor = Color.Black,
                        disabledUnselectedColor = Color.Black
                    ),
                    onClick = { tipeLaundry = "Express" })
                Text("Express")
            }
        }

        item {
            Text("Pembayaran")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = pembayaran == "Cash", colors = RadioButtonColors(
                    selectedColor = BlueLaundryGo,
                    unselectedColor = Color.Black,
                    disabledSelectedColor = Color.Black,
                    disabledUnselectedColor = Color.Black
                ), onClick = { pembayaran = "Cash" })
                Text("Cash")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = pembayaran == "QRIS", colors = RadioButtonColors(
                    selectedColor = BlueLaundryGo,
                    unselectedColor = Color.Black,
                    disabledSelectedColor = Color.Black,
                    disabledUnselectedColor = Color.Black
                ), onClick = { pembayaran = "QRIS" })
                Text("QRIS")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = pembayaran == "Virtual Account", colors = RadioButtonColors(
                        selectedColor = BlueLaundryGo,
                        unselectedColor = Color.Black,
                        disabledSelectedColor = Color.Black,
                        disabledUnselectedColor = Color.Black
                    ),
                    onClick = { pembayaran = "Virtual Account" })
                Text("Virtual Account")
            }
        }

        item {
            Button(
                onClick = { /* Handle Order Submission */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BlueLaundryGo)
            ) {
                Text("Pesan")
            }
        }
    }
}



@Composable
fun AccordionSection(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.background(color = BlueLaundryGo)) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = title, style = MaterialTheme.typography.labelLarge, color = Color.White)
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
            if (isExpanded) {
                content()
            }
        }
    }
}

@Composable
fun LaundryOption(name: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name)
        Text(price)
    }
}

@Composable
fun QuantityOption(name: String, price: String) {
    var quantity by remember { mutableStateOf(0) } // State variable to persist and react to changes

    Box(modifier = Modifier.background(Color.LightGray)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(name)
                Text(price, style = MaterialTheme.typography.labelMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        if (quantity > 0) quantity -= 1 // Prevent negative values
                    }
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Minus")
                }
                Text(text = quantity.toString())
                IconButton(onClick = { quantity += 1 }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Plus")
                }
            }
        }
    }
}

@Composable
fun RadioButtonOption(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) }
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    colors = RadioButtonColors(
                        selectedColor = BlueLaundryGo,
                        unselectedColor = Color.Black,
                        disabledSelectedColor = Color.Black,
                        disabledUnselectedColor = Color.Black
                    ),
                    onClick = { onOptionSelected(option) })
                Text(option)
            }
        }
    }
}

@Composable
fun Header() {
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
            text = "Isi Data dan Pilih Layanan",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PrevLaundryOrder() {
//    LaundryOrderScreen()
//}