package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grown_plants")
data class GrownPlantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val plantType: String,
    val name: String,
    val emoji: String,
    val harvestedAt: Long = System.currentTimeMillis(),
    val heightCm: Int = 25,
    val timesWatered: Int = 1
)

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey
    val badgeId: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val unlockedAt: Long? = null
)

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val id: Int = 1,
    val stars: Int = 10,
    val totalQuizzesAnswered: Int = 0,
    val totalPlantsGrown: Int = 0,
    val anatomyCompleted: Boolean = false,
    val photosynthesisCompleted: Boolean = false
)
