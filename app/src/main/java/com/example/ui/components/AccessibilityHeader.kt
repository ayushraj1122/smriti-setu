package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.FontSizeScale
import com.example.i18n.StringsProvider

@Composable
fun AccessibilityHeader(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    onCycleFontSize: () -> Unit,
    onSpeakCurrentContext: (() -> Unit)? = null,
    title: String? = null,
    showDemoBadge: Boolean = true,
    modifier: Modifier = Modifier
) {
    var langMenuExpanded by remember { mutableStateOf(false) }

    val btnBg = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantActionBtnBg
    val badgeBg = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBlueContainer
    val badgeTextColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantOnBlueContainer

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (highContrast) Color(0xFF0F172A) else Color.White,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title and Demo badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showDemoBadge) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(badgeBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = StringsProvider.get("demo_mode_badge", currentLanguage),
                                color = badgeTextColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    if (!title.isNullOrBlank()) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (highContrast) Color.White else com.example.ui.theme.VibrantTextPrimary
                        )
                    }
                }

                // Quick Accessibility Controls: Language, TTS, High-Contrast, Font Scale
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Language dropdown button
                    Box {
                        IconButton(
                            onClick = { langMenuExpanded = true },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(btnBg)
                                .testTag("btn_language_select")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Select Language",
                                tint = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBluePrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = langMenuExpanded,
                            onDismissRequest = { langMenuExpanded = false }
                        ) {
                            AppLanguage.values().forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = lang.nativeName,
                                                fontWeight = if (lang.code == currentLanguage) FontWeight.Bold else FontWeight.Normal
                                            )
                                            Text(
                                                text = lang.displayName,
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    },
                                    onClick = {
                                        onLanguageSelected(lang.code)
                                        langMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Voice / TTS read aloud toggle
                    IconButton(
                        onClick = {
                            if (onSpeakCurrentContext != null && voiceEnabled) {
                                onSpeakCurrentContext()
                            } else {
                                onToggleVoice()
                            }
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(btnBg)
                            .testTag("btn_toggle_voice")
                    ) {
                        Icon(
                            imageVector = if (voiceEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = if (voiceEnabled) "Voice Read-Aloud Active" else "Voice Muted",
                            tint = if (voiceEnabled) {
                                if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                            } else {
                                Color.Gray
                            },
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // High Contrast Toggle
                    IconButton(
                        onClick = onToggleHighContrast,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(btnBg)
                            .testTag("btn_toggle_contrast")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Contrast,
                            contentDescription = "Toggle High Contrast",
                            tint = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Font Size Cycle button
                    IconButton(
                        onClick = onCycleFontSize,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(btnBg)
                            .testTag("btn_cycle_font_size")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Change Font Size (${fontSizeScale.label})",
                            tint = if (highContrast) Color.White else com.example.ui.theme.VibrantTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}
