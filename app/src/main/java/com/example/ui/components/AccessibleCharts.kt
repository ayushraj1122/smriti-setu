package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FontSizeScale
import com.example.data.model.GameAttemptEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AccuracyTrendChart(
    attempts: List<GameAttemptEntity>,
    highContrast: Boolean = false,
    fontSizeScale: FontSizeScale = FontSizeScale.STANDARD,
    modifier: Modifier = Modifier
) {
    val displayAttempts = attempts.take(7).reversed()
    val barColor = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75)
    val gridColor = if (highContrast) Color(0xFF334155) else Color(0xFFE2E8F0)
    val textColor = if (highContrast) Color.White else Color(0xFF334155)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Accuracy Over Recent Sessions (%)",
            fontWeight = FontWeight.Bold,
            fontSize = (14f * fontSizeScale.scale).sp,
            color = textColor
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (displayAttempts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (highContrast) Color(0xFF1E293B) else Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Text("No recent game data yet.", color = textColor.copy(alpha = 0.7f))
            }
            return
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (highContrast) Color(0xFF0F172A) else Color(0xFFF8FAFC))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                displayAttempts.forEach { attempt ->
                    val heightFraction = (attempt.accuracyPercent / 100f).coerceIn(0.15f, 1f)
                    val dateLabel = SimpleDateFormat("dd MMM", Locale.ENGLISH).format(Date(attempt.timestamp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = "${attempt.accuracyPercent}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .fillMaxHeight(heightFraction * 0.78f)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    if (attempt.accuracyPercent >= 80) {
                                        if (highContrast) Color(0xFF4ADE80) else Color(0xFF16A34A)
                                    } else if (attempt.accuracyPercent >= 50) {
                                        barColor
                                    } else {
                                        if (highContrast) Color(0xFFFB923C) else Color(0xFFEA580C)
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dateLabel,
                            fontSize = 9.sp,
                            color = textColor.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResponseTimeChart(
    attempts: List<GameAttemptEntity>,
    highContrast: Boolean = false,
    fontSizeScale: FontSizeScale = FontSizeScale.STANDARD,
    modifier: Modifier = Modifier
) {
    val displayAttempts = attempts.take(6).reversed()
    val textColor = if (highContrast) Color.White else Color(0xFF334155)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Average Response Time (Seconds)",
            fontWeight = FontWeight.Bold,
            fontSize = (14f * fontSizeScale.scale).sp,
            color = textColor
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (displayAttempts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (highContrast) Color(0xFF1E293B) else Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Text("Waiting for activity sessions...", color = textColor.copy(alpha = 0.7f))
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (highContrast) Color(0xFF0F172A) else Color(0xFFF8FAFC))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            displayAttempts.forEach { attempt ->
                val seconds = (attempt.avgResponseTimeMs / 1000f)
                val safeSecStr = String.format(Locale.US, "%.1fs", seconds)
                val progressFraction = (seconds / 8.0f).coerceIn(0.1f, 1f)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = attempt.gameCategory.replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor,
                        modifier = Modifier.width(75.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(if (highContrast) Color(0xFF334155) else Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressFraction)
                                .clip(RoundedCornerShape(7.dp))
                                .background(if (highContrast) Color(0xFF38BDF8) else Color(0xFF0284C7))
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = safeSecStr,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.width(42.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBreakdownChart(
    attempts: List<GameAttemptEntity>,
    highContrast: Boolean = false,
    fontSizeScale: FontSizeScale = FontSizeScale.STANDARD,
    modifier: Modifier = Modifier
) {
    val textColor = if (highContrast) Color.White else Color(0xFF334155)
    val categories = listOf("orientation", "memory", "attention", "reasoning")

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Cognitive Category Performance",
            fontWeight = FontWeight.Bold,
            fontSize = (14f * fontSizeScale.scale).sp,
            color = textColor
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (highContrast) Color(0xFF0F172A) else Color(0xFFF8FAFC))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            categories.forEach { cat ->
                val catAttempts = attempts.filter { it.gameCategory.equals(cat, ignoreCase = true) }
                val avgAcc = if (catAttempts.isNotEmpty()) {
                    catAttempts.map { it.accuracyPercent }.average().toInt()
                } else {
                    75 // default baseline
                }
                val count = catAttempts.size

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.width(90.dp)) {
                        Text(
                            text = cat.replaceFirstChar { it.uppercase() },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = "$count sessions",
                            fontSize = 10.sp,
                            color = textColor.copy(alpha = 0.6f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (highContrast) Color(0xFF334155) else Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(avgAcc / 100f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when (cat) {
                                        "orientation" -> if (highContrast) Color(0xFFFACC15) else Color(0xFFD97706)
                                        "memory" -> if (highContrast) Color(0xFF4ADE80) else Color(0xFF16A34A)
                                        "attention" -> if (highContrast) Color(0xFF38BDF8) else Color(0xFF0284C7)
                                        else -> if (highContrast) Color(0xFFA78BFA) else Color(0xFF7C3AED)
                                    }
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$avgAcc%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.width(42.dp)
                    )
                }
            }
        }
    }
}
