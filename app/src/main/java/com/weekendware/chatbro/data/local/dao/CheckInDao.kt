
package com.weekendware.chatbro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.weekendware.chatbro.data.local.entity.CheckInEntity

@Dao
interface CheckInDao {
    @Insert
    suspend fun insertCheckIn(checkIn: CheckInEntity)

    @Query("SELECT * FROM checkins ORDER BY timestamp DESC")
    suspend fun getAll(): List<CheckInEntity>
}
