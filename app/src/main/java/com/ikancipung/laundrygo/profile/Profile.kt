package com.ikancipung.laundrygo.profile

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ikancipung.laundrygo.R
import com.ikancipung.laundrygo.ui.theme.BlueLaundryGo
import com.ikancipung.laundrygo.ui.theme.RedLaundryGo

@Composable
fun Profile() {
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf(0) }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    username = "Bos Cipung"
    phoneNumber = 123
    email = "boscipung@gmail.com"
    address = "Jl. Green Andara Residences Blok B3 No. 19, Pangkalan Jati"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profil",
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight(500)
        )

        PhotoProfile()

        Text(
            text = username,
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight(500)
        )

        DataProfile(
            username = username, phoneNumber = phoneNumber, email = email, address = address
        )
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
    username: String, phoneNumber: Number, email: String, address: String
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
                .clickable { /*TODO*/ })
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

@Preview
@Composable
fun PrevProfile() {
    Profile()
}