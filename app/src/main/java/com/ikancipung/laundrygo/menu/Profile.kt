package com.ikancipung.laundrygo.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ikancipung.laundrygo.R


@Composable
fun MyApp() {
    Scaffold(
        bottomBar = { Footer() }
    ) { innerPadding ->
        // Isi konten aplikasi Anda di sini
        Box(modifier = Modifier.padding(innerPadding)) {
            ProfileSettingsScreen()
        }
    }
}

@Composable
fun ProfileSettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Profile Settings")

        // Gambar Profil
        Image(
            painter = painterResource(id = R.drawable.bos_cipung), // Ganti dengan gambar lokal Anda
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text("Bos Cipung")

        // Detail Profil
        OutlinedTextField(
            value = "Bos Cipung",
            onValueChange = { /* Handle Name Change */ },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        OutlinedTextField(
            value = "08123456789",
            onValueChange = { /* Handle Phone Number Change */ },
            label = { Text("Nomor Telepon") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        OutlinedTextField(
            value = "boscipung@example.com",
            onValueChange = { /* Handle Email Change */ },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        OutlinedTextField(
            value = "Jl. Green Andara Residences Blok B3 No. 19, Pangkalan Jati",
            onValueChange = { /* Handle Email Change */ },
            label = { Text("Alamat") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        OutlinedTextField(
            value = "Ganti Kata Sandi",
            onValueChange = { /* Handle Email Change */ },
            label = { Text("") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        OutlinedTextField(
            value = "Keluar",
            onValueChange = { /* Handle Email Change */ },
            label = { Text("") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

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

@Composable
fun NavigationItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Text(text = title, modifier = Modifier.padding(top = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSettingsScreenPreview(){
//    ProfileSettingsScreen()
//    Footer()
    MyApp()
}