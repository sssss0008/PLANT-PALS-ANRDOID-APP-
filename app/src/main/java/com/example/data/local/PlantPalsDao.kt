package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantPalsDao {

    @Query("SELECT * FROM grown_plants ORDER BY harvestedAt DESC")
    fun getAllGrownPlants(): Flow<List<GrownPlantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrownPlant(plant: GrownPlantEntity)

    @Update
    suspend fun updateGrownPlant(plant: GrownPlantEntity)

    @Query("DELETE FROM grown_plants WHERE id = :id")
    suspend fun deleteGrownPlant(id: Int)

    @Query("SELECT * FROM badges")
    fun getAllBadges(): Flow<List<BadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)

    @Query("UPDATE badges SET unlockedAt = :timestamp WHERE badgeId = :badgeId AND unlockedAt IS NULL")
    suspend fun unlockBadge(badgeId: String, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM user_progress WHERE id = 1 LIMIT 1")
    fun getUserProgress(): Flow<UserProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProgress(progress: UserProgressEntity)

    @Query("UPDATE user_progress SET stars = stars + :amount WHERE id = 1")
    suspend fun addStars(amount: Int)
}
