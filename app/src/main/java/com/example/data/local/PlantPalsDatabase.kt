package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        GrownPlantEntity::class,
        BadgeEntity::class,
        UserProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PlantPalsDatabase : RoomDatabase() {

    abstract fun plantPalsDao(): PlantPalsDao

    companion object {
        @Volatile
        private var INSTANCE: PlantPalsDatabase? = null

        fun getDatabase(context: Context): PlantPalsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantPalsDatabase::class.java,
                    "plant_pals_database"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed initial data
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = getDatabase(context).plantPalsDao()
                            seedInitialBadges(dao)
                            dao.saveUserProgress(
                                UserProgressEntity(
                                    id = 1,
                                    stars = 20,
                                    totalQuizzesAnswered = 0,
                                    totalPlantsGrown = 1,
                                    anatomyCompleted = false,
                                    photosynthesisCompleted = false
                                )
                            )
                            // Initial welcoming grown sunflower in garden!
                            dao.insertGrownPlant(
                                GrownPlantEntity(
                                    plantType = "SUNFLOWER",
                                    name = "Sunny",
                                    emoji = "🌻",
                                    heightCm = 30,
                                    timesWatered = 3
                                )
                            )
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedInitialBadges(dao: PlantPalsDao) {
            val initialBadges = listOf(
                BadgeEntity("SEED_SCOUT", "Seed Scout", "Plant your very first seed in the Growth Lab", "🌱", System.currentTimeMillis()),
                BadgeEntity("ANATOMY_STAR", "Plant Detective", "Discover all 6 parts of a plant", "🔍"),
                BadgeEntity("MICROSCOPE_EYE", "Microscope Explorer", "Peer into stomata and plant cells with the microscope lens", "🔬"),
                BadgeEntity("CHEF_MAGIC", "Kitchen Chemist", "Cook plant food in the Photosynthesis Lab", "☀️"),
                BadgeEntity("WATER_WHISPERER", "Water Master", "Carefully water plants without overfilling", "💧"),
                BadgeEntity("PLANT_DOCTOR", "Green Cross Doctor", "Heal a thirsty plant or rescue from hungry caterpillars", "🩺"),
                BadgeEntity("SEED_TRAVELER", "Seed Flight Pilot", "Launch seeds across wind, water, and animal routes", "🚀"),
                BadgeEntity("FLOWER_FAN", "Flower Explorer", "Learn about wonderful flowering plants", "🌸"),
                BadgeEntity("TREE_HERO", "Tree Guardian", "Discover the secrets of mighty forest trees", "🌳"),
                BadgeEntity("QUIZ_GENIUS", "Botanist Champion", "Score high stars in the Plant Quiz", "⭐"),
                BadgeEntity("WEATHER_WIZARD", "Weather Wizard", "Care for your plant through sunshine, rain, and starlight", "🌈"),
                BadgeEntity("MASTER_GARDENER", "Green Thumb Hero", "Grow and nurture multiple plants in your garden", "🏆")
            )
            dao.insertBadges(initialBadges)
        }
    }
}
