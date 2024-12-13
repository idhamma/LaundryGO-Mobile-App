package com.ikancipung.laundrygo.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDao {

    // Tambahkan atau perbarui status notifikasi
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(notification: NotificationEntity)

    // Periksa apakah notifikasi sudah diberikan
    @Query("SELECT isNotified FROM notifications WHERE orderId = :orderId LIMIT 1")
    suspend fun isNotified(orderId: String): Boolean?

    // Hapus semua data notifikasi (opsional)
    @Query("DELETE FROM notifications")
    suspend fun clearAll()
}


