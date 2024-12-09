package com.ikancipung.laundrygo.profile

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo
import com.ikancipung.laundrygo.ui.theme.RedLaundryGo

@Composable
fun Profile(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid
    val context = LocalContext.current

    // Listener untuk pembaruan data secara real-time
    DisposableEffect(uid) {
        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users").child(uid)

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.let {
                        username = it.child("name").getValue(String::class.java) ?: "Unknown"
                        email = it.child("email").getValue(String::class.java) ?: "Unknown"
                        address = it.child("address").getValue(String::class.java) ?: "Unknown"
                        phoneNumber =
                            it.child("phoneNumber").getValue(String::class.java) ?: "Unknown"
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

    if (isLoading) {
        // Tampilkan indikator loading jika data sedang dimuat
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
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
                    text = if (username.isNotEmpty()) username else "Nama belum diisi",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            item {
                DataProfile(navController = navController,
                    username = username,
                    phoneNumber = phoneNumber,
                    email = email,
                    address = address,
                    onSave = { fieldKey, newValue ->
                        if (uid != null) {
                            val updates: Map<String, Any> =
                                mapOf(fieldKey to newValue) // Simpan sebagai String

                            FirebaseDatabase.getInstance().getReference("users").child(uid)
                                .updateChildren(updates).addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "$fieldKey berhasil diperbarui!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context, "Gagal memperbarui $fieldKey.", Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    })
            }
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
    username: String,
    phoneNumber: String,
    email: String,
    address: String,
    navController: NavController,
    onSave: (String, String) -> Unit
) {
    val context = LocalContext.current
    // Pemetaan label ke variabel database
    val fieldMap = mapOf(
        "Nama" to "name",
        "Nomor Telepon" to "phoneNumber",
        "Email" to "email",
        "Alamat" to "address"
    )

    val dataFields = listOf(
        "Nama" to username, "Nomor Telepon" to phoneNumber, "Email" to email, "Alamat" to address
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        dataFields.forEach { (label, value) ->
            FieldDataProfile(label = label, value = value, onSave = { newValue ->
                val fieldKey = fieldMap[label] // Dapatkan kunci dari peta
                if (fieldKey != null) {
                    onSave(fieldKey, newValue) // Gunakan kunci yang sesuai
                } else {
                    Toast.makeText(
                        context, "Invalid field: $label", Toast.LENGTH_SHORT
                    ).show()
                }
            })
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
                .clickable { navController.navigate("Login") })
    }
}

@Composable
fun FieldDataProfile(
    label: String, value: String, onSave: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(value) }

    Column(modifier = Modifier.fillMaxWidth()) {
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
            if (isEditing) {
                TextField(
                    value = textFieldValue, onValueChange = { newValue ->
                        if (label == "Nomor Telepon" && newValue.all { it.isDigit() }) {
                            textFieldValue = newValue
                        } else if (label != "Nomor Telepon") {
                            textFieldValue = newValue
                        }
                    }, singleLine = true, modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = {
                        onSave(textFieldValue)
                        isEditing = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Simpan",
                            tint = BlueLaundryGo
                        )
                    }
                    IconButton(onClick = {
                        textFieldValue = value
                        isEditing = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Batal",
                            tint = RedLaundryGo
                        )
                    }
                }
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { isEditing = true }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 16.dp), thickness = 0.25.dp, color = Color.Gray
        )
    }
}
