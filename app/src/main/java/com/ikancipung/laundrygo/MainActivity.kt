package com.ikancipung.laundrygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.common.reflect.TypeToken
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.ikancipung.laundrygo.login.LoginScreen
import com.ikancipung.laundrygo.menu.FavoriteLaundryScreen
import com.ikancipung.laundrygo.menu.ServiceLaundryScreen
import com.ikancipung.laundrygo.menu.HomepagePage
import com.ikancipung.laundrygo.menu.ProfileUser
import com.ikancipung.laundrygo.order.LaundryOrderScreen
import com.ikancipung.laundrygo.order.RatingScreen
import com.ikancipung.laundrygo.order.TitleLaundryScreen
//import com.ikancipung.laundrygo.order.myOrderPage
import com.ikancipung.laundrygo.payment.QrisPaymentScreen
import com.ikancipung.laundrygo.payment.VAPaymentScreen
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
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("Login") { LoginScreen(navController = navController) }
                    composable("Signup") { SignUpScreen(navController = navController) }
                    composable("Homepage") { HomepagePage(navController = navController) }
                    composable("FavLaundry") { FavoriteLaundryScreen(navController = navController) }
                    composable("ServiceLaundryScreen/{serviceName}") { backStackEntry ->
                        val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
                        ServiceLaundryScreen(navController = navController, serviceName = serviceName)
                    }
//                    composable("Myorder") { myOrderPage(navController = navController) }
//                    composable("Orderpage") { LaundryOrderScreen(navController = navController) }
                    composable(
                        route = "Ordersum/{orderId}",
                        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                        TitleLaundryScreen(navController = navController, orderId = orderId)
                    }
                    composable("Rating") { RatingScreen(navController = navController) }
                    composable("Qris") { QrisPaymentScreen(navController = navController) }
                    composable("Vapayment") { VAPaymentScreen(navController = navController) }
                    composable("Profile") { ProfileUser(navController = navController) }
                    composable(
                        route = "ProfileLaundry/{name}/{address}/{imageUrl}/{description}/{hours}/{prices}/{services}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("address") { type = NavType.StringType },
                            navArgument("imageUrl") { type = NavType.StringType },
                            navArgument("description") { type = NavType.StringType },
                            navArgument("hours") { type = NavType.StringType },
                            navArgument("services") { type = NavType.StringType },
                            navArgument("prices") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val address = backStackEntry.arguments?.getString("address") ?: ""
                        val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                        val description = backStackEntry.arguments?.getString("description") ?: ""
                        val hours = backStackEntry.arguments?.getString("hours") ?: ""
                        val pricesJson = backStackEntry.arguments?.getString("prices") ?: ""
                        val servicesJson = backStackEntry.arguments?.getString("services") ?: ""

                        val prices = if (pricesJson != null) {
                            Gson().fromJson(pricesJson, object : TypeToken<List<String>>() {}.type)
                        } else {
                            emptyList<String>()
                        }

                        val service = if (servicesJson != null) {
                            Gson().fromJson(
                                servicesJson,
                                object : TypeToken<List<String>>() {}.type
                            )
                        } else {
                            emptyList<String>()
                        }
                        ProfileLaundry(
                            navController = navController,
                            laundryName = name,
                            laundryAddress = address,
                            laundryRating = "5",
                            laundryLogo = imageUrl,
                            services = service,
                            prices = prices,
                            serviceHours = hours,
                            laundryDescription = description
                        )
                    }
                    composable(
                        route = "Orderpage/{name}/{prices}/{services}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("prices") { type = NavType.StringType },
                            navArgument("services") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val pricesJson = backStackEntry.arguments?.getString("prices") ?: ""
                        val servicesJson = backStackEntry.arguments?.getString("services") ?: ""

                        val prices = if (pricesJson != null) {
                            Gson().fromJson(pricesJson, object : TypeToken<List<String>>() {}.type)
                        } else {
                            emptyList<String>()
                        }

                        val service = if (servicesJson != null) {
                            Gson().fromJson(
                                servicesJson,
                                object : TypeToken<List<String>>() {}.type
                            )
                        } else {
                            emptyList<String>()
                        }
                        LaundryOrderScreen(
                            navController = navController,
                            laundryName = name,
                            prices = prices,
                            services = service
                        )
                    }
                }
            }
        }
//        addNode()
    }

    private fun addNode() {
        val database = FirebaseDatabase.getInstance().getReference("Banner")

        val laundries = mapOf(
            "Banner" to mapOf(
                "imageUrl1" to "https://raw.githubusercontent.com/idhamma/LaundryGO-image-storage/starter/LaundryGO.jpg",
                "imageUrl2" to "https://raw.githubusercontent.com/idhamma/LaundryGO-image-storage/starter/Promo.jpg"
            ),
        )

        database.setValue(laundries).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Node berhasil dibuat!")
            } else {
                println("Gagal membuat node : ${task.exception}")
            }
        }
    }
}