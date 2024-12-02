package com.ikancipung.laundrygo.signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpScreen()
        }
    }
}

@Composable
fun SignUpScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title dan Subtitle
        Text(
            text = "Sign up",
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create an account to get started",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input untuk Nama
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle name input */ },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input untuk Email
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle email input */ },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input untuk Password
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle password input */ },
            label = { Text("Create a password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input untuk Confirm Password
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle confirm password input */ },
            label = { Text("Confirm password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox dengan Teks Terms and Privacy
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = { /* Handle checkbox click */ }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row {
                    Text(
                        text = "I've read and agree with the ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Terms and Conditions",
                        fontSize = 14.sp,
                        color = Color.Blue
                    )
                }
                Row {
                    Text(
                        text = "and the ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Privacy Policy",
                        fontSize = 14.sp,
                        color = Color.Blue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Sign-Up
        Button(
            onClick = { Toast.makeText(context, "Sign-Up clicked", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue, // Warna tombol biru
                contentColor = Color.White // Teks tombol putih
            )
        ) {
            Text("Sign Up", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}
