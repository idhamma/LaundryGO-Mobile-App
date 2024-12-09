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
import com.google.firebase.database.FirebaseDatabase
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

    private fun addLaundryNode() {
        val database = FirebaseDatabase.getInstance().getReference("laundry")

        val laundries = mapOf(
            "laundry1" to mapOf(
                "name" to "Antony Laundry",
                "imageUrl" to "https://ibb.co.com/vXgJjtW",
                "address" to "Jl. Kemanggisan Raya No. 10, Jakarta",
                "services" to listOf("Cuci Lipat", "Cuci Setrika", "Cuci Express"),
                "prices" to listOf("4.500/kg", "8.500/kg", "10.000/kg"),
                "hours" to "08.00 - 18.00 WIB",
                "description" to "Laundry terpercaya dengan layanan berkualitas tinggi."
            ),

            "laundry2" to mapOf(
                "name" to "Jasjus Laundry",
                "imageUrl" to "https://via.placeholder.com/150/FF0000/FFFFFF?text=Lyonard",
                "address" to "Jl. Soekarno Hatta No. 15, Bandung",
                "services" to listOf("Cuci Lipat", "Cuci Selimut", "Cuci Sepatu"),
                "prices" to listOf("5.000/kg", "12.000/pcs", "20.000/pair"),
                "hours" to "07.00 - 19.00 WIB",
                "description" to "Memberikan layanan laundry terbaik dengan harga terjangkau."
            ),

            "laundry3" to mapOf(
                "name" to "Kiyomasa Laundry",
                "imageUrl" to "https://imgur.com/a/yoeQQWx",
                "address" to "Jl. Imam Bonjol No. 21, Surabaya",
                "services" to listOf("Cuci Lipat", "Cuci Express", "Cuci Selimut"),
                "prices" to listOf("4.000/kg", "9.000/kg", "15.000/pcs"),
                "hours" to "06.00 - 20.00 WIB",
                "description" to "Layanan cepat dan bersih, cocok untuk kebutuhan Anda."
            ),

            "laundry4" to mapOf(
                "name" to "Bersih Laundry",
                "imageUrl" to "https://via.placeholder.com/2000/00000/FFFFFF?text=rakha",
                "address" to "Jl. Merdeka No. 5, Yogyakarta",
                "services" to listOf("Cuci Lipat", "Cuci Setrika", "Cuci Karpet"),
                "prices" to listOf("6.000/kg", "10.000/kg", "25.000/m2"),
                "hours" to "08.00 - 17.00 WIB",
                "description" to "Layanan laundry yang ramah lingkungan dan berkualitas."
            ),

            "laundry5" to mapOf(
                "name" to "Cuci Cepat",
                "imageUrl" to "https://raw.githubusercontent.com/idhamma/LaundryGO-image-storage/starter/national-cancer-institute-rHfsPolwIgk-unsplash.jpg",
                "address" to "Jl. Sudirman No. 12, Medan",
                "services" to listOf("Cuci Lipat", "Cuci Setrika", "Cuci Baju"),
                "prices" to listOf("5.500/kg", "9.500/kg", "12.000/kg"),
                "hours" to "24 Jam",
                "description" to "Layanan laundry cepat dan terpercaya, buka 24 jam."
            ),

            "laundry6" to mapOf(
                "name" to "Laundry Sehat",
                "imageUrl" to "https://example.com/laundry_sehat.jpg",
                "address" to "Jl. Siliwangi No. 8, Bandung",
                "services" to listOf("Cuci Lipat", "Cuci Selimut", "Cuci Bantal"),
                "prices" to listOf("7.000/kg", "15.000/pcs", "18.000/pcs"),
                "hours" to "09.00 - 21.00 WIB",
                "description" to "Laundry dengan produk ramah lingkungan dan aman."
            )
        )

        database.setValue(laundries).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Node laundries berhasil dibuat!")
            } else {
                println("Gagal membuat node laundries: ${task.exception}")
            }
        }
    }
}