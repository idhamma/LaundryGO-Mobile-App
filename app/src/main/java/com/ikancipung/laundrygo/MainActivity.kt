package com.ikancipung.laundrygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.ikancipung.laundrygo.login.LoginScreen
import com.ikancipung.laundrygo.menu.HomepagePage
import com.ikancipung.laundrygo.order.TitleLaundryScreen
import com.ikancipung.laundrygo.signup.SignUpScreen
import com.ikancipung.laundrygo.ui.theme.LaundryGOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaundryGOTheme {

            }
        }
    }
}