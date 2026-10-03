package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class PlantCategory(val displayName: String, val emoji: String) {
    FLOWERS("Wonderful Flowers", "🌸"),
    TREES("Mighty Trees", "🌳"),
    FRUITS_VEGGIES("Yummy Fruits & Veggies", "🍓"),
    WILD_WONDERS("Wild & Strange Plants", "🌵")
}

data class PlantSpecies(
    val id: String,
    val name: String,
    val emoji: String,
    val category: PlantCategory,
    val simpleDescription: String,
    val superpower: String,
    val habitat: String,
    val howItGrows: String,
    val funFact: String,
    val color: Color
)

data class PlantPart(
    val id: String,
    val name: String,
    val emoji: String,
    val roleTitle: String,
    val kidExplanation: String,
    val funFact: String,
    val accentColor: Color,
    val microscopeTitle: String,
    val microscopeExplanation: String,
    val microscopeEmoji: String
)

enum class GrowthStage(val stageNumber: Int, val title: String, val hint: String) {
    SEED(1, "Little Seed", "Tuck the seed safely into cozy soil!"),
    SPROUT(2, "Baby Sprout", "Give water to help tiny roots drink!"),
    SEEDLING(3, "Growing Seedling", "Give warm sunshine to make green food!"),
    BLOOMING(4, "Big Blooming Plant", "Flowers open for friendly bees!"),
    HARVEST(5, "Ready to Harvest!", "Hooray! The plant has grown full and strong!")
}

enum class WeatherCondition(val label: String, val emoji: String, val description: String) {
    SUNNY("Warm Sun", "☀️", "Golden sunbeams power the green leaf kitchen!"),
    RAINY("Gentle Rain", "🌧️", "Raindrops soak into the soil and quench thirst!"),
    WINDY("Breezy Wind", "💨", "Breeze rustles leaves and spreads pollen!"),
    NIGHT("Starlit Night", "🌙", "The plant rests under the cool moon and stars.")
}

enum class PlantHealthStatus(val label: String, val emoji: String, val prescription: String) {
    HEALTHY("Thriving & Happy!", "😊", "All good! Your plant is dancing in the breeze!"),
    THIRSTY("Thirsty & Droopy", "🥀", "Leaves are thirsty! Give a splash of fresh water!"),
    CATERPILLAR("Little Bug Visitor", "🐛", "A cute caterpillar is nibbling a leaf! Gently brush it to a leaf hotel!"),
    SUNBURNED("Too Hot & Sunny", "🔥", "Leaves need a little shade or mist!")
}

enum class SeedDispersalMode(val label: String, val emoji: String) {
    WIND("Wind Parachute", "💨"),
    WATER("Water Boat", "🌊"),
    ANIMAL("Animal Hitchhiker", "🐿️"),
    EXPLOSION("Popping Pods", "💥")
}

data class SeedTravelAdventure(
    val id: String,
    val plantName: String,
    val seedEmoji: String,
    val mode: SeedDispersalMode,
    val vehicleName: String,
    val funStory: String,
    val actionText: String
)

enum class PotStyle(val id: String, val label: String, val potColor: Color, val rimColor: Color, val emoji: String) {
    TERRACOTTA("terracotta", "Classic Terracotta", Color(0xFFFF7043), Color(0xFFFF8A65), "🪴"),
    RAINBOW("rainbow", "Rainbow Pastel", Color(0xFFBA68C8), Color(0xFFE1BEE7), "🌈"),
    SUNNY_GOLD("gold", "Golden Botanist", Color(0xFFFFCA28), Color(0xFFFFF176), "🏆"),
    FOREST_MINT("mint", "Forest Mint", Color(0xFF4DB6AC), Color(0xFF80CBC4), "🍃")
}

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val optionEmojis: List<String>,
    val correctIndex: Int,
    val hint: String,
    val praiseMessage: String
)

data class BadgeInfo(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean = false
)
