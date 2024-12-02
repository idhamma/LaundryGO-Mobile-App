package com.ikancipung.laundrygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ikancipung.laundrygo.order.TitleLaundryScreen
import com.ikancipung.laundrygo.ui.theme.LaundryGOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaundryGOTheme {
                // Memanggil TitleLaundryScreen dengan contoh orderId
                TitleLaundryScreen(orderId = "L-93V832NM102")
            }
        }
    }
}