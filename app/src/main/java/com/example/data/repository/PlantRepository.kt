package com.example.data.repository

import androidx.compose.ui.graphics.Color
import com.example.data.local.BadgeEntity
import com.example.data.local.GrownPlantEntity
import com.example.data.local.PlantPalsDao
import com.example.data.local.UserProgressEntity
import com.example.data.model.PlantCategory
import com.example.data.model.PlantPart
import com.example.data.model.PlantSpecies
import com.example.data.model.QuizQuestion
import com.example.data.model.SeedDispersalMode
import com.example.data.model.SeedTravelAdventure
import kotlinx.coroutines.flow.Flow

class PlantRepository(private val dao: PlantPalsDao) {

    // Reactive DB flows
    val grownPlants: Flow<List<GrownPlantEntity>> = dao.getAllGrownPlants()
    val badges: Flow<List<BadgeEntity>> = dao.getAllBadges()
    val userProgress: Flow<UserProgressEntity?> = dao.getUserProgress()

    suspend fun addPlantToGarden(type: String, name: String, emoji: String, heightCm: Int) {
        dao.insertGrownPlant(
            GrownPlantEntity(
                plantType = type,
                name = name,
                emoji = emoji,
                heightCm = heightCm
            )
        )
        dao.addStars(10)
    }

    suspend fun waterGardenPlant(plant: GrownPlantEntity) {
        dao.updateGrownPlant(plant.copy(timesWatered = plant.timesWatered + 1, heightCm = plant.heightCm + 2))
        dao.addStars(2)
        dao.unlockBadge("WATER_WHISPERER")
    }

    suspend fun unlockBadge(badgeId: String) {
        dao.unlockBadge(badgeId)
    }

    suspend fun addStars(amount: Int) {
        dao.addStars(amount)
    }

    suspend fun markAnatomyCompleted() {
        dao.unlockBadge("ANATOMY_STAR")
        dao.addStars(15)
    }

    suspend fun markPhotosynthesisCompleted() {
        dao.unlockBadge("CHEF_MAGIC")
        dao.addStars(15)
    }

    suspend fun markMicroscopeCompleted() {
        dao.unlockBadge("MICROSCOPE_EYE")
        dao.addStars(15)
    }

    suspend fun markPlantDoctorCompleted() {
        dao.unlockBadge("PLANT_DOCTOR")
        dao.addStars(15)
    }

    suspend fun markSeedTravelCompleted() {
        dao.unlockBadge("SEED_TRAVELER")
        dao.addStars(15)
    }

    // Static educational content
    fun getPlantParts(): List<PlantPart> {
        return listOf(
            PlantPart(
                id = "flower",
                name = "Flower",
                emoji = "🌸",
                roleTitle = "The Seed Maker & Pollinator Magnet!",
                kidExplanation = "Flowers are bright and colorful to invite busy bees and butterflies. They help make new seeds so more plants can grow!",
                funFact = "Some flowers like sunflowers always turn their heads to follow the warm sun across the sky!",
                accentColor = Color(0xFFEC407A),
                microscopeTitle = "Pollen Grains Under Lens",
                microscopeExplanation = "Golden microscopic spheres with spiky Velcro surfaces that stick to fuzzy bee legs for pollination!",
                microscopeEmoji = "🟡"
            ),
            PlantPart(
                id = "leaf",
                name = "Leaf",
                emoji = "🍃",
                roleTitle = "The Solar Kitchen (Photosynthesis)!",
                kidExplanation = "Leaves catch sunlight and mix it with water and fresh air to cook sweet food (sugar) for the whole plant. They also make fresh oxygen for us to breathe!",
                funFact = "Leaves have tiny microscopic windows called stomata that open to let air in and out!",
                accentColor = Color(0xFF43A047),
                microscopeTitle = "Stomata & Chloroplasts",
                microscopeExplanation = "Tiny green mouth-like pores (stomata) that breathe in CO2, surrounded by millions of green solar disks called chloroplasts!",
                microscopeEmoji = "🟢"
            ),
            PlantPart(
                id = "fruit",
                name = "Fruit",
                emoji = "🍎",
                roleTitle = "The Seed Suitcase & Protector!",
                kidExplanation = "Fruits wrap around seeds to protect them. Delicious fruits like apples and berries encourage animals to eat them and carry seeds to new places!",
                funFact = "Did you know that tomatoes, pumpkins, and cucumbers are scientifically fruits because they have seeds inside?",
                accentColor = Color(0xFFE53935),
                microscopeTitle = "Fructose Sugar Crystals & Seed Shell",
                microscopeExplanation = "Sweet juice vesicles packed with water and sugar molecules, surrounding a tough protective seed coat!",
                microscopeEmoji = "💎"
            ),
            PlantPart(
                id = "stem",
                name = "Stem & Trunk",
                emoji = "🌿",
                roleTitle = "The Plant's Elevator & Backbone!",
                kidExplanation = "The stem stands tall and strong to hold up leaves to the sun. Inside are tiny straw-like tubes that pump water up from roots and food down from leaves!",
                funFact = "Tree trunks are just giant woody stems that grow tree rings every single year!",
                accentColor = Color(0xFF689F38),
                microscopeTitle = "Xylem & Phloem (Twin Straws)",
                microscopeExplanation = "A bundle of microscopic tubes: Xylem straw pumps water upwards, Phloem straw pumps sweet sugar downwards!",
                microscopeEmoji = "🧪"
            ),
            PlantPart(
                id = "seed",
                name = "Seed",
                emoji = "🌱",
                roleTitle = "The Baby Plant in a Shell!",
                kidExplanation = "A seed is a sleeping baby plant packed with its own lunchbox of energy. When warm water and soil hug it, it wakes up and sprouts!",
                funFact = "The biggest seed in the world is the Coco de Mer coconut—it can weigh as much as three big bowling balls (up to 40 pounds)!",
                accentColor = Color(0xFFFBC02D),
                microscopeTitle = "Embryo & Cotyledon Lunchbox",
                microscopeExplanation = "Inside the hard shell rests the tiny sleeping baby sprout with twin mini leaves waiting for moisture to wake up!",
                microscopeEmoji = "🔬"
            ),
            PlantPart(
                id = "root",
                name = "Roots",
                emoji = "🪱",
                roleTitle = "The Deep Anchor & Water Straw!",
                kidExplanation = "Roots hide under the ground. They grip the dirt tightly so wind won't blow the plant away, and suck up water and minerals from the earth!",
                funFact = "Some desert plants send roots down over 100 feet deep into the ground just to find hidden water!",
                accentColor = Color(0xFF8D6E63),
                microscopeTitle = "Fuzzy Microscopic Root Hairs",
                microscopeExplanation = "Thousands of microscopic translucent hairs squeezing between dirt particles to sip individual water molecules!",
                microscopeEmoji = "🕸️"
            )
        )
    }

    fun getSeedTravelAdventures(): List<SeedTravelAdventure> {
        return listOf(
            SeedTravelAdventure(
                id = "dandelion",
                plantName = "Dandelion",
                seedEmoji = "🌾",
                mode = SeedDispersalMode.WIND,
                vehicleName = "Pappus Parachute",
                funStory = "Dandelion seeds wear tiny white fluffy parachutes. When a child makes a wish or the wind blows, they float high into the clouds!",
                actionText = "Blow In The Wind! 💨"
            ),
            SeedTravelAdventure(
                id = "maple",
                plantName = "Maple Tree",
                seedEmoji = "🍁",
                mode = SeedDispersalMode.WIND,
                vehicleName = "Helicopter Whirlybird (Samara)",
                funStory = "Maple seeds have aerodynamic curved wings. When they drop from the high branches, they spin like helicopter blades to glide far away!",
                actionText = "Spin Helicopter! 🚁"
            ),
            SeedTravelAdventure(
                id = "coconut",
                plantName = "Coconut Palm",
                seedEmoji = "🥥",
                mode = SeedDispersalMode.WATER,
                vehicleName = "Ocean Voyager Canoe",
                funStory = "Coconuts have a thick waterproof husk filled with trapped air pockets. They can float across hundreds of miles of ocean waves to find a new island beach!",
                actionText = "Sail Across Ocean! 🌊"
            ),
            SeedTravelAdventure(
                id = "burdock",
                plantName = "Burdock (Invention of Velcro)",
                seedEmoji = "🦔",
                mode = SeedDispersalMode.ANIMAL,
                vehicleName = "Furry Hitchhiker",
                funStory = "Burdock seeds have hundreds of microscopic curved hooks. They latch onto a passing puppy's fur or kid's socks for a free ride! This inspired the invention of Velcro!",
                actionText = "Hitch A Ride! 🐕"
            ),
            SeedTravelAdventure(
                id = "touch_me_not",
                plantName = "Jewelweed (Touch-Me-Not)",
                seedEmoji = "💥",
                mode = SeedDispersalMode.EXPLOSION,
                vehicleName = "Pop Cannon Pod",
                funStory = "When these seed pods ripen, they build up elastic spring tension. A single gentle touch makes the pod burst open like a party popper, firing seeds up to 10 feet!",
                actionText = "Trigger Explosion! 💥"
            )
        )
    }

    fun getAllPlantSpecies(): List<PlantSpecies> {
        return listOf(
            PlantSpecies(
                id = "sunflower",
                name = "Happy Sunflower",
                emoji = "🌻",
                category = PlantCategory.FLOWERS,
                simpleDescription = "A giant, cheery flower with golden yellow petals that loves standing tall in sunny fields.",
                superpower = "Heliotropism: Tracks the sun from east to west every morning and evening!",
                habitat = "Sunny gardens & open grasslands",
                howItGrows = "Grows super fast—can shoot up 12 feet tall in just 3 months!",
                funFact = "One sunflower head is actually made of thousands of tiny individual flowers clustered together.",
                color = Color(0xFFFBC02D)
            ),
            PlantSpecies(
                id = "rose",
                name = "Sweet Rose",
                emoji = "🌹",
                category = PlantCategory.FLOWERS,
                simpleDescription = "A fragrant beauty with soft layered petals and sharp thorns along its stem.",
                superpower = "Thorn Armor: Sharp thorns protect it from hungry garden animals!",
                habitat = "Backyards, botanical gardens, temperate hills",
                howItGrows = "Blooms with wonderful perfume in spring and summer.",
                funFact = "Fossil records show wild roses have been blooming on Earth for over 35 million years!",
                color = Color(0xFFE91E63)
            ),
            PlantSpecies(
                id = "water_lily",
                name = "Floating Water Lily",
                emoji = "🪷",
                category = PlantCategory.FLOWERS,
                simpleDescription = "A gorgeous aquatic flower that floats serenely on quiet ponds and lakes.",
                superpower = "Floating Raft: Leaves have built-in air pockets that keep frogs resting dry on top!",
                habitat = "Calm freshwater ponds & river bends",
                howItGrows = "Roots anchor in underwater mud, while long stems reach to the water surface.",
                funFact = "Water lily pads are coated in a wax that makes water droplets bead up and roll away without getting soaked!",
                color = Color(0xFF80DEEA)
            ),
            PlantSpecies(
                id = "oak_tree",
                name = "Mighty Oak Tree",
                emoji = "🌳",
                category = PlantCategory.TREES,
                simpleDescription = "A grand, sturdy forest giant with wide branches that can live for hundreds of years.",
                superpower = "Acorn Factory: Can produce over 10,000 acorns in a single year to feed squirrels!",
                habitat = "Temperate woodlands and lush parks",
                howItGrows = "Starts from a tiny brown acorn and grows into a 80-foot tall king of the forest.",
                funFact = "A single mature oak tree can drink over 50 gallons of water every single day!",
                color = Color(0xFF388E3C)
            ),
            PlantSpecies(
                id = "sequoia",
                name = "Giant Redwood Sequoia",
                emoji = "🌲",
                category = PlantCategory.TREES,
                simpleDescription = "The skyscraper of the plant world! Taller than the Statue of Liberty.",
                superpower = "Fireproof Bark: Thick, spongy bark protects it from forest fires and bugs!",
                habitat = "Misty coastal mountain slopes of California",
                howItGrows = "Can live for more than 3,000 years and grow over 300 feet tall.",
                funFact = "They can drink fog directly from the clouds through their needles!",
                color = Color(0xFF2E7D32)
            ),
            PlantSpecies(
                id = "coconut_palm",
                name = "Tropical Coconut Palm",
                emoji = "🌴",
                category = PlantCategory.TREES,
                simpleDescription = "A flexible, beach-loving palm tree with delicious coconuts swinging up top.",
                superpower = "Hurricane Bender: Super flexible trunks can bend almost flat in strong storm winds without snapping!",
                habitat = "Warm tropical beaches & sunny ocean islands",
                howItGrows = "Coconuts float across the ocean waves for miles until they wash ashore and sprout!",
                funFact = "Inside every fresh coconut is pure, sweet refreshing water to hydrate desert island explorers.",
                color = Color(0xFF00897B)
            ),
            PlantSpecies(
                id = "strawberry",
                name = "Juicy Strawberry",
                emoji = "🍓",
                category = PlantCategory.FRUITS_VEGGIES,
                simpleDescription = "A low-creeping ground plant with sweet, bright red heart-shaped berries.",
                superpower = "Inside-Out Fruit: The only fruit that wears its 200 tiny yellow seeds on the outside!",
                habitat = "Sunny garden patches & berry farms",
                howItGrows = "Sends out long horizontal stems called 'runners' to sprout new baby sister plants.",
                funFact = "Strawberries are actually members of the Rose family!",
                color = Color(0xFFD32F2F)
            ),
            PlantSpecies(
                id = "carrot",
                name = "Crunchy Orange Carrot",
                emoji = "🥕",
                category = PlantCategory.FRUITS_VEGGIES,
                simpleDescription = "A tasty underground taproot packed with vitamins that help you see in the dark!",
                superpower = "Subterranean Vault: Stores healthy sweet sugars underground safe from frosts.",
                habitat = "Soft, sandy farm soil",
                howItGrows = "Feathery green leaves soak up sun above, while the orange root grows deep and thick.",
                funFact = "Long ago, most wild carrots were purple and yellow before farmers bred sweet orange ones!",
                color = Color(0xFFFF9800)
            ),
            PlantSpecies(
                id = "tomato",
                name = "Vibrant Red Tomato",
                emoji = "🍅",
                category = PlantCategory.FRUITS_VEGGIES,
                simpleDescription = "A climbing vine with yellow star flowers that ripen into juicy red garden treats.",
                superpower = "Vine Climber: Hairy stems can sprout extra roots anywhere they touch moist soil!",
                habitat = "Vegetable gardens, pots, and sunny trellises",
                howItGrows = "Needs sunny days and warm soil to produce dozens of sweet round fruits.",
                funFact = "There are over 10,000 different types of tomatoes, including purple, zebra striped, and yellow pear shapes!",
                color = Color(0xFFF44336)
            ),
            PlantSpecies(
                id = "cactus",
                name = "Spike the Saguaro Cactus",
                emoji = "🌵",
                category = PlantCategory.WILD_WONDERS,
                simpleDescription = "A desert champion with prickly spines instead of leaves, standing tall under the scorching sun.",
                superpower = "Water Sponge: Can expand like an accordion to drink and store over 200 gallons of rain in one storm!",
                habitat = "Hot, arid Sonoran desert",
                howItGrows = "Takes 75 years just to grow its first side arm, living over 200 years!",
                funFact = "Its sharp spines are actually modified leaves that protect it from thirsty desert critters.",
                color = Color(0xFF43A047)
            ),
            PlantSpecies(
                id = "venus_flytrap",
                name = "Snap! Venus Flytrap",
                emoji = "🪴",
                category = PlantCategory.WILD_WONDERS,
                simpleDescription = "A jaw-dropping carnivorous plant with toothy snap-traps that loves catching bug snacks.",
                superpower = "Super Speed Snap: Shuts in 1/10th of a second when a fly touches its trigger hairs twice!",
                habitat = "Boggy wetlands with nutrient-poor soil in North and South Carolina",
                howItGrows = "Catches insects to get essential minerals like nitrogen that muddy bog soils lack.",
                funFact = "It can count! It only snaps shut if a bug touches two hairs in 20 seconds, saving its energy from raindrops!",
                color = Color(0xFF00796B)
            ),
            PlantSpecies(
                id = "sensitive_plant",
                name = "Shy Mimosa (Touch-Me-Not)",
                emoji = "🌿",
                category = PlantCategory.WILD_WONDERS,
                simpleDescription = "A playful plant whose delicate fern leaves fold up instantly when gently touched!",
                superpower = "Fast Reflexes: Folds up in seconds to look wilted so plant-eating caterpillars lose interest!",
                habitat = "Tropical forests and sunny home windowsills",
                howItGrows = "Water rushes out of tiny hinges at leaf bases to fold them closed.",
                funFact = "After a few minutes of quiet and peace, the leaves slowly reopen to soak in the sunshine again.",
                color = Color(0xFF81C784)
            )
        )
    }

    fun getQuizQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 1,
                question = "Which part of the plant drinks water and anchors it in the dirt?",
                options = listOf("Leaves", "Roots", "Flower"),
                optionEmojis = listOf("🍃", "🪱", "🌸"),
                correctIndex = 1,
                hint = "Think about what is hiding underground in the dark soil!",
                praiseMessage = "Super Botanist! Roots act like tiny straws drinking water from the ground!"
            ),
            QuizQuestion(
                id = 2,
                question = "What do green leaves use to make sweet food for the plant?",
                options = listOf("Sunlight, Water & Air", "Pizza & Soda", "Ice & Rocks"),
                optionEmojis = listOf("☀️💧🌬️", "🍕🥤", "🧊🪨"),
                correctIndex = 0,
                hint = "Leaves are solar kitchens powered by the sun and rain!",
                praiseMessage = "Brilliant! That process is called Photosynthesis!"
            ),
            QuizQuestion(
                id = 3,
                question = "Which wonderful plant can store hundreds of gallons of water in the hot desert?",
                options = listOf("Cactus", "Water Lily", "Strawberry"),
                optionEmojis = listOf("🌵", "🪷", "🍓"),
                correctIndex = 0,
                hint = "It has sharp spines instead of flat leaves!",
                praiseMessage = "Awesome! Cacti are desert survival champions!"
            ),
            QuizQuestion(
                id = 4,
                question = "What special gas do plants release into the air for humans and animals to breathe?",
                options = listOf("Smoke", "Oxygen", "Steam"),
                optionEmojis = listOf("💨", "🫧", "♨️"),
                correctIndex = 1,
                hint = "It makes the morning air feel so fresh and crisp!",
                praiseMessage = "You rock! Plants make the clean Oxygen we need to breathe!"
            ),
            QuizQuestion(
                id = 5,
                question = "Which microscopic windows on leaves open to breathe air in and out?",
                options = listOf("Stomata", "Eyeballs", "Window blinds"),
                optionEmojis = listOf("🔬", "👀", "🪟"),
                correctIndex = 0,
                hint = "They look like tiny green smiling mouths under a microscope!",
                praiseMessage = "Wow! You know real plant biology! Stomata are microscopic leaf pores!"
            ),
            QuizQuestion(
                id = 6,
                question = "How do dandelion seeds travel to find new places to grow?",
                options = listOf("They take a bicycle", "Wind parachutes", "Submarines"),
                optionEmojis = listOf("🚲", "💨", "🚢"),
                correctIndex = 1,
                hint = "Make a wish and blow on the white fluffy puff!",
                praiseMessage = "Whoosh! Fluffy pappus parachutes catch the wind!"
            ),
            QuizQuestion(
                id = 7,
                question = "Which part of the plant holds it up tall like an elevator?",
                options = listOf("Stem", "Petals", "Acorn"),
                optionEmojis = listOf("🌿", "🌸", "🌰"),
                correctIndex = 0,
                hint = "It carries water up and down between the roots and leaves!",
                praiseMessage = "Spot on! Stems are the backbone and elevator of the plant!"
            ),
            QuizQuestion(
                id = 8,
                question = "Why do flowers have bright colorful petals and sweet scents?",
                options = listOf("To scare away birds", "To invite bees & butterflies", "To sleep in the dark"),
                optionEmojis = listOf("🦅", "🐝🦋", "😴"),
                correctIndex = 1,
                hint = "Bees love yellow, red, and purple flowers!",
                praiseMessage = "Hooray! Flowers attract pollinators to help make seeds!"
            ),
            QuizQuestion(
                id = 9,
                question = "Which amazing plant can snap shut in 1/10th of a second to catch insects?",
                options = listOf("Oak Tree", "Venus Flytrap", "Carrot"),
                optionEmojis = listOf("🌳", "🪴", "🥕"),
                correctIndex = 1,
                hint = "It has green leafy jaws with sensitive trigger hairs!",
                praiseMessage = "Snap! The Venus Flytrap is an incredible carnivorous plant!"
            ),
            QuizQuestion(
                id = 10,
                question = "Where does an apple keep its baby seeds safe?",
                options = listOf("Underground", "Inside its sweet fruit", "Floating in the sky"),
                optionEmojis = listOf("🕳️", "🍎", "☁️"),
                correctIndex = 1,
                hint = "Take a bite and look in the center core!",
                praiseMessage = "You got it! Fruits are nature's seed suitcases!"
            )
        )
    }
}
