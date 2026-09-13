package com.example.data.model

object GameCatalog {
    val games = listOf(
        // Orientation
        GameDefinition(
            id = "day_date",
            titleKey = "game_day_date",
            subtitleKey = "game_day_date_desc",
            category = GameCategory.ORIENTATION,
            iconEmoji = "📅",
            totalLevels = 3
        ),
        GameDefinition(
            id = "place_time",
            titleKey = "game_place_time",
            subtitleKey = "game_place_time_desc",
            category = GameCategory.ORIENTATION,
            iconEmoji = "🏡",
            totalLevels = 3
        ),
        // Memory
        GameDefinition(
            id = "memory_match",
            titleKey = "game_memory_match",
            subtitleKey = "game_memory_match_desc",
            category = GameCategory.MEMORY,
            iconEmoji = "🃏",
            totalLevels = 3
        ),
        GameDefinition(
            id = "remember_objects",
            titleKey = "game_remember_objects",
            subtitleKey = "game_remember_objects_desc",
            category = GameCategory.MEMORY,
            iconEmoji = "🧺",
            totalLevels = 3
        ),
        // Attention
        GameDefinition(
            id = "find_target",
            titleKey = "game_find_target",
            subtitleKey = "game_find_target_desc",
            category = GameCategory.ATTENTION,
            iconEmoji = "🎯",
            totalLevels = 3
        ),
        GameDefinition(
            id = "sequence_attention",
            titleKey = "game_sequence_attention",
            subtitleKey = "game_sequence_attention_desc",
            category = GameCategory.ATTENTION,
            iconEmoji = "🔢",
            totalLevels = 3
        ),
        // Reasoning
        GameDefinition(
            id = "simple_pattern",
            titleKey = "game_simple_pattern",
            subtitleKey = "game_simple_pattern_desc",
            category = GameCategory.REASONING,
            iconEmoji = "🧩",
            totalLevels = 3
        ),
        GameDefinition(
            id = "everyday_choice",
            titleKey = "game_everyday_choice",
            subtitleKey = "game_everyday_choice_desc",
            category = GameCategory.REASONING,
            iconEmoji = "💡",
            totalLevels = 3
        )
    )

    fun getById(id: String): GameDefinition? = games.firstOrNull { it.id == id }
}
