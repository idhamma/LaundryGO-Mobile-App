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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.profile.Profile
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo

val auth = FirebaseAuth.getInstance()
val currentUser = auth.currentUser
val displayName = currentUser?.displayName ?: "User"
val userId = currentUser?.uid ?: "Unknown ID"

@Composable
fun ProfileUser(navController: NavController) {
    Scaffold(
        bottomBar = { Footer(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Profile(navController)
        }
    }
}

@Composable
fun ProfileSettingsScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val displayName = currentUser?.displayName ?: "User"
    val email = currentUser?.email ?: "No email available"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Text("Profile Settings") }

        item {
            Image(
                painter = painterResource(id = R.drawable.bos_cipung),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        item { Text(displayName) }

        item {
            OutlinedTextField(
                value = displayName,
                onValueChange = { /* Handle Name Change */ },
                label = { Text("Nama") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
        }
        item {
            OutlinedTextField(
                value = email,
                onValueChange = { /* Handle Email Change */ },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
        }
        item {
            OutlinedTextField(
                value = "08123456789",
                onValueChange = { /* Handle Phone Number Change */ },
                label = { Text("Nomor Telepon") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
        }
        item {
            OutlinedTextField(
                value = "Jl. Green Andara Residences Blok B3 No. 19, Pangkalan Jati",
                onValueChange = { /* Handle Address Change */ },
                label = { Text("Alamat") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
        }
        item {
            Button(
                onClick = {
                    auth.signOut() // Sign out the user
                    navController.navigate("Login") { // Navigate to Login screen
                        popUpTo("Homepage") { inclusive = true } // Remove Homepage from the back stack
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Keluar")
            }
        }
    }
}

@Composable
fun Footer(navController: NavController) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            icon = Icons.Filled.Person,
            title = "Explore",
            isSelected = currentRoute == "Homepage",
            onClick = { navController.navigate("Homepage") }
        )
        NavigationItem(
            icon = Icons.Filled.Person,
            title = "My Order",
            isSelected = currentRoute == "Myorder",
            onClick = { navController.navigate("MyOrder") }
        )
        NavigationItem(
            icon = Icons.Filled.Person,
            title = "Profile",
            isSelected = currentRoute == "Profile",
            onClick = { navController.navigate("Profile") }
        )
    }
}

@Composable
fun NavigationItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) BlueLaundryGo else Color.Gray
        )
        Text(
            text = title,
            color = if (isSelected) BlueLaundryGo else Color.Gray
        )
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

//@Preview(showBackground = true)
//@Composable
//fun ProfileSettingsScreenPreview() {
//    MyApp()
//}