package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FontSizeScale
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibilityHeader
import com.example.ui.components.AccessibleButton
import com.example.ui.components.DisclaimerBanner

@Composable
fun WelcomeScreen(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    onCycleFontSize: () -> Unit,
    onSpeakContext: () -> Unit,
    onPlayGames: () -> Unit,
    onPatientLogin: () -> Unit,
    onSignUp: () -> Unit,
    onCaregiverLogin: () -> Unit,
    onQuickDemoPatient: () -> Unit,
    onQuickDemoCaregiver: () -> Unit
) {
    val scrollState = rememberScrollState()
    val bg = if (highContrast) Color(0xFF0F172A) else com.example.ui.theme.VibrantBg
    val textPrimary = if (highContrast) Color.White else com.example.ui.theme.VibrantTextPrimary

    Scaffold(
        topBar = {
            AccessibilityHeader(
                currentLanguage = currentLanguage,
                onLanguageSelected = onLanguageSelected,
                voiceEnabled = voiceEnabled,
                onToggleVoice = onToggleVoice,
                highContrast = highContrast,
                onToggleHighContrast = onToggleHighContrast,
                fontSizeScale = fontSizeScale,
                onCycleFontSize = onCycleFontSize,
                onSpeakCurrentContext = onSpeakContext,
                title = StringsProvider.get("app_name", currentLanguage)
            )
        },
        containerColor = bg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Logo & Title
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantBlueContainer)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_smriti),
                    contentDescription = "Smriti App Icon",
                    modifier = Modifier
                        .size(74.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = StringsProvider.get("app_name", currentLanguage),
                fontSize = (28f * fontSizeScale.scale).sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = StringsProvider.get("app_tagline", currentLanguage),
                fontSize = (15f * fontSizeScale.scale).sp,
                color = if (highContrast) Color(0xFFCBD5E1) else com.example.ui.theme.VibrantTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Big Action: Play Games
            AccessibleButton(
                text = StringsProvider.get("btn_play_games", currentLanguage),
                onClick = onPlayGames,
                icon = Icons.Default.PlayArrow,
                isPrimary = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                testTag = "btn_welcome_play_games"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Demo Buttons (Instant access for judges)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                color = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Judge & Evaluator Quick Demo (1-Tap)",
                        fontWeight = FontWeight.Bold,
                        fontSize = (12f * fontSizeScale.scale).sp,
                        color = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AccessibleButton(
                            text = StringsProvider.get("btn_quick_demo_patient", currentLanguage),
                            onClick = onQuickDemoPatient,
                            emoji = "👤",
                            isPrimary = false,
                            highContrast = highContrast,
                            fontSizeScale = fontSizeScale,
                            modifier = Modifier.weight(1f),
                            testTag = "btn_quick_demo_patient"
                        )
                        AccessibleButton(
                            text = StringsProvider.get("btn_quick_demo_caregiver", currentLanguage),
                            onClick = onQuickDemoCaregiver,
                            emoji = "🩺",
                            isPrimary = false,
                            highContrast = highContrast,
                            fontSizeScale = fontSizeScale,
                            modifier = Modifier.weight(1f),
                            testTag = "btn_quick_demo_caregiver"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Patient Login & Sign Up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AccessibleButton(
                    text = StringsProvider.get("btn_patient_login", currentLanguage),
                    onClick = onPatientLogin,
                    icon = Icons.Default.Person,
                    isOutlined = true,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    modifier = Modifier.weight(1f),
                    testTag = "btn_welcome_patient_login"
                )

                AccessibleButton(
                    text = StringsProvider.get("btn_sign_up", currentLanguage),
                    onClick = onSignUp,
                    isOutlined = true,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    modifier = Modifier.weight(1f),
                    testTag = "btn_welcome_signup"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Caregiver Login
            AccessibleButton(
                text = StringsProvider.get("btn_caregiver_login", currentLanguage),
                onClick = onCaregiverLogin,
                icon = Icons.Default.SupervisorAccount,
                isPrimary = false,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                testTag = "btn_welcome_caregiver_login"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Medical Disclaimer
            DisclaimerBanner(
                currentLanguage = currentLanguage,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
        }
    }
}
