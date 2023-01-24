package com.tiun.gpstracker.db

import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface Dao {
    @Insert
    suspend fun insertTrack(trackItem: TrackItem)

    @Query("SELECT * FROM track")
    fun getAllTrack(): Flow<List<TrackItem>>
}