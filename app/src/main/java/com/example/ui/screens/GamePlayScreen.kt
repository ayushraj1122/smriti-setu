package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FontSizeScale
import com.example.data.model.GameCatalog
import com.example.data.model.GameQuestion
import com.example.data.model.GameQuestionGenerator
import com.example.data.model.MemoryCard
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibleButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GamePlayScreen(
    gameId: String,
    level: Int,
    currentLanguage: String,
    fontSizeScale: FontSizeScale,
    highContrast: Boolean,
    largeButtons: Boolean,
    voiceEnabled: Boolean,
    onSpeak: (String) -> Unit,
    onBackToGames: () -> Unit,
    onCompleteGame: (
        totalQuestions: Int,
        correctAnswers: Int,
        responseTimesMs: List<Long>
    ) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val gameDef = remember(gameId) { GameCatalog.getById(gameId) }
    val isMemoryMatch = gameId == "memory_match"

    var isPaused by remember { mutableStateOf(false) }

    val bg = if (highContrast) Color(0xFF0F172A) else com.example.ui.theme.VibrantBg
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White
    val textPrimary = if (highContrast) Color.White else com.example.ui.theme.VibrantTextPrimary

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantBluePrimary
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onBackToGames() },
                            modifier = Modifier.testTag("btn_game_back")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Games",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = gameDef?.let { StringsProvider.get(it.titleKey, currentLanguage) } ?: "Game",
                            fontSize = (18f * fontSizeScale.scale).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Level $level",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(
                            onClick = { isPaused = !isPaused },
                            modifier = Modifier.testTag("btn_game_pause")
                        ) {
                            Icon(
                                imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (isPaused) "Resume Game" else "Pause Game",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        },
        containerColor = bg
    ) { innerPadding ->
        if (isPaused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Game Paused",
                            fontSize = (22f * fontSizeScale.scale).sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Take your time. Whenever you feel comfortable, tap Resume.",
                            fontSize = (14f * fontSizeScale.scale).sp,
                            color = textPrimary.copy(alpha = 0.75f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        AccessibleButton(
                            text = "Resume Game",
                            onClick = { isPaused = false },
                            icon = Icons.Default.PlayArrow,
                            isPrimary = true,
                            highContrast = highContrast,
                            fontSizeScale = fontSizeScale
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AccessibleButton(
                            text = "Exit to Games",
                            onClick = { onBackToGames() },
                            isOutlined = true,
                            highContrast = highContrast,
                            fontSizeScale = fontSizeScale
                        )
                    }
                }
            }
        } else if (isMemoryMatch) {
            MemoryMatchGameContent(
                level = level,
                innerPadding = innerPadding,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                currentLanguage = currentLanguage,
                onSpeak = onSpeak,
                onComplete = { correct, total, times ->
                    onCompleteGame(total, correct, times)
                }
            )
        } else {
            StandardQuestionGameContent(
                gameId = gameId,
                level = level,
                innerPadding = innerPadding,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                largeButtons = largeButtons,
                currentLanguage = currentLanguage,
                onSpeak = onSpeak,
                onComplete = { correct, total, times ->
                    onCompleteGame(total, correct, times)
                }
            )
        }
    }
}

@Composable
private fun StandardQuestionGameContent(
    gameId: String,
    level: Int,
    innerPadding: androidx.compose.foundation.layout.PaddingValues,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale,
    largeButtons: Boolean,
    currentLanguage: String,
    onSpeak: (String) -> Unit,
    onComplete: (correct: Int, total: Int, times: List<Long>) -> Unit
) {
    val questions = remember(gameId, level) {
        GameQuestionGenerator.generateQuestions(gameId, level)
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    val responseTimes = remember { mutableStateListOf<Long>() }

    var questionStartTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val currentQ = questions.getOrNull(currentIndex)

    LaunchedEffect(currentIndex) {
        questionStartTime = System.currentTimeMillis()
        selectedOptionIndex = null
        feedbackMessage = null
        isAnswerSubmitted = false
        if (currentQ != null) {
            onSpeak(currentQ.promptTextFallback)
        }
    }

    if (currentQ == null) {
        // Complete
        LaunchedEffect(Unit) {
            onComplete(correctCount, questions.size, responseTimes.toList())
        }
        return
    }

    val bg = if (highContrast) Color(0xFF0F172A) else Color(0xFFF8FAFC)
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress bar indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${currentIndex + 1} of ${questions.size}",
                fontSize = (14f * fontSizeScale.scale).sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary.copy(alpha = 0.8f)
            )

            // Audio speak prompt button
            IconButton(
                onClick = { onSpeak(currentQ.promptTextFallback) },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (highContrast) Color(0xFF334155) else com.example.ui.theme.VibrantActionBtnBg)
                    .testTag("btn_speak_prompt")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Read question aloud",
                    tint = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = if (highContrast) BorderStroke(2.dp, Color.White) else BorderStroke(1.dp, com.example.ui.theme.VibrantBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentQ.visualEmoji.isNotBlank()) {
                    Text(
                        text = currentQ.visualEmoji,
                        fontSize = 44.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Text(
                    text = currentQ.promptTextFallback,
                    fontSize = (20f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Answer Options
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            currentQ.options.forEachIndexed { index, optionText ->
                val isSelected = selectedOptionIndex == index
                val isCorrect = index == currentQ.correctIndex

                val optionBg = when {
                    isAnswerSubmitted && isCorrect -> if (highContrast) Color(0xFF14532D) else com.example.ui.theme.VibrantGreenContainer
                    isAnswerSubmitted && isSelected && !isCorrect -> if (highContrast) Color(0xFF7F1D1D) else com.example.ui.theme.VibrantAlertContainer
                    isSelected -> if (highContrast) Color(0xFF334155) else com.example.ui.theme.VibrantBlueContainer
                    else -> cardBg
                }

                val optionBorder = when {
                    isAnswerSubmitted && isCorrect -> if (highContrast) Color(0xFF4ADE80) else com.example.ui.theme.VibrantGreenPrimary
                    isAnswerSubmitted && isSelected && !isCorrect -> if (highContrast) Color(0xFFF87171) else com.example.ui.theme.VibrantAlertRed
                    isSelected -> if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                    else -> if (highContrast) Color(0xFF475569) else com.example.ui.theme.VibrantBorder
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = optionBg,
                    border = BorderStroke(if (highContrast) 2.dp else 1.5.dp, optionBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(enabled = !isAnswerSubmitted) {
                            val elapsed = System.currentTimeMillis() - questionStartTime
                            responseTimes.add(elapsed)
                            selectedOptionIndex = index
                            isAnswerSubmitted = true

                            if (index == currentQ.correctIndex) {
                                correctCount++
                                feedbackMessage = StringsProvider.get("msg_good_job", currentLanguage)
                                onSpeak(feedbackMessage ?: "")
                            } else {
                                feedbackMessage = StringsProvider.get("msg_try_again", currentLanguage)
                                onSpeak(feedbackMessage ?: "")
                            }

                            coroutineScope.launch {
                                delay(1600)
                                if (currentIndex + 1 < questions.size) {
                                    currentIndex++
                                } else {
                                    onComplete(correctCount, questions.size, responseTimes.toList())
                                }
                            }
                        }
                        .testTag("option_$index")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = if (largeButtons) 18.dp else 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = optionText,
                            fontSize = (18f * fontSizeScale.scale).sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textPrimary,
                            modifier = Modifier.weight(1f)
                        )

                        if (isAnswerSubmitted) {
                            if (isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = if (highContrast) Color(0xFF4ADE80) else Color(0xFF16A34A),
                                    modifier = Modifier.size(28.dp)
                                )
                            } else if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Incorrect",
                                    tint = if (highContrast) Color(0xFFF87171) else Color(0xFFDC2626),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Calming feedback banner
        AnimatedVisibility(
            visible = !feedbackMessage.isNullOrBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (highContrast) Color(0xFF1E293B) else Color(0xFFF0FDF4),
                border = BorderStroke(
                    1.dp,
                    if (highContrast) Color(0xFFFACC15) else Color(0xFF86EFAC)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = feedbackMessage ?: "",
                    fontSize = (16f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = if (highContrast) Color(0xFFFACC15) else Color(0xFF15803D),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }
    }
}

@Composable
private fun MemoryMatchGameContent(
    level: Int,
    innerPadding: androidx.compose.foundation.layout.PaddingValues,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale,
    currentLanguage: String,
    onSpeak: (String) -> Unit,
    onComplete: (correct: Int, total: Int, times: List<Long>) -> Unit
) {
    val initialCards = remember(level) {
        GameQuestionGenerator.generateMemoryMatchCards(level)
    }

    val cards = remember { mutableStateListOf<MemoryCard>().apply { addAll(initialCards) } }
    var flippedFirstIndex by remember { mutableStateOf<Int?>(null) }
    var flippedSecondIndex by remember { mutableStateOf<Int?>(null) }
    var pairsMatched by remember { mutableIntStateOf(0) }
    var totalFlips by remember { mutableIntStateOf(0) }
    val totalPairs = initialCards.size / 2

    val startTime = remember { mutableLongStateOf(System.currentTimeMillis()) }
    var isCheckingMatch by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onSpeak("Find matching pairs of cards. Take your time.")
    }

    LaunchedEffect(pairsMatched) {
        if (pairsMatched == totalPairs && totalPairs > 0) {
            val totalTime = System.currentTimeMillis() - startTime.longValue
            delay(800)
            onSpeak("Excellent memory match! All pairs found.")
            delay(800)
            onComplete(totalPairs, totalPairs, listOf(totalTime))
        }
    }

    val bg = if (highContrast) Color(0xFF0F172A) else Color(0xFFF8FAFC)
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pairs Found: $pairsMatched / $totalPairs",
                fontSize = (16f * fontSizeScale.scale).sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Text(
                text = "Flips: $totalFlips",
                fontSize = (14f * fontSizeScale.scale).sp,
                color = textPrimary.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid of cards
        val columns = 2
        val rows = (cards.size + columns - 1) / columns

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (r in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (c in 0 until columns) {
                        val index = r * columns + c
                        if (index < cards.size) {
                            val card = cards[index]
                            val isFlipped = card.isFaceUp || card.isMatched

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (card.isMatched) {
                                    if (highContrast) Color(0xFF14532D) else Color(0xFFDCFCE7)
                                } else if (isFlipped) {
                                    if (highContrast) Color(0xFF1E293B) else Color.White
                                } else {
                                    if (highContrast) Color(0xFF334155) else Color(0xFF0D5C75)
                                },
                                border = BorderStroke(
                                    2.dp,
                                    if (card.isMatched) {
                                        if (highContrast) Color(0xFF4ADE80) else Color(0xFF16A34A)
                                    } else if (highContrast) {
                                        Color(0xFFFACC15)
                                    } else {
                                        Color(0xFFCBD5E1)
                                    }
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable(enabled = !isFlipped && !isCheckingMatch) {
                                        totalFlips++
                                        if (flippedFirstIndex == null) {
                                            flippedFirstIndex = index
                                            cards[index] = card.copy(isFaceUp = true)
                                        } else if (flippedSecondIndex == null && index != flippedFirstIndex) {
                                            flippedSecondIndex = index
                                            cards[index] = card.copy(isFaceUp = true)
                                            isCheckingMatch = true

                                            coroutineScope.launch {
                                                val firstIdx = flippedFirstIndex!!
                                                val secondIdx = index
                                                delay(800)

                                                if (cards[firstIdx].content == cards[secondIdx].content) {
                                                    cards[firstIdx] = cards[firstIdx].copy(isMatched = true)
                                                    cards[secondIdx] = cards[secondIdx].copy(isMatched = true)
                                                    pairsMatched++
                                                    onSpeak("Pair matched!")
                                                } else {
                                                    cards[firstIdx] = cards[firstIdx].copy(isFaceUp = false)
                                                    cards[secondIdx] = cards[secondIdx].copy(isFaceUp = false)
                                                }
                                                flippedFirstIndex = null
                                                flippedSecondIndex = null
                                                isCheckingMatch = false
                                            }
                                        }
                                    }
                                    .testTag("memory_card_$index")
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isFlipped) {
                                        Text(
                                            text = card.content,
                                            fontSize = 38.sp
                                        )
                                    } else {
                                        Text(
                                            text = "❓",
                                            fontSize = 28.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
