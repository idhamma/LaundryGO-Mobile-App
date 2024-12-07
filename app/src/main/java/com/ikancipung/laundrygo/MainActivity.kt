package com.ikancipung.laundrygo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ikancipung.laundrygo.login.LoginScreen
import com.ikancipung.laundrygo.menu.HomepagePage
import com.ikancipung.laundrygo.menu.ProfileUser
import com.ikancipung.laundrygo.order.LaundryOrderScreen
import com.ikancipung.laundrygo.order.RatingScreen
import com.ikancipung.laundrygo.order.TitleLaundryScreen
import com.ikancipung.laundrygo.order.myOrderPage
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
                NavHost(navController, startDestination = startDestination) {
                    composable("Login") { LoginScreen(navController = navController) }
                    composable("Signup") { SignUpScreen(navController = navController) }
                    composable("Homepage") { HomepagePage(navController = navController) }
                    composable("Myorder") { myOrderPage(navController = navController) }
                    composable("Orderpage") { LaundryOrderScreen(navController = navController) }
                    composable("Ordersum") { TitleLaundryScreen(navController = navController) }
                    composable("Rating") { RatingScreen(navController = navController) }
                    composable("Qris") { QrisPaymentScreen(navController = navController) }
                    composable("Vapayment") { VAPaymentScreen(navController = navController) }
                    composable("Profile") { ProfileUser(navController = navController) }

                    // Tambahkan navigasi untuk ProfileLaundry
                    composable(
                        "Profilelaundry/{name}/{address}/{imageUrl}/{services}/{prices}/{hours}/{description}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("address") { type = NavType.StringType },
                            navArgument("imageUrl") { type = NavType.StringType },
                            navArgument("services") { type = NavType.StringType },
                            navArgument("prices") { type = NavType.StringType },
                            navArgument("hours") { type = NavType.StringType },
                            navArgument("description") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        ProfileLaundry(
                            navController = navController,
                            laundryName = backStackEntry.arguments?.getString("name") ?: "",
                            laundryAddress = backStackEntry.arguments?.getString("address") ?: "",
                            imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: "",
                            services = backStackEntry.arguments?.getString("services")?.split(",") ?: emptyList(),
                            prices = backStackEntry.arguments?.getString("prices")?.split(",") ?: emptyList(),
                            serviceHours = backStackEntry.arguments?.getString("hours") ?: "",
                            laundryDescription = backStackEntry.arguments?.getString("description") ?: "",
                            laundryRating = "5 Stars" // Tambahkan rating statis (opsional)
                        )
                    }
                }
            }
        }
    }
}


