package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MatchItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY id DESC")
    fun getAllMatches(): Flow<List<MatchItem>>

    @Query("SELECT * FROM matches WHERE visibility = 'public' OR visibility = 'both' ORDER BY id DESC")
    fun getPublicMatches(): Flow<List<MatchItem>>

    @Query("SELECT * FROM matches WHERE visibility = 'premium' OR visibility = 'both' ORDER BY id DESC")
    fun getPremiumMatches(): Flow<List<MatchItem>>

    @Query("SELECT COUNT(*) FROM matches")
    suspend fun getMatchCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(match: MatchItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<MatchItem>)

    @Update
    suspend fun update(match: MatchItem)

    @Delete
    suspend fun delete(match: MatchItem)

    @Query("DELETE FROM matches WHERE id = :id")
    suspend fun deleteById(id: Long)
}
