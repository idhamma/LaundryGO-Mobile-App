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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ikancipung.laundrygo.profile.Profile
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo

class OrderPage {

    @Composable
    fun LaundryOrderScreen() {
        var isCuciKiloanExpanded by remember { mutableStateOf(false) }
        var isTempatTidurExpanded by remember { mutableStateOf(false) }
        var isAksesorisExpanded by remember { mutableStateOf(false) }

        // State for radio button selections
        var antarJemput by remember { mutableStateOf("Ya") }
        var tipeLaundry by remember { mutableStateOf("Regular") }
        var pembayaran by remember { mutableStateOf("Cash") }
        var selectedCuciKiloanOption by remember { mutableStateOf("") }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item{
                header()
            }
            item {
                Text("Nama")
                TextField(
                    value = "",
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors( unfocusedContainerColor = Color(0xFFE4E4E4)),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text("Alamat")
                TextField(
                    value = "",
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors( unfocusedContainerColor = Color(0xFFE4E4E4)),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Map")
                    }
                )
            }

            item {
                Text("No. HP")
                TextField(
                    value = "",
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors( unfocusedContainerColor = Color(0xFFE4E4E4)),
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
                            options = listOf("Cuci Lipat", "Cuci Setrika"),
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
                    QuantityOption("Selimut", "12.500/pcs")
                    QuantityOption("Sprei", "10.000/pcs")
                }
            }

            item {
                AccordionSection(
                    title = "Aksesoris",
                    isExpanded = isAksesorisExpanded,
                    onToggle = { isAksesorisExpanded = !isAksesorisExpanded }
                ) {
                    QuantityOption("Topi", "7.500/pcs")
                    QuantityOption("Sepatu", "20.000/pair")
                }
            }

            item {
                Text("Antar Jemput")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = antarJemput == "Ya",colors = RadioButtonColors(
                        selectedColor = BlueLaundryGo,
                        unselectedColor = Color.Black,
                        disabledSelectedColor = Color.Black,
                        disabledUnselectedColor = Color.Black
                    ), onClick = { antarJemput = "Ya" })
                    Text("Ya")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = antarJemput == "Tidak",colors = RadioButtonColors(
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
                        selected = tipeLaundry == "Regular",colors = RadioButtonColors(
                            selectedColor = BlueLaundryGo,
                            unselectedColor = Color.Black,
                            disabledSelectedColor = Color.Black,
                            disabledUnselectedColor = Color.Black
                        ),
                        onClick = { tipeLaundry = "Regular" })
                    Text("Regular")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = tipeLaundry == "Express",colors = RadioButtonColors(
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
                    RadioButton(selected = pembayaran == "Cash",colors = RadioButtonColors(
                        selectedColor = BlueLaundryGo,
                        unselectedColor = Color.Black,
                        disabledSelectedColor = Color.Black,
                        disabledUnselectedColor = Color.Black
                    ), onClick = { pembayaran = "Cash" })
                    Text("Cash")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = pembayaran == "QRIS",colors = RadioButtonColors(
                        selectedColor = BlueLaundryGo,
                        unselectedColor = Color.Black,
                        disabledSelectedColor = Color.Black,
                        disabledUnselectedColor = Color.Black
                    ), onClick = { pembayaran = "QRIS" })
                    Text("QRIS")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = pembayaran == "Virtual Account",colors = RadioButtonColors(
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
                text = "Isi Data dan Pilih Layanan",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun PrevLaundryOrder() {
        LaundryOrderScreen()
    }
}