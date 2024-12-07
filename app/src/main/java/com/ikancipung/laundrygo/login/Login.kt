package com.ikancipung.laundrygo.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 10.dp, end = 10.dp), // Padding untuk elemen utama
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Logo
        Image(
            painter = painterResource(id = R.drawable.laundrygo2_1),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(250.dp)
        )

        Spacer(modifier = Modifier.height(0.dp))

        // Welcome Text
        Text(
            text = "Welcome!",
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Input dengan Icon
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.iconmata),
                    contentDescription = "Password Icon",
                    modifier = Modifier.size(20.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Forgot Password Text
        TextButton(onClick = {
            Toast.makeText(context, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        }) {
            Text(
                text = "Forgot password?",
                color = Color.Blue,
                fontSize = 14.sp,
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    PerformLogin(
                        email = email,
                        password = password,
                        onSuccess = {
                            navController.navigate("Homepage") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onError = { error ->
                            errorMessage = error
                        },
                        onLoading = { loading ->
                            isLoading = loading
                        }
                    )

                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF006FFD),
                contentColor = Color.White
            )
        ) {
            Text("Login", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Annotated Text for Register Section
        val annotatedText = buildAnnotatedString {
            // Bagian teks "Not a member?"
            withStyle(style = SpanStyle(color = Color.Gray)) {
                append("Not a member? ")
            }
            // Bagian teks "Register now"
            pushStringAnnotation(tag = "REGISTER", annotation = "register_link")
            withStyle(style = SpanStyle(color = Color.Blue)) {
                append("Register now")
            }
            pop()
        }

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "REGISTER", start = offset, end = offset)
                    .firstOrNull()?.let {
                        navController.navigate("Signup")
                    }
            },
            modifier = Modifier.padding(top = 8.dp),
            style = LocalTextStyle.current.copy(fontSize = 14.sp, textAlign = TextAlign.Center)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Or Continue With Text
        Text(
            text = "Or continue with",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Social Login Icons
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Google Icon
            IconButton(onClick = {
                Toast.makeText(context, "Login with Google", Toast.LENGTH_SHORT).show()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Facebook Icon
            IconButton(onClick = {
                Toast.makeText(context, "Login with Facebook", Toast.LENGTH_SHORT).show()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.facebook_icon),
                    contentDescription = "Facebook Icon",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

private fun PerformLogin(email:String,
                         password:String,
                         onSuccess:()->Unit,
                         onError:(String)->Unit,
                         onLoading:(Boolean)->Unit){
    onLoading(true)
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password).
            addOnCompleteListener{ task -> onLoading(false)
            if (task.isSuccessful){
                onSuccess()
            }else{
                onError(task.exception?.message ?:"Login Failed")
            }}
}
