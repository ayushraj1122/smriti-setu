package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.DemoDataSeeder
import com.example.data.model.AppLanguage
import com.example.data.model.FontSizeScale
import com.example.data.model.GameAttemptEntity
import com.example.data.model.GameCategory
import com.example.data.model.ReminderEntity
import com.example.data.model.RoutineItemEntity
import com.example.data.model.SettingsEntity
import com.example.data.model.UserEntity
import com.example.data.repository.SmritiRepository
import com.example.engine.AdaptiveEngine
import com.example.engine.ScoringEngine
import com.example.engine.TtsManager
import com.example.i18n.StringsProvider
import com.example.ui.navigation.CaregiverTab
import com.example.ui.navigation.PatientTab
import com.example.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = SmritiRepository(db)
    val ttsManager = TtsManager(application)

    // Current Navigation Screen
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Auth & Users
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _linkedPatient = MutableStateFlow<UserEntity?>(null)
    val linkedPatient: StateFlow<UserEntity?> = _linkedPatient.asStateFlow()

    // Preferences & Accessibility
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH.code)
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _highContrast = MutableStateFlow(false)
    val highContrast: StateFlow<Boolean> = _highContrast.asStateFlow()

    private val _fontSizeScale = MutableStateFlow(FontSizeScale.STANDARD)
    val fontSizeScale: StateFlow<FontSizeScale> = _fontSizeScale.asStateFlow()

    private val _largeButtons = MutableStateFlow(true)
    val largeButtons: StateFlow<Boolean> = _largeButtons.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _voiceEnabled = MutableStateFlow(true)
    val voiceEnabled: StateFlow<Boolean> = _voiceEnabled.asStateFlow()

    // Games filter
    private val _selectedGameCategory = MutableStateFlow(GameCategory.ALL)
    val selectedGameCategory: StateFlow<GameCategory> = _selectedGameCategory.asStateFlow()

    // Patient active tab
    private val _patientTab = MutableStateFlow(PatientTab.HOME)
    val patientTab: StateFlow<PatientTab> = _patientTab.asStateFlow()

    // Caregiver active tab
    private val _caregiverTab = MutableStateFlow(CaregiverTab.DASHBOARD)
    val caregiverTab: StateFlow<CaregiverTab> = _caregiverTab.asStateFlow()

    // Dynamic Reactive Data for Currently Monitored Patient
    val targetPatientIdFlow = MutableStateFlow<Long?>(null)

    val gameAttempts: StateFlow<List<GameAttemptEntity>> = targetPatientIdFlow.flatMapLatest { id ->
        if (id != null) repository.getAttemptsForUser(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val reminders: StateFlow<List<ReminderEntity>> = targetPatientIdFlow.flatMapLatest { id ->
        if (id != null) repository.getRemindersForPatient(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val dailyRoutine: StateFlow<List<RoutineItemEntity>> = targetPatientIdFlow.flatMapLatest { id ->
        if (id != null) repository.getRoutineForPatient(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allPatients: StateFlow<List<UserEntity>> = repository.getAllPatients()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Feedback message banner
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    init {
        viewModelScope.launch {
            DemoDataSeeder.seedDatabaseIfEmpty(db)
        }
    }

    fun navigateTo(screen: Screen) {
        ttsManager.stop()
        _currentScreen.value = screen
    }

    fun setPatientTab(tab: PatientTab) {
        _patientTab.value = tab
    }

    fun setCaregiverTab(tab: CaregiverTab) {
        _caregiverTab.value = tab
    }

    fun setGameCategory(category: GameCategory) {
        _selectedGameCategory.value = category
    }

    fun speakText(text: String) {
        if (_voiceEnabled.value) {
            ttsManager.speak(text, _currentLanguage.value)
        }
    }

    fun stopSpeaking() {
        ttsManager.stop()
    }

    fun setLanguage(code: String) {
        _currentLanguage.value = code
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val updated = user.copy(preferredLanguage = code)
            repository.updateUser(updated)
            _currentUser.value = updated
        }
    }

    fun toggleHighContrast(enabled: Boolean) {
        _highContrast.value = enabled
    }

    fun setFontSize(scale: FontSizeScale) {
        _fontSizeScale.value = scale
    }

    fun toggleLargeButtons(enabled: Boolean) {
        _largeButtons.value = enabled
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun toggleVoiceEnabled(enabled: Boolean) {
        _voiceEnabled.value = enabled
        ttsManager.setMuted(!enabled)
    }

    // Demo Logins
    fun quickLoginDemoPatient() {
        viewModelScope.launch {
            val patient = repository.getUserByEmail("patient@demo.com")
            if (patient != null) {
                _currentUser.value = patient
                _currentLanguage.value = patient.preferredLanguage
                targetPatientIdFlow.value = patient.id
                _patientTab.value = PatientTab.HOME
                _currentScreen.value = Screen.PatientMain()
                speakText("Welcome back, Arun! Let's enjoy some light cognitive games today.")
            }
        }
    }

    fun quickLoginDemoCaregiver() {
        viewModelScope.launch {
            val caregiver = repository.getUserByEmail("caregiver@demo.com")
            if (caregiver != null) {
                _currentUser.value = caregiver
                _currentLanguage.value = caregiver.preferredLanguage
                val patient = caregiver.linkedPatientId?.let { repository.getUserById(it) }
                    ?: repository.getUserByEmail("patient@demo.com")
                _linkedPatient.value = patient
                targetPatientIdFlow.value = patient?.id
                _caregiverTab.value = CaregiverTab.DASHBOARD
                _currentScreen.value = Screen.CaregiverMain()
            }
        }
    }

    fun loginPatient(email: String, pass: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email.trim().lowercase())
            if (user != null && user.role == "PATIENT" && user.passwordHash == pass) {
                _currentUser.value = user
                _currentLanguage.value = user.preferredLanguage
                targetPatientIdFlow.value = user.id
                _patientTab.value = PatientTab.HOME
                _currentScreen.value = Screen.PatientMain()
                speakText("Welcome, ${user.fullName}!")
            } else {
                onError("Please check your email and password, or tap Quick Demo.")
            }
        }
    }

    fun signupPatient(
        name: String,
        age: Int,
        gender: String,
        language: String,
        state: String,
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val existing = repository.getUserByEmail(email.trim().lowercase())
            if (existing != null) {
                onError("An account with this email already exists.")
                return@launch
            }
            val randomCode = "NER-${(1000..9999).random()}"
            val newUser = UserEntity(
                email = email.trim().lowercase(),
                passwordHash = pass,
                fullName = name.trim(),
                role = "PATIENT",
                age = age,
                gender = gender,
                preferredLanguage = language,
                locationState = state,
                patientCode = randomCode
            )
            val newId = repository.insertUser(newUser)
            _currentUser.value = newUser.copy(id = newId)
            _currentLanguage.value = language
            targetPatientIdFlow.value = newId
            onSuccess()
        }
    }

    fun loginCaregiver(email: String, pass: String, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email.trim().lowercase())
            if (user != null && user.role == "CAREGIVER" && user.passwordHash == pass) {
                _currentUser.value = user
                _currentLanguage.value = user.preferredLanguage
                val patient = user.linkedPatientId?.let { repository.getUserById(it) }
                    ?: repository.getUserByEmail("patient@demo.com")
                _linkedPatient.value = patient
                targetPatientIdFlow.value = patient?.id
                _caregiverTab.value = CaregiverTab.DASHBOARD
                _currentScreen.value = Screen.CaregiverMain()
            } else {
                onError("Caregiver account not found or password incorrect. You can use Quick Demo.")
            }
        }
    }

    fun linkPatientByCode(code: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val patient = repository.getPatientByCode(code.trim().uppercase())
            if (patient != null) {
                val currentCaregiver = _currentUser.value ?: return@launch
                val updated = currentCaregiver.copy(linkedPatientId = patient.id)
                repository.updateUser(updated)
                _currentUser.value = updated
                _linkedPatient.value = patient
                targetPatientIdFlow.value = patient.id
                onResult(true, "Successfully linked to ${patient.fullName} (${patient.locationState})")
            } else {
                onResult(false, "Patient code not found. Please verify the code (e.g. NER-6842).")
            }
        }
    }

    fun selectMonitoredPatient(patient: UserEntity) {
        _linkedPatient.value = patient
        targetPatientIdFlow.value = patient.id
    }

    fun logout() {
        ttsManager.stop()
        _currentUser.value = null
        _linkedPatient.value = null
        targetPatientIdFlow.value = null
        _currentScreen.value = Screen.Welcome
    }

    // Record Game Attempt
    fun recordGameAttempt(
        gameId: String,
        category: String,
        level: Int,
        totalQuestions: Int,
        correctAnswers: Int,
        responseTimesMs: List<Long>,
        onSaved: (GameAttemptEntity) -> Unit
    ) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val scoreResult = ScoringEngine.calculate(totalQuestions, correctAnswers, responseTimesMs)
            val recommendation = AdaptiveEngine.getRecommendation(
                currentLevel = level,
                accuracyPercent = scoreResult.accuracyPercent,
                avgResponseTimeMs = scoreResult.avgResponseTimeMs
            )

            val attempt = GameAttemptEntity(
                userId = user.id,
                gameId = gameId,
                gameCategory = category,
                level = level,
                timestamp = System.currentTimeMillis(),
                totalQuestions = scoreResult.totalQuestions,
                correctAnswers = scoreResult.correctAnswers,
                incorrectAnswers = scoreResult.incorrectAnswers,
                accuracyPercent = scoreResult.accuracyPercent,
                avgResponseTimeMs = scoreResult.avgResponseTimeMs,
                totalScore = scoreResult.calculatedScore,
                recommendationMessage = recommendation.recommendationText
            )

            val attemptId = repository.recordGameAttempt(attempt)
            onSaved(attempt.copy(id = attemptId))
        }
    }

    // Reminders
    fun toggleReminder(id: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleReminder(id, completed)
        }
    }

    fun addReminder(title: String, description: String, category: String, timeStr: String) {
        val patientId = targetPatientIdFlow.value ?: return
        viewModelScope.launch {
            val reminder = ReminderEntity(
                patientId = patientId,
                title = title.trim(),
                description = description.trim(),
                category = category,
                timeStr = timeStr,
                dateStr = "Daily",
                repeatFrequency = "DAILY",
                isActive = true,
                isCompleted = false
            )
            repository.addReminder(reminder)
        }
    }

    fun deleteReminder(id: Long) {
        viewModelScope.launch {
            repository.deleteReminder(id)
        }
    }

    // Daily Routine
    fun toggleRoutineItem(id: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleRoutineItem(id, completed)
        }
    }

    fun addRoutineItem(period: String, title: String, description: String, timeStr: String) {
        val patientId = targetPatientIdFlow.value ?: return
        viewModelScope.launch {
            val item = RoutineItemEntity(
                patientId = patientId,
                period = period,
                timeStr = timeStr,
                title = title.trim(),
                description = description.trim(),
                isCompleted = false,
                orderIndex = (dailyRoutine.value.size + 1)
            )
            repository.addRoutineItem(item)
        }
    }

    fun deleteRoutineItem(id: Long) {
        viewModelScope.launch {
            repository.deleteRoutineItem(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
