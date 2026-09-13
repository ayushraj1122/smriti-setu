package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Replay
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
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibleButton
import com.example.ui.components.DisclaimerBanner
import com.example.ui.components.StatCard
import java.util.Locale

@Composable
fun GameResultScreen(
    gameId: String,
    level: Int,
    score: Int,
    accuracy: Int,
    avgTimeMs: Long,
    correct: Int,
    total: Int,
    recommendation: String,
    currentLanguage: String,
    fontSizeScale: FontSizeScale,
    highContrast: Boolean,
    largeButtons: Boolean,
    voiceEnabled: Boolean,
    onSpeak: (String) -> Unit,
    onReplay: () -> Unit,
    onBackToGames: () -> Unit
) {
    val gameDef = GameCatalog.getById(gameId)
    val gameTitle = gameDef?.let { StringsProvider.get(it.titleKey, currentLanguage) } ?: "Game"
    val scrollState = rememberScrollState()

    val congratulation = if (accuracy >= 80) {
        StringsProvider.get("msg_good_job", currentLanguage)
    } else {
        StringsProvider.get("msg_improving", currentLanguage)
    }

    LaunchedEffect(Unit) {
        val speechText = "$congratulation Your accuracy was $accuracy percent. $recommendation"
        onSpeak(speechText)
    }

    val bg = if (highContrast) Color(0xFF0F172A) else com.example.ui.theme.VibrantBg
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White
    val textPrimary = if (highContrast) Color.White else com.example.ui.theme.VibrantTextPrimary

    Scaffold(
        containerColor = bg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Celebration Badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantAttentionBg),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (accuracy >= 80) "🌟" else "🌱",
                    fontSize = 40.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = congratulation,
                fontSize = (24f * fontSizeScale.scale).sp,
                fontWeight = FontWeight.Bold,
                color = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "$gameTitle • Level $level Complete",
                fontSize = (14f * fontSizeScale.scale).sp,
                color = com.example.ui.theme.VibrantTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Stat Cards Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = StringsProvider.get("label_score", currentLanguage),
                    value = "$score / 100",
                    modifier = Modifier.weight(1f),
                    emoji = "🏆",
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale
                )
                StatCard(
                    title = StringsProvider.get("label_accuracy", currentLanguage),
                    value = "$accuracy%",
                    modifier = Modifier.weight(1f),
                    subtitle = "$correct of $total correct",
                    emoji = "🎯",
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val avgSec = String.format(Locale.US, "%.1fs", avgTimeMs / 1000.0)
            StatCard(
                title = StringsProvider.get("label_response_time", currentLanguage),
                value = avgSec,
                modifier = Modifier.fillMaxWidth(),
                subtitle = "Comfortable natural pacing without rush",
                emoji = "⏱️",
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Adaptive Recommendation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(
                    1.dp,
                    if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBorder
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Next Adaptive Recommendation",
                            fontWeight = FontWeight.Bold,
                            fontSize = (15f * fontSizeScale.scale).sp,
                            color = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBluePrimary
                        )
                        IconButton(
                            onClick = { onSpeak(recommendation) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Read recommendation aloud",
                                tint = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBluePrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = recommendation,
                        fontSize = (14f * fontSizeScale.scale).sp,
                        color = textPrimary,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Actions
            AccessibleButton(
                text = StringsProvider.get("btn_replay", currentLanguage),
                onClick = onReplay,
                icon = Icons.Default.Replay,
                isPrimary = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                largeButtonMode = largeButtons,
                testTag = "btn_game_replay"
            )

            Spacer(modifier = Modifier.height(10.dp))

            AccessibleButton(
                text = StringsProvider.get("btn_home", currentLanguage),
                onClick = onBackToGames,
                icon = Icons.Default.Home,
                isOutlined = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                largeButtonMode = largeButtons,
                testTag = "btn_game_finish_home"
            )

            Spacer(modifier = Modifier.height(20.dp))

            DisclaimerBanner(
                currentLanguage = currentLanguage,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
        }
    }
}
