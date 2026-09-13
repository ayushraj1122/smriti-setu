package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import com.example.data.model.AppLanguage
import com.example.data.model.FontSizeScale
import com.example.data.model.NERState
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibilityHeader
import com.example.ui.components.AccessibleButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientAuthScreen(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    onCycleFontSize: () -> Unit,
    onSpeakContext: () -> Unit,
    initialTabIsSignUp: Boolean = false,
    onLogin: (email: String, pass: String, onError: (String) -> Unit) -> Unit,
    onSignUp: (
        name: String,
        age: Int,
        gender: String,
        language: String,
        state: String,
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) -> Unit,
    onQuickDemo: () -> Unit,
    onNavigateBack: () -> Unit,
    onSwitchToCaregiver: () -> Unit
) {
    var isSignUp by remember { mutableStateOf(initialTabIsSignUp) }
    val scrollState = rememberScrollState()

    // Login state
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    // Signup state
    var name by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("65") }
    var gender by remember { mutableStateOf("Male") }
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }
    var selectedState by remember { mutableStateOf(NERState.ASSAM.displayName) }
    var signupEmail by remember { mutableStateOf("") }
    var signupPassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var stateMenuExpanded by remember { mutableStateOf(false) }
    var genderMenuExpanded by remember { mutableStateOf(false) }

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
                title = if (isSignUp) "Patient Sign Up" else "Patient Login"
            )
        },
        containerColor = bg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(18.dp)
        ) {
            // Back button
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

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Selector
            TabRow(
                selectedTabIndex = if (isSignUp) 1 else 0,
                containerColor = if (highContrast) Color(0xFF1E293B) else Color(0xFFE2E8F0),
                contentColor = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75)
            ) {
                Tab(
                    selected = !isSignUp,
                    onClick = { isSignUp = false; errorMessage = null },
                    text = {
                        Text(
                            text = StringsProvider.get("btn_patient_login", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = (15f * fontSizeScale.scale).sp
                        )
                    }
                )
                Tab(
                    selected = isSignUp,
                    onClick = { isSignUp = true; errorMessage = null },
                    text = {
                        Text(
                            text = StringsProvider.get("btn_sign_up", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = (15f * fontSizeScale.scale).sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error or Success Banner
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

            if (!successMessage.isNullOrBlank()) {
                Surface(
                    color = if (highContrast) Color(0xFF14532D) else Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (highContrast) Color(0xFF4ADE80) else Color(0xFF15803D)
                        )
                        Text(
                            text = " " + (successMessage ?: ""),
                            color = if (highContrast) Color(0xFFBBF7D0) else Color(0xFF166534),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (!isSignUp) {
                // LOGIN FORM
                Text(
                    text = "Welcome to your cognitive space",
                    fontSize = (20f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = "Sign in to continue your games and activities.",
                    fontSize = (13f * fontSizeScale.scale).sp,
                    color = textPrimary.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = loginEmail,
                    onValueChange = { loginEmail = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_patient_login_email"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = loginPassword,
                    onValueChange = { loginPassword = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().testTag("input_patient_login_pass"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                AccessibleButton(
                    text = "Sign In",
                    onClick = {
                        if (loginEmail.isBlank() || loginPassword.isBlank()) {
                            errorMessage = "Please enter both email and password."
                        } else {
                            onLogin(loginEmail, loginPassword) { err -> errorMessage = err }
                        }
                    },
                    isPrimary = true,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    testTag = "btn_patient_submit_login"
                )

                Spacer(modifier = Modifier.height(12.dp))

                AccessibleButton(
                    text = "Quick Demo Login (No typing needed)",
                    onClick = onQuickDemo,
                    isOutlined = true,
                    emoji = "⚡",
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    testTag = "btn_patient_quick_demo"
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = { onSwitchToCaregiver() }) {
                        Text(
                            text = "Are you a Caregiver? Login here",
                            fontWeight = FontWeight.SemiBold,
                            color = if (highContrast) Color(0xFF38BDF8) else Color(0xFF0D5C75)
                        )
                    }
                }
            } else {
                // SIGN UP FORM
                Text(
                    text = "Create Patient Profile",
                    fontSize = (20f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = "Specialized cognitive care with North East India language support.",
                    fontSize = (13f * fontSizeScale.scale).sp,
                    color = textPrimary.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_patient_signup_name"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = ageStr,
                        onValueChange = { ageStr = it.filter { ch -> ch.isDigit() }.take(3) },
                        label = { Text("Age *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("input_patient_signup_age"),
                        singleLine = true
                    )

                    // Gender dropdown
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Gender") },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { genderMenuExpanded = true }
                                )
                            },
                            modifier = Modifier.fillMaxWidth().clickable { genderMenuExpanded = true }
                        )
                        DropdownMenu(
                            expanded = genderMenuExpanded,
                            onDismissRequest = { genderMenuExpanded = false }
                        ) {
                            listOf("Male", "Female", "Other").forEach { g ->
                                DropdownMenuItem(
                                    text = { Text(g) },
                                    onClick = { gender = g; genderMenuExpanded = false }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // North Eastern Region state dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedState,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("North East State (Location) *") },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.clickable { stateMenuExpanded = true }
                            )
                        },
                        modifier = Modifier.fillMaxWidth().clickable { stateMenuExpanded = true }
                    )
                    DropdownMenu(
                        expanded = stateMenuExpanded,
                        onDismissRequest = { stateMenuExpanded = false }
                    ) {
                        NERState.values().forEach { stateItem ->
                            DropdownMenuItem(
                                text = { Text(stateItem.displayName) },
                                onClick = {
                                    selectedState = stateItem.displayName
                                    stateMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = signupEmail,
                    onValueChange = { signupEmail = it },
                    label = { Text("Email Address *") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().testTag("input_patient_signup_email"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = signupPassword,
                    onValueChange = { signupPassword = it },
                    label = { Text("Password * (At least 4 characters)") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().testTag("input_patient_signup_pass"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                AccessibleButton(
                    text = "Complete Sign Up",
                    onClick = {
                        val ageInt = ageStr.toIntOrNull()
                        when {
                            name.isBlank() -> errorMessage = "Please enter full name."
                            ageInt == null || ageInt < 10 || ageInt > 120 -> errorMessage = "Please enter a valid age."
                            signupEmail.isBlank() || !signupEmail.contains("@") -> errorMessage = "Please enter a valid email address."
                            signupPassword.length < 4 -> errorMessage = "Password must be at least 4 characters."
                            else -> {
                                errorMessage = null
                                onSignUp(
                                    name,
                                    ageInt,
                                    gender,
                                    selectedLanguage,
                                    selectedState,
                                    signupEmail,
                                    signupPassword,
                                    {
                                        successMessage = "Account created successfully! You are now logged in."
                                    },
                                    { err -> errorMessage = err }
                                )
                            }
                        }
                    },
                    isPrimary = true,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    testTag = "btn_patient_submit_signup"
                )
            }
        }
    }
}
