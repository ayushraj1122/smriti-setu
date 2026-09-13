package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FontSizeScale
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibilityHeader
import com.example.ui.components.AccessibleButton
import com.example.ui.components.DisclaimerBanner

@Composable
fun CaregiverAuthScreen(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    onCycleFontSize: () -> Unit,
    onSpeakContext: () -> Unit,
    onLogin: (email: String, pass: String, onError: (String) -> Unit) -> Unit,
    onQuickDemo: () -> Unit,
    onNavigateBack: () -> Unit,
    onSwitchToPatient: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    val bg = if (highContrast) Color(0xFF0F172A) else Color(0xFFF8FAFC)
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)

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
                title = "Caregiver Portal"
            )
        },
        containerColor = bg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75)
                )
                Text(
                    text = " Back to Welcome",
                    fontWeight = FontWeight.SemiBold,
                    color = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75),
                    fontSize = (15f * fontSizeScale.scale).sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (highContrast) Color(0xFF1E293B) else Color(0xFFEFF6FF),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (highContrast) Color(0xFF38BDF8) else Color(0xFFBFDBFE)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = if (highContrast) Color(0xFF38BDF8) else Color(0xFF1D4ED8),
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Column {
                        Text(
                            text = "Caregiver Privacy Shield",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (highContrast) Color.White else Color(0xFF1E3A8A)
                        )
                        Text(
                            text = "Caregivers monitor linked patient progress and manage daily routines without exposing patient passwords.",
                            fontSize = 12.sp,
                            color = if (highContrast) Color(0xFFCBD5E1) else Color(0xFF3B82F6),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Caregiver Sign In",
                fontSize = (22f * fontSizeScale.scale).sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Text(
                text = "Monitor performance trends, activity sessions, and schedule medication & hydration reminders.",
                fontSize = (13f * fontSizeScale.scale).sp,
                color = textPrimary.copy(alpha = 0.7f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (!errorMessage.isNullOrBlank()) {
                Surface(
                    color = if (highContrast) Color(0xFF7F1D1D) else Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = if (highContrast) Color(0xFFFECACA) else Color(0xFFB91C1C),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Caregiver Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().testTag("input_caregiver_email"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Caregiver Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().testTag("input_caregiver_pass"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            AccessibleButton(
                text = "Login as Caregiver",
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please enter caregiver email and password."
                    } else {
                        onLogin(email, password) { err -> errorMessage = err }
                    }
                },
                isPrimary = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                testTag = "btn_caregiver_submit_login"
            )

            Spacer(modifier = Modifier.height(12.dp))

            AccessibleButton(
                text = "Quick Demo: Caregiver (Priya Sharma)",
                onClick = onQuickDemo,
                isOutlined = true,
                emoji = "🩺",
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                testTag = "btn_caregiver_quick_demo"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { onSwitchToPatient() }) {
                    Text(
                        text = "Are you a Patient? Go to Patient Login",
                        fontWeight = FontWeight.SemiBold,
                        color = if (highContrast) Color(0xFF38BDF8) else Color(0xFF0D5C75)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            DisclaimerBanner(
                currentLanguage = currentLanguage,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
        }
    }
}
