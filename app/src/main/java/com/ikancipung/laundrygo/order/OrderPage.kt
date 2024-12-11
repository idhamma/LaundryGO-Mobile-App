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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
    var pembayaran by remember { mutableStateOf("Cash") } // Track selected payment method
    var selectedCuciKiloanOption by remember { mutableStateOf("") } // Track selected Cuci Kiloan option

    // State for text fields
    var username by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // State for service quantities
    val serviceQuantities = remember { mutableStateMapOf<String, Int>() }
    services.forEach { service ->
        serviceQuantities[service] = 0 // Initialize the quantity for each service
    }

//    val currentUser = FirebaseAuth.getInstance().currentUser
//    val uid = currentUser?.uid
//    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
//    val database = FirebaseDatabase.getInstance()
    val database = FirebaseDatabase.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val context = LocalContext.current

    DisposableEffect(uid) {
        if (uid != null) {
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

            // Remove listener on dispose
            onDispose {
                userRef.removeEventListener(listener)
            }
        } else {
            navController.navigate("Login")
        }

        onDispose { } // Required by DisposableEffect
    }

    // Handle order submission
    fun submitOrder() {
        val orderId = database.reference.push().key ?: return

        val filteredOrders = serviceQuantities.filter { it.value > 0 }.map { (service, quantity) ->
            service to mapOf(
                "Service" to service,
                "Price" to prices[services.indexOf(service)],
                "Quantity" to quantity
            )
        }.toMap().toMutableMap()

        if (selectedCuciKiloanOption.isNotEmpty()) {
            val serviceName = selectedCuciKiloanOption.split(" - ")[0]
            val servicePrice = selectedCuciKiloanOption.split(" - ")[1].toInt()
            filteredOrders[serviceName] = mapOf(
                "Service" to serviceName,
                "Price" to servicePrice.toString(),
                "Quantity" to 1
            )
        }

        val orderData = mapOf(
            "OrderID" to orderId,
            "NamaLaundry" to laundryName,
            "NamaPemesan" to username, // Ambil nama dari form
            "AlamatPemesanan" to address, // Ambil alamat dari form
            "IDPemesan" to uid,
            "AlamatLaundry" to laundryName,
            "WaktuPesan" to System.currentTimeMillis(),
            "Orders" to filteredOrders,
            "isAntarJemput" to (antarJemput == "Ya"),
            "isExpress" to (tipeLaundry == "Express"),
            "Pembayaran" to pembayaran,
            "Status" to mapOf(
                "isReceived" to mapOf("value" to false, "time" to null),
                "isInLaundry" to mapOf("value" to false, "time" to null),
                "isWeighted" to mapOf("value" to false, "time" to null),
                "isPaid" to mapOf("value" to false, "time" to null),
                "isWashing" to mapOf("value" to false, "time" to null),
                "isSent" to mapOf("value" to false, "time" to null),
                "isDone" to mapOf("value" to false, "time" to null)
            ),
            "CuciKiloanOption" to selectedCuciKiloanOption
        )

        val orderRef = database.getReference("orders").child(orderId)
        orderRef.setValue(orderData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate("Ordersum/$orderId")
            } else {
                Toast.makeText(context, "Gagal menambahkan order", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Header(navController) }

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

        // Add Accordion sections for services
        item {
            AccordionSection(
                title = "Cuci Kiloan",
                isExpanded = isCuciKiloanExpanded,
                onToggle = { isCuciKiloanExpanded = !isCuciKiloanExpanded }
            ) {
                Box(modifier = Modifier.background(Color.LightGray)) {
                    // Filter hanya untuk layanan "Cuci Lipat" dan "Cuci Setrika"
                    RadioButtonOption(
                        options = services.zip(prices)
                            .filter { it.first.contains("Cuci Lipat") || it.first.contains("Cuci Setrika") }
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
                        QuantityOption(
                            name = service,
                            price = price,
                            onQuantityChange = { quantity ->
                                serviceQuantities[service] = quantity
                            }
                        )
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
                        QuantityOption(
                            name = service,
                            price = price,
                            onQuantityChange = { quantity ->
                                serviceQuantities[service] = quantity
                            }
                        )
                    }
            }
        }

        item {
            Text("Antar Jemput")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = antarJemput == "Ya",
                    onClick = { antarJemput = "Ya" },
                    colors = RadioButtonDefaults.colors(selectedColor = BlueLaundryGo)
                )
                Text("Ya")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = antarJemput == "Tidak",
                    onClick = { antarJemput = "Tidak" },
                    colors = RadioButtonDefaults.colors(selectedColor = BlueLaundryGo)
                )
                Text("Tidak")
            }
        }

        item {
            Text("Tipe Laundry")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = tipeLaundry == "Regular",
                    onClick = { tipeLaundry = "Regular" },
                    colors = RadioButtonDefaults.colors(selectedColor = BlueLaundryGo)
                )
                Text("Regular")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = tipeLaundry == "Express",
                    onClick = { tipeLaundry = "Express" },
                    colors = RadioButtonDefaults.colors(selectedColor = BlueLaundryGo)
                )
                Text("Express")
            }
        }

        item {
            Text("Pembayaran")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = pembayaran == "Cash",
                    onClick = { pembayaran = "Cash" },
                    colors = RadioButtonDefaults.colors(selectedColor = BlueLaundryGo)
                )
                Text("Cash")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = pembayaran == "QRIS",
                    onClick = { pembayaran = "QRIS" },
                    colors = RadioButtonDefaults.colors(selectedColor = BlueLaundryGo)
                )
                Text("QRIS")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = pembayaran == "Virtual Account",
                    onClick = { pembayaran = "Virtual Account" },
                    colors = RadioButtonDefaults.colors(selectedColor = BlueLaundryGo)
                )
                Text("Virtual Account")
            }
        }

        item {
            Button(
                onClick = {
                    submitOrder()
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor =  BlueLaundryGo)
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
fun QuantityOption(
    name: String,
    price: String,
    onQuantityChange: (Int) -> Unit // Add this parameter to handle quantity change
) {
    var quantity by remember { mutableStateOf(0) }

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
                        if (quantity > 0) {
                            quantity -= 1 // Prevent negative values
                            onQuantityChange(quantity) // Notify parent of the updated quantity
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Minus")
                }
                Text(text = quantity.toString())
                IconButton(
                    onClick = {
                        quantity += 1
                        onQuantityChange(quantity) // Notify parent of the updated quantity
                    }
                ) {
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
                        selectedColor =  BlueLaundryGo,
                        unselectedColor = BlueLaundryGo,
                        disabledSelectedColor = BlueLaundryGo,
                        disabledUnselectedColor = BlueLaundryGo
                    ),
                    onClick = { onOptionSelected(option) })
                Text(option)
            }
        }
    }
}

@Composable
fun Header(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Icon on the left
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        // Text in the center
        Text(
            text = "Isi Data dan Pilih Layanan",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

fun orderLogic(
    navController: NavController,
    database: FirebaseDatabase,
    laundryName: String,
    username: String,
    address: String,
    phoneNumber: String,
    antarJemput: String,
    tipeLaundry: String,
    pembayaran: String,
    selectedCuciKiloanOption: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    if (username.isBlank() || address.isBlank() || phoneNumber.isBlank() || selectedCuciKiloanOption.isBlank()) {
        onError("Semua data harus diisi!")
        return
    }

    val ordersRef = database.getReference("orders")
    val orderId = ordersRef.push().key

    val orderData = mapOf(
        "orderId" to orderId,
        "laundryName" to laundryName,
        "username" to username,
        "address" to address,
        "phoneNumber" to phoneNumber,
        "antarJemput" to antarJemput,
        "tipeLaundry" to tipeLaundry,
        "pembayaran" to pembayaran,
        "service" to selectedCuciKiloanOption,
        "timestamp" to System.currentTimeMillis()
    )

    if (orderId != null) {
        ordersRef.child(orderId).setValue(orderData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { error -> onError(error.message ?: "Unknown Error") }
        navController.navigate("Homepage")
    } else {
        onError("Gagal membuat ID Pesanan!")
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PrevLaundryOrder() {
//    LaundryOrderScreen()
//}