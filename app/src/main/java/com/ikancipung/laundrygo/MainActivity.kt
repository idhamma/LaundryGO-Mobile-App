package com.ikancipung.laundrygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ikancipung.laundrygo.login.LoginScreen
import com.ikancipung.laundrygo.menu.Homepage
import com.ikancipung.laundrygo.order.LaundryOrderScreen
import com.ikancipung.laundrygo.order.RatingScreen
import com.ikancipung.laundrygo.order.TitleLaundryPreview
import com.ikancipung.laundrygo.order.TitleLaundryScreen
import com.ikancipung.laundrygo.order.myOrder
import com.ikancipung.laundrygo.payment.PaymentScreen
import com.ikancipung.laundrygo.profile.Profile
import com.ikancipung.laundrygo.profile.ProfileLaundry
import com.ikancipung.laundrygo.signup.SignUpScreen
import com.ikancipung.laundrygo.ui.theme.LaundryGOTheme

private lateinit var auth: FirebaseAuth
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            auth = Firebase.auth
            val currentUser = auth.currentUser
            val startDestination = if (currentUser != null) "Homepage" else "Login"

            val navController = rememberNavController()


            LaundryGOTheme {
                NavHost(navController, startDestination = startDestination){
                    composable("Login"){LoginScreen(navController=navController)
                    }
                    composable("Signup") {
                        SignUpScreen(navController = navController)
                    }
                    composable("Homepage") {
                        Homepage(navController = navController)
                    }
                }
            }
        }
    }
}