package com.ikancipung.laundrygo.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {

    fun markAsNotified(orderId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertOrUpdateNotification(orderId, true)
        }
    }

    suspend fun isOrderNotified(orderId: String): Boolean {
        return repository.checkIfNotified(orderId)
    }
}
