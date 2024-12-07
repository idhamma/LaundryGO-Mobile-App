package com.ikancipung.laundrygo.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo
import com.ikancipung.laundrygo.ui.theme.RedLaundryGo

@Composable
fun Profile(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf(0) }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid

    if (uid != null) {
        // Referensi ke Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(uid)

        // Fetch data dari Realtime Database
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                username = snapshot.child("name").getValue(String::class.java) ?: ""
                email = snapshot.child("email").getValue(String::class.java) ?: ""
            }
            override fun onCancelled(error: DatabaseError) {
                // Tangani error
                println("Error fetching data: ${error.message}")
            }

        })
    } else {
        // Jika pengguna belum login, navigasikan ke halaman login
        navController.navigate("Login")
    }

//    username = "Bos Cipung"
//    phoneNumber = 123
//    email = "boscipung@gmail.com"
//    address = "Jl. Green Andara Residences Blok B3 No. 19, Pangkalan Jati"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Profil",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight(500)
            )
        }

        item {
            PhotoProfile()
        }

        item {
            Text(
                text = username,
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight(500)
            )
        }

        item {
            DataProfile(
                navController = navController, username = username, phoneNumber = phoneNumber, email = email, address = address
            )
        }
    }
}

@Composable
fun PhotoProfile() {
    Box {
        val photoProfile = painterResource(id = R.drawable.bos_cipung)

        Image(
            painter = photoProfile,
            contentDescription = "photo_profile",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
        ) {
            Icon(imageVector = Icons.Filled.Edit,
                contentDescription = "edit_icon",
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .background(BlueLaundryGo, CircleShape)
                    .clickable { /*TODO*/ }
                    .padding(8.dp))
        }
    }
}

@Composable
fun DataProfile(
    username: String, phoneNumber: Number, email: String, address: String,
    navController: NavController
) {
    val dataFields = listOf(
        "Nama" to username,
        "Nomor Telepon" to phoneNumber.toString(),
        "Email" to email,
        "Alamat" to address
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        dataFields.forEach { (label, value) ->
            FieldDataProfile(
                label = label, value = value
            )
        }
        Text(text = "Ganti Kata Sandi",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight(500),
            color = BlueLaundryGo,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable { /*TODO*/ })
        Text(text = "Keluar",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight(500),
            color = RedLaundryGo,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable { navController?.navigate("Login") })
    }
}

@Composable
fun FieldDataProfile(
    label: String, value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(500),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Image(painter = painterResource(id = R.drawable.edit),
                contentDescription = "edit_icon",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { /*TODO*/ })
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(bottom = 16.dp), thickness = 0.25.dp, color = Color.Gray
    )
}
