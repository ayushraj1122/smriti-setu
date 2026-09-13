package com.example.data.model

data class GameDefinition(
    val id: String,
    val titleKey: String,
    val subtitleKey: String,
    val category: GameCategory,
    val iconEmoji: String,
    val totalLevels: Int = 3
)

data class GameQuestion(
    val id: String,
    val promptKey: String,
    val promptTextFallback: String,
    val visualEmoji: String = "",
    val options: List<String>,
    val correctIndex: Int,
    val explanationKey: String = ""
)

data class MemoryCard(
    val id: Int,
    val content: String,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
)

data class SequenceItem(
    val emoji: String,
    val label: String
)
