package com.ikancipung.laundrygo.notification

class NotificationRepository(private val dao: NotificationDao) {

    suspend fun insertOrUpdateNotification(orderId: String, isNotified: Boolean) {
        dao.insertOrUpdate(NotificationEntity(orderId, isNotified))
    }

    suspend fun checkIfNotified(orderId: String): Boolean {
        return dao.isNotified(orderId) ?: false
    }
}
