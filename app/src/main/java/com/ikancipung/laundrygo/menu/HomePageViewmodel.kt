package com.ikancipung.laundrygo.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ikancipung.laundrygo.R

class HomepageViewModel : ViewModel() {
    private val _laundries = MutableLiveData<List<Laundry>>()
    val laundries: LiveData<List<Laundry>> = _laundries

    init {
        loadOutlets()
    }

    private fun loadOutlets() {
        val database = FirebaseDatabase.getInstance()
        val laundryRef = database.getReference("laundry")

        laundryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val laundryList = mutableListOf<Laundry>()
                for (child in snapshot.children) {
                    val name = child.key.orEmpty()
                    val imageUrl = child.child("imageUrl").value?.toString().orEmpty()
                    val address = child.child("address").value?.toString().orEmpty()
                    val services = child.child("services").children.map { it.value?.toString().orEmpty() }
                    val prices = child.child("prices").children.map { it.value?.toString().orEmpty() }
                    val hours = child.child("hours").value?.toString().orEmpty()
                    val description = child.child("description").value?.toString().orEmpty()

                    laundryList.add(
                        Laundry(
                            name = name,
                            imageUrl = imageUrl,
                            logoResourceId = getLogoResourceIdByName(name),
                            address = address,
                            services = services,
                            prices = prices,
                            hours = hours,
                            description = description
                        )
                    )
                }
                _laundries.value = laundryList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to load outlets: ${error.message}")
            }
        })
    }

    private fun getLogoResourceIdByName(name: String): Int {
        return when (name) {
            "Antony Laundry" -> R.drawable.antony_laundry
            "Jesjus Laundry" -> R.drawable.jasjus_laundry
            "Kiyomasa Laundry" -> R.drawable.kiyomasa_laundry
            else -> R.drawable.laundrygo2_1
        }
    }
}
