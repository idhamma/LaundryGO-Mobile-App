package com.ikancipung.laundrygo.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val orderId: String,
    val isNotified: Boolean
)

