package com.ikancipung.laundrygo.login

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikancipung.laundrygo.R
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    // Handle the back press
    BackHandler {
        // Close the app instead of navigating back
        (context as? Activity)?.finish()
    }
    // States
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo
        Image(
            painter = painterResource(id = R.drawable.laundrygo2_1),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome!",
            fontSize = 24.sp,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Error Message
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colors.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Login Button
        Button(
            onClick = {
                performLogin(
                    email = email,
                    password = password,
                    onSuccess = {
                        navController.navigate("Homepage") {
                            popUpTo("Login") { inclusive = true }
                        }
                    },
                    onError = { error ->
                        errorMessage = error
                    },
                    onLoading = { loading ->
                        isLoading = loading
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Registration Prompt
        val annotatedText = buildAnnotatedString {
            append("Not a member? ")
            withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                append("Register now")
            }
        }

        ClickableText(
            text = annotatedText,
            onClick = { navController.navigate("Signup") },
            style = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
    }
}

fun performLogin(
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
    onLoading: (Boolean) -> Unit
) {
    if (email.isBlank() || password.isBlank()) {
        onError("Please fill in both email and password.")
        return
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        onError("Invalid email format.")
        return
    }

    onLoading(true)
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            onLoading(false)
            if (task.isSuccessful) {
                onSuccess()
            } else {
                val errorMessage = when (task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Incorrect password."
                    is FirebaseAuthInvalidUserException -> "No user found with this email."
                    else -> "Login failed. Please try again."
                }
                onError(errorMessage)
            }
        }
}