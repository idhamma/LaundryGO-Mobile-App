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
import com.ikancipung.laundrygo.menu.Homepage
import com.ikancipung.laundrygo.menu.HomepagePage
import com.ikancipung.laundrygo.menu.ProfileUser
import com.ikancipung.laundrygo.order.LaundryOrderScreen
import com.ikancipung.laundrygo.order.RatingScreen
import com.ikancipung.laundrygo.order.TitleLaundryScreen
import com.ikancipung.laundrygo.order.myOrder
import com.ikancipung.laundrygo.order.myOrderPage
import com.ikancipung.laundrygo.payment.QrisPaymentScreen
import com.ikancipung.laundrygo.payment.VAPaymentScreen
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
                NavHost(navController = navController, startDestination = startDestination) {
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
                    composable(
                        route = "ProfileLaundry/{name}/{address}/{imageUrl}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("address") { type = NavType.StringType },
                            navArgument("imageUrl") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val address = backStackEntry.arguments?.getString("address") ?: ""
                        val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                        ProfileLaundry(
                            navController = navController,
                            laundryName = name,
                            laundryAddress = address,
                            laundryRating = "5 Stars",
                            laundryLogo = R.drawable.ic_launcher_background,
                            services = listOf("Cuci Lipat", "Cuci Setrika"),
                            prices = listOf("10.000", "15.000"),
                            serviceHours = "08:00 - 20:00",
                            laundryDescription = "Deskripsi Laundry"
                        )
                    }
                }

            }
        }
    }
}