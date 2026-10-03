package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SpeechHelper
import com.example.data.local.GrownPlantEntity
import com.example.data.local.PlantPalsDatabase
import com.example.data.model.GrowthStage
import com.example.data.model.PlantCategory
import com.example.data.model.PlantHealthStatus
import com.example.data.model.PlantPart
import com.example.data.model.PlantSpecies
import com.example.data.model.PotStyle
import com.example.data.model.QuizQuestion
import com.example.data.model.SeedTravelAdventure
import com.example.data.model.WeatherCondition
import com.example.data.repository.PlantRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    ANATOMY,
    GROWTH_LAB,
    PHOTOSYNTHESIS,
    SEED_TRAVEL,
    ENCYCLOPEDIA,
    QUIZ,
    GARDEN
}

class PlantPalsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlantRepository
    private val speechHelper: SpeechHelper = SpeechHelper(application)

    init {
        val database = PlantPalsDatabase.getDatabase(application)
        repository = PlantRepository(database.plantPalsDao())
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Confetti celebration state
    private val _showConfetti = MutableStateFlow(false)
    val showConfetti: StateFlow<Boolean> = _showConfetti.asStateFlow()

    // Database reactive flows
    val grownPlants = repository.grownPlants.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val badges = repository.badges.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val userProgress = repository.userProgress.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    // Educational Data
    val plantParts: List<PlantPart> = repository.getPlantParts()
    val allSpecies: List<PlantSpecies> = repository.getAllPlantSpecies()
    val quizQuestions: List<QuizQuestion> = repository.getQuizQuestions()
    val seedAdventures: List<SeedTravelAdventure> = repository.getSeedTravelAdventures()

    // Anatomy Screen State
    private val _selectedPartId = MutableStateFlow("flower")
    val selectedPartId: StateFlow<String> = _selectedPartId.asStateFlow()

    private val _exploredPartIds = MutableStateFlow(setOf("flower"))
    val exploredPartIds: StateFlow<Set<String>> = _exploredPartIds.asStateFlow()

    private val _isMicroscopeMode = MutableStateFlow(false)
    val isMicroscopeMode: StateFlow<Boolean> = _isMicroscopeMode.asStateFlow()

    // Growth Lab State
    private val _selectedPlantType = MutableStateFlow("SUNFLOWER")
    val selectedPlantType: StateFlow<String> = _selectedPlantType.asStateFlow()

    private val _growthStage = MutableStateFlow(GrowthStage.SEED)
    val growthStage: StateFlow<GrowthStage> = _growthStage.asStateFlow()

    private val _soilMoisture = MutableStateFlow(20f)
    val soilMoisture: StateFlow<Float> = _soilMoisture.asStateFlow()

    private val _sunshineLevel = MutableStateFlow(30f)
    val sunshineLevel: StateFlow<Float> = _sunshineLevel.asStateFlow()

    private val _labMessage = MutableStateFlow("Tap 'Add Soil' to tuck the little seed safely in bed!")
    val labMessage: StateFlow<String> = _labMessage.asStateFlow()

    private val _isHarvested = MutableStateFlow(false)
    val isHarvested: StateFlow<Boolean> = _isHarvested.asStateFlow()

    private val _currentWeather = MutableStateFlow(WeatherCondition.SUNNY)
    val currentWeather: StateFlow<WeatherCondition> = _currentWeather.asStateFlow()

    private val _currentHealth = MutableStateFlow(PlantHealthStatus.HEALTHY)
    val currentHealth: StateFlow<PlantHealthStatus> = _currentHealth.asStateFlow()

    private val _selectedPotStyle = MutableStateFlow(PotStyle.TERRACOTTA)
    val selectedPotStyle: StateFlow<PotStyle> = _selectedPotStyle.asStateFlow()

    // Photosynthesis Magic Kitchen State
    private val _hasSunlight = MutableStateFlow(false)
    val hasSunlight: StateFlow<Boolean> = _hasSunlight.asStateFlow()

    private val _hasWater = MutableStateFlow(false)
    val hasWater: StateFlow<Boolean> = _hasWater.asStateFlow()

    private val _hasAir = MutableStateFlow(false)
    val hasAir: StateFlow<Boolean> = _hasAir.asStateFlow()

    private val _isCooking = MutableStateFlow(false)
    val isCooking: StateFlow<Boolean> = _isCooking.asStateFlow()

    private val _isKitchenDone = MutableStateFlow(false)
    val isKitchenDone: StateFlow<Boolean> = _isKitchenDone.asStateFlow()

    private val _poppedBubbles = MutableStateFlow(0)
    val poppedBubbles: StateFlow<Int> = _poppedBubbles.asStateFlow()

    // Seed Travel State
    private val _activeSeedFlight = MutableStateFlow<SeedTravelAdventure?>(null)
    val activeSeedFlight: StateFlow<SeedTravelAdventure?> = _activeSeedFlight.asStateFlow()

    // Plant Encyclopedia State
    private val _selectedCategory = MutableStateFlow<PlantCategory?>(null)
    val selectedCategory: StateFlow<PlantCategory?> = _selectedCategory.asStateFlow()

    private val _selectedSpecies = MutableStateFlow<PlantSpecies?>(null)
    val selectedSpecies: StateFlow<PlantSpecies?> = _selectedSpecies.asStateFlow()

    // Quiz State
    private val _quizIndex = MutableStateFlow(0)
    val quizIndex: StateFlow<Int> = _quizIndex.asStateFlow()

    private val _selectedQuizOption = MutableStateFlow<Int?>(null)
    val selectedQuizOption: StateFlow<Int?> = _selectedQuizOption.asStateFlow()

    private val _isQuizAnswerChecked = MutableStateFlow(false)
    val isQuizAnswerChecked: StateFlow<Boolean> = _isQuizAnswerChecked.asStateFlow()

    private val _quizStarsEarned = MutableStateFlow(0)
    val quizStarsEarned: StateFlow<Int> = _quizStarsEarned.asStateFlow()

    private val _quizFinished = MutableStateFlow(false)
    val quizFinished: StateFlow<Boolean> = _quizFinished.asStateFlow()

    // Navigation functions
    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
        stopAudio()
    }

    // Audio & Speech
    fun speak(text: String) {
        speechHelper.speak(text)
    }

    fun stopAudio() {
        speechHelper.stop()
    }

    // Anatomy actions
    fun selectPlantPart(partId: String) {
        _selectedPartId.value = partId
        val updated = _exploredPartIds.value + partId
        _exploredPartIds.value = updated

        val part = plantParts.find { it.id == partId }
        part?.let {
            if (_isMicroscopeMode.value) {
                speak("Microscope Lens on ${it.name}! ${it.microscopeTitle}. ${it.microscopeExplanation}")
            } else {
                speak("${it.name}! ${it.roleTitle}. ${it.kidExplanation}")
            }
        }

        if (updated.size >= plantParts.size) {
            viewModelScope.launch {
                repository.markAnatomyCompleted()
                triggerConfetti()
            }
        }
    }

    fun toggleMicroscopeMode() {
        _isMicroscopeMode.value = !_isMicroscopeMode.value
        val currentPart = plantParts.find { it.id == _selectedPartId.value } ?: plantParts.first()
        if (_isMicroscopeMode.value) {
            viewModelScope.launch {
                repository.markMicroscopeCompleted()
            }
            speak("Microscope Lens Activated! Look closely at ${currentPart.microscopeTitle}!")
        } else {
            speak("Returning to Whole Plant Anatomy View!")
        }
    }

    // Growth Lab actions
    fun selectPlantTypeToGrow(type: String) {
        _selectedPlantType.value = type
        resetGrowthLab()
        val plantName = when (type) {
            "SUNFLOWER" -> "Happy Sunflower"
            "STRAWBERRY" -> "Juicy Strawberry"
            "CACTUS" -> "Spike the Cactus"
            else -> "Blooming Rose"
        }
        speak("Let's grow a $plantName! Tuck the seed in the dirt first.")
    }

    fun setWeather(weather: WeatherCondition) {
        _currentWeather.value = weather
        when (weather) {
            WeatherCondition.SUNNY -> {
                _sunshineLevel.value = (_sunshineLevel.value + 20f).coerceAtMost(100f)
                speak("Sun is shining bright! Photosynthesis is active!")
            }
            WeatherCondition.RAINY -> {
                _soilMoisture.value = (_soilMoisture.value + 40f).coerceAtMost(100f)
                speak("Pitter patter raindrops! Rain is soaking the soil naturally!")
            }
            WeatherCondition.WINDY -> {
                speak("Whoosh! Fresh breeze rustles the plant stems and spreads pollen!")
            }
            WeatherCondition.NIGHT -> {
                speak("Starlight night! The flower folds its petals to rest until sunrise.")
            }
        }
        viewModelScope.launch {
            repository.unlockBadge("WEATHER_WIZARD")
        }
        checkGrowthStep()
    }

    fun selectPotStyle(potStyle: PotStyle) {
        _selectedPotStyle.value = potStyle
        speak("New pot chosen: ${potStyle.label}! Looks wonderful!")
    }

    fun triggerRandomCheckup() {
        val issues = listOf(PlantHealthStatus.THIRSTY, PlantHealthStatus.CATERPILLAR, PlantHealthStatus.SUNBURNED)
        _currentHealth.value = issues.random()
        speak("Doctor alert! Your plant needs a checkup: ${_currentHealth.value.label}!")
    }

    fun curePlant() {
        val old = _currentHealth.value
        _currentHealth.value = PlantHealthStatus.HEALTHY
        viewModelScope.launch {
            repository.markPlantDoctorCompleted()
            triggerConfetti()
        }
        speak("All better! Your plant is healthy, green, and smiling again! Great job Plant Doctor! +15 Stars!")
    }

    fun waterPlantInLab() {
        val newMoisture = (_soilMoisture.value + 35f).coerceAtMost(100f)
        _soilMoisture.value = newMoisture
        if (_currentHealth.value == PlantHealthStatus.THIRSTY) {
            _currentHealth.value = PlantHealthStatus.HEALTHY
            speak("Splish splash! The thirsty plant drank fresh water and perked right up!")
        } else {
            speak("Splish splash! The soil drank fresh water!")
        }
        checkGrowthStep()
    }

    fun addSunshineInLab() {
        val newSun = (_sunshineLevel.value + 35f).coerceAtMost(100f)
        _sunshineLevel.value = newSun
        speak("Warm and sunny! The plant is soaking up sunbeams!")
        checkGrowthStep()
    }

    fun advanceGrowthStage() {
        when (_growthStage.value) {
            GrowthStage.SEED -> {
                _growthStage.value = GrowthStage.SPROUT
                _labMessage.value = "Look! A tiny green sprout popped up! Give it water to drink!"
                speak(_labMessage.value)
            }
            GrowthStage.SPROUT -> {
                _growthStage.value = GrowthStage.SEEDLING
                _labMessage.value = "Great job! It grew into a strong seedling! Give it warm sunshine!"
                speak(_labMessage.value)
            }
            GrowthStage.SEEDLING -> {
                _growthStage.value = GrowthStage.BLOOMING
                _labMessage.value = "Hooray! Big beautiful flowers opened! Friendly bees are buzzing!"
                speak(_labMessage.value)
            }
            GrowthStage.BLOOMING -> {
                _growthStage.value = GrowthStage.HARVEST
                _labMessage.value = "Ta-da! Your plant is fully grown and ready for your Botanical Garden!"
                speak(_labMessage.value)
                triggerConfetti()
            }
            GrowthStage.HARVEST -> {
                harvestCurrentPlant()
            }
        }
    }

    private fun checkGrowthStep() {
        if (_soilMoisture.value >= 50f && _sunshineLevel.value >= 50f) {
            _soilMoisture.value = 25f
            _sunshineLevel.value = 25f
            advanceGrowthStage()
        }
    }

    fun harvestCurrentPlant() {
        if (_isHarvested.value) return
        _isHarvested.value = true
        val type = _selectedPlantType.value
        val (name, emoji, height) = when (type) {
            "SUNFLOWER" -> Triple("Sunny", "🌻", 95)
            "STRAWBERRY" -> Triple("Sweetie Berry", "🍓", 20)
            "CACTUS" -> Triple("Spikey", "🌵", 45)
            else -> Triple("Rosy", "🌹", 35)
        }
        viewModelScope.launch {
            repository.addPlantToGarden(type, name, emoji, height)
            repository.unlockBadge("SEED_SCOUT")
            repository.unlockBadge("MASTER_GARDENER")
            triggerConfetti()
            speak("Harvested! $name has moved to your Little Botanical Garden! You earned 10 stars!")
        }
    }

    fun resetGrowthLab() {
        _growthStage.value = GrowthStage.SEED
        _soilMoisture.value = 20f
        _sunshineLevel.value = 25f
        _currentHealth.value = PlantHealthStatus.HEALTHY
        _isHarvested.value = false
        _labMessage.value = "Give water and sunshine to help your seed grow!"
    }

    // Seed Travel
    fun launchSeedTravel(adventure: SeedTravelAdventure) {
        _activeSeedFlight.value = adventure
        speak("Launching ${adventure.plantName} seed! ${adventure.vehicleName}! ${adventure.funStory}")
        viewModelScope.launch {
            repository.markSeedTravelCompleted()
            triggerConfetti()
        }
    }

    fun dismissSeedFlight() {
        _activeSeedFlight.value = null
    }

    // Photosynthesis Kitchen actions
    fun toggleSunlight() {
        _hasSunlight.value = !_hasSunlight.value
        if (_hasSunlight.value) speak("Sunlight added! Warm solar energy!")
        checkPhotosynthesisReady()
    }

    fun toggleWater() {
        _hasWater.value = !_hasWater.value
        if (_hasWater.value) speak("Water added! Slurped up from the roots!")
        checkPhotosynthesisReady()
    }

    fun toggleAir() {
        _hasAir.value = !_hasAir.value
        if (_hasAir.value) speak("Air added! Carbon dioxide from the breeze!")
        checkPhotosynthesisReady()
    }

    private fun checkPhotosynthesisReady() {
        if (_hasSunlight.value && _hasWater.value && _hasAir.value && !_isKitchenDone.value && !_isCooking.value) {
            cookPhotosynthesis()
        }
    }

    fun cookPhotosynthesis() {
        viewModelScope.launch {
            _isCooking.value = true
            speak("Mixing sunlight, water, and air in the leaf's kitchen...")
            delay(1600)
            _isCooking.value = false
            _isKitchenDone.value = true
            repository.markPhotosynthesisCompleted()
            triggerConfetti()
            speak("Magic! Photosynthesis produced sweet plant sugar and fresh oxygen bubbles for us to breathe! Tap the bubbles to pop them!")
        }
    }

    fun popOxygenBubble() {
        _poppedBubbles.value = _poppedBubbles.value + 1
        viewModelScope.launch {
            repository.addStars(1)
        }
    }

    fun resetPhotosynthesisKitchen() {
        _hasSunlight.value = false
        _hasWater.value = false
        _hasAir.value = false
        _isCooking.value = false
        _isKitchenDone.value = false
        _poppedBubbles.value = 0
    }

    // Encyclopedia actions
    fun filterCategory(category: PlantCategory?) {
        _selectedCategory.value = category
    }

    fun selectSpecies(species: PlantSpecies?) {
        _selectedSpecies.value = species
        species?.let {
            speak("${it.name}! ${it.simpleDescription} Superpower: ${it.superpower}")
        }
    }

    // Quiz actions
    fun selectQuizOption(index: Int) {
        if (_isQuizAnswerChecked.value) return
        _selectedQuizOption.value = index
        _isQuizAnswerChecked.value = true

        val currentQ = quizQuestions[_quizIndex.value]
        val correct = index == currentQ.correctIndex

        if (correct) {
            _quizStarsEarned.value = _quizStarsEarned.value + 3
            triggerConfetti()
            speak("Correct! ${currentQ.praiseMessage}")
            viewModelScope.launch {
                repository.addStars(3)
                if (_quizStarsEarned.value >= 9) {
                    repository.unlockBadge("QUIZ_GENIUS")
                }
            }
        } else {
            speak("Nice try! Here's a tip: ${currentQ.hint}")
        }
    }

    fun nextQuizQuestion() {
        if (_quizIndex.value < quizQuestions.size - 1) {
            _quizIndex.value = _quizIndex.value + 1
            _selectedQuizOption.value = null
            _isQuizAnswerChecked.value = false
            speak(quizQuestions[_quizIndex.value].question)
        } else {
            _quizFinished.value = true
            triggerConfetti()
            speak("Hooray! You finished the Plant Quiz with flying colors! Great botanist!")
        }
    }

    fun restartQuiz() {
        _quizIndex.value = 0
        _selectedQuizOption.value = null
        _isQuizAnswerChecked.value = false
        _quizStarsEarned.value = 0
        _quizFinished.value = false
        speak(quizQuestions[0].question)
    }

    // Garden actions
    fun waterGardenPlant(plant: GrownPlantEntity) {
        viewModelScope.launch {
            repository.waterGardenPlant(plant)
            triggerConfetti()
            speak("Yum! ${plant.name} loved the fresh drink of water! +2 Stars!")
        }
    }

    private fun triggerConfetti() {
        _showConfetti.value = true
    }

    fun dismissConfetti() {
        _showConfetti.value = false
    }

    override fun onCleared() {
        super.onCleared()
        speechHelper.shutdown()
    }
}
