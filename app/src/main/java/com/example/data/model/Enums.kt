package com.example.data.model

enum class UserRole {
    PATIENT,
    CAREGIVER
}

enum class NERState(val displayName: String) {
    ARUNACHAL_PRADESH("Arunachal Pradesh"),
    ASSAM("Assam"),
    MANIPUR("Manipur"),
    MEGHALAYA("Meghalaya"),
    MIZORAM("Mizoram"),
    NAGALAND("Nagaland"),
    SIKKIM("Sikkim"),
    TRIPURA("Tripura")
}

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    ASSAMESE("as", "Assamese", "অসমীয়া"),
    BENGALI("bn", "Bengali", "বাংলা"),
    MANIPURI("mni", "Manipuri", "মৈতৈলোন্"),
    KHASI("kha", "Khasi", "Khasi"),
    MIZO("lus", "Mizo", "Mizo ṭawng"),
    NAGAMESE("nag", "Nagamese", "Nagamese")
}

enum class GameCategory(val key: String) {
    ALL("all"),
    ORIENTATION("orientation"),
    MEMORY("memory"),
    ATTENTION("attention"),
    REASONING("reasoning")
}

enum class ReminderCategory {
    MEDICATION,
    HYDRATION,
    APPOINTMENT,
    DAILY_ROUTINE,
    EXERCISE,
    GAME_PRACTICE,
    CUSTOM
}

enum class DayPeriod {
    MORNING,
    AFTERNOON,
    EVENING
}

enum class PerformanceTrend {
    IMPROVING,
    STABLE,
    NEEDS_ATTENTION
}

enum class FontSizeScale(val scale: Float, val label: String) {
    STANDARD(1.0f, "Standard"),
    LARGE(1.2f, "Large"),
    EXTRA_LARGE(1.4f, "Extra Large")
}
