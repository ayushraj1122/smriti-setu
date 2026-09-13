package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.FontSizeScale
import com.example.data.model.GameAttemptEntity
import com.example.data.model.PerformanceTrend
import com.example.data.model.ReminderEntity
import com.example.data.model.RoutineItemEntity
import com.example.data.model.UserEntity
import com.example.engine.AdaptiveEngine
import com.example.i18n.StringsProvider
import com.example.ui.components.AccessibilityHeader
import com.example.ui.components.AccessibleButton
import com.example.ui.components.AccuracyTrendChart
import com.example.ui.components.CategoryBreakdownChart
import com.example.ui.components.DisclaimerBanner
import com.example.ui.components.ResponseTimeChart
import com.example.ui.components.StatCard
import com.example.ui.navigation.CaregiverTab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CaregiverMainScreen(
    currentTab: CaregiverTab,
    onTabSelected: (CaregiverTab) -> Unit,
    caregiverUser: UserEntity?,
    linkedPatient: UserEntity?,
    allPatients: List<UserEntity>,
    onSelectPatient: (UserEntity) -> Unit,
    attempts: List<GameAttemptEntity>,
    reminders: List<ReminderEntity>,
    onToggleReminder: (Long, Boolean) -> Unit,
    onAddReminder: (title: String, desc: String, category: String, time: String) -> Unit,
    onDeleteReminder: (Long) -> Unit,
    routineItems: List<RoutineItemEntity>,
    onToggleRoutine: (Long, Boolean) -> Unit,
    onAddRoutine: (period: String, title: String, desc: String, time: String) -> Unit,
    onDeleteRoutine: (Long) -> Unit,
    onLinkPatientByCode: (code: String, onResult: (Boolean, String) -> Unit) -> Unit,
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    onCycleFontSize: () -> Unit,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    onSpeak: (String) -> Unit,
    onLogout: () -> Unit
) {
    val bg = if (highContrast) Color(0xFF0F172A) else com.example.ui.theme.VibrantBg
    val navBg = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantNavBg
    val navSelectedColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantNavSelected
    val navUnselectedColor = if (highContrast) Color(0xFF94A3B8) else com.example.ui.theme.VibrantNavUnselected
    val navIndicatorColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantNavIndicator

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
                title = StringsProvider.get("caregiver_dashboard_title", currentLanguage)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = navBg,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == CaregiverTab.DASHBOARD,
                    onClick = { onTabSelected(CaregiverTab.DASHBOARD) },
                    icon = { Icon(Icons.Default.ShowChart, contentDescription = "Dashboard") },
                    label = { Text("Overview", fontSize = 11.sp, fontWeight = if (currentTab == CaregiverTab.DASHBOARD) FontWeight.Bold else FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantNavSelected,
                        selectedTextColor = navSelectedColor,
                        indicatorColor = navIndicatorColor,
                        unselectedIconColor = navUnselectedColor,
                        unselectedTextColor = navUnselectedColor
                    ),
                    modifier = Modifier.testTag("tab_cg_dashboard")
                )
                NavigationBarItem(
                    selected = currentTab == CaregiverTab.PROGRESS,
                    onClick = { onTabSelected(CaregiverTab.PROGRESS) },
                    icon = { Icon(Icons.Default.List, contentDescription = "History") },
                    label = { Text("Reports", fontSize = 11.sp, fontWeight = if (currentTab == CaregiverTab.PROGRESS) FontWeight.Bold else FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantNavSelected,
                        selectedTextColor = navSelectedColor,
                        indicatorColor = navIndicatorColor,
                        unselectedIconColor = navUnselectedColor,
                        unselectedTextColor = navUnselectedColor
                    ),
                    modifier = Modifier.testTag("tab_cg_reports")
                )
                NavigationBarItem(
                    selected = currentTab == CaregiverTab.REMINDERS,
                    onClick = { onTabSelected(CaregiverTab.REMINDERS) },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Reminders") },
                    label = { Text("Reminders", fontSize = 11.sp, fontWeight = if (currentTab == CaregiverTab.REMINDERS) FontWeight.Bold else FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantNavSelected,
                        selectedTextColor = navSelectedColor,
                        indicatorColor = navIndicatorColor,
                        unselectedIconColor = navUnselectedColor,
                        unselectedTextColor = navUnselectedColor
                    ),
                    modifier = Modifier.testTag("tab_cg_reminders")
                )
                NavigationBarItem(
                    selected = currentTab == CaregiverTab.ROUTINE,
                    onClick = { onTabSelected(CaregiverTab.ROUTINE) },
                    icon = { Icon(Icons.Default.Schedule, contentDescription = "Routine") },
                    label = { Text("Routine", fontSize = 11.sp, fontWeight = if (currentTab == CaregiverTab.ROUTINE) FontWeight.Bold else FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantNavSelected,
                        selectedTextColor = navSelectedColor,
                        indicatorColor = navIndicatorColor,
                        unselectedIconColor = navUnselectedColor,
                        unselectedTextColor = navUnselectedColor
                    ),
                    modifier = Modifier.testTag("tab_cg_routine")
                )
                NavigationBarItem(
                    selected = currentTab == CaregiverTab.SETTINGS,
                    onClick = { onTabSelected(CaregiverTab.SETTINGS) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings", fontSize = 11.sp, fontWeight = if (currentTab == CaregiverTab.SETTINGS) FontWeight.Bold else FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = if (highContrast) Color.Black else com.example.ui.theme.VibrantNavSelected,
                        selectedTextColor = navSelectedColor,
                        indicatorColor = navIndicatorColor,
                        unselectedIconColor = navUnselectedColor,
                        unselectedTextColor = navUnselectedColor
                    ),
                    modifier = Modifier.testTag("tab_cg_settings")
                )
            }
        },
        containerColor = bg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                CaregiverTab.DASHBOARD -> CaregiverDashboardContent(
                    linkedPatient = linkedPatient,
                    allPatients = allPatients,
                    onSelectPatient = onSelectPatient,
                    attempts = attempts,
                    reminders = reminders,
                    routineItems = routineItems,
                    currentLanguage = currentLanguage,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    onSwitchToReminders = { onTabSelected(CaregiverTab.REMINDERS) },
                    onSwitchToRoutine = { onTabSelected(CaregiverTab.ROUTINE) }
                )
                CaregiverTab.PROGRESS -> CaregiverReportsContent(
                    linkedPatient = linkedPatient,
                    attempts = attempts,
                    currentLanguage = currentLanguage,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale
                )
                CaregiverTab.REMINDERS -> CaregiverRemindersContent(
                    linkedPatient = linkedPatient,
                    reminders = reminders,
                    onToggleReminder = onToggleReminder,
                    onAddReminder = onAddReminder,
                    onDeleteReminder = onDeleteReminder,
                    currentLanguage = currentLanguage,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale
                )
                CaregiverTab.ROUTINE -> CaregiverRoutineContent(
                    linkedPatient = linkedPatient,
                    routineItems = routineItems,
                    onToggleRoutine = onToggleRoutine,
                    onAddRoutine = onAddRoutine,
                    onDeleteRoutine = onDeleteRoutine,
                    currentLanguage = currentLanguage,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale
                )
                CaregiverTab.SETTINGS -> CaregiverSettingsContent(
                    caregiverUser = caregiverUser,
                    linkedPatient = linkedPatient,
                    onLinkPatientByCode = onLinkPatientByCode,
                    currentLanguage = currentLanguage,
                    highContrast = highContrast,
                    onToggleHighContrast = onToggleHighContrast,
                    fontSizeScale = fontSizeScale,
                    voiceEnabled = voiceEnabled,
                    onToggleVoice = onToggleVoice,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
private fun CaregiverDashboardContent(
    linkedPatient: UserEntity?,
    allPatients: List<UserEntity>,
    onSelectPatient: (UserEntity) -> Unit,
    attempts: List<GameAttemptEntity>,
    reminders: List<ReminderEntity>,
    routineItems: List<RoutineItemEntity>,
    currentLanguage: String,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale,
    onSwitchToReminders: () -> Unit,
    onSwitchToRoutine: () -> Unit
) {
    val scrollState = rememberScrollState()
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White

    val trendSummary = remember(attempts) {
        AdaptiveEngine.evaluatePerformanceTrend(attempts)
    }

    val patientName = linkedPatient?.fullName ?: "Arun Sharma"
    val patientCode = linkedPatient?.patientCode ?: "NER-6842"
    val patientState = linkedPatient?.locationState ?: "Assam"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Patient Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = if (highContrast) BorderStroke(2.dp, Color.White) else BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Monitoring Patient",
                            fontSize = (12f * fontSizeScale.scale).sp,
                            color = if (highContrast) Color(0xFF38BDF8) else Color(0xFF0369A1),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "$patientName (${linkedPatient?.age ?: 68}y)",
                            fontSize = (20f * fontSizeScale.scale).sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "$patientState • Code: $patientCode",
                            fontSize = (13f * fontSizeScale.scale).sp,
                            color = textPrimary.copy(alpha = 0.7f)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = if (highContrast) Color(0xFF0F172A) else Color(0xFFE0F2FE),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "👤", fontSize = 24.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Performance Trend / Variation Indicator Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(
                1.5.dp,
                when (trendSummary.trend) {
                    PerformanceTrend.IMPROVING -> if (highContrast) Color(0xFF4ADE80) else Color(0xFF22C55E)
                    PerformanceTrend.NEEDS_ATTENTION -> if (highContrast) Color(0xFFFACC15) else Color(0xFFF59E0B)
                    PerformanceTrend.STABLE -> if (highContrast) Color(0xFF38BDF8) else Color(0xFF0284C7)
                }
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (trendSummary.trend) {
                                PerformanceTrend.IMPROVING -> Icons.Default.TrendingUp
                                PerformanceTrend.NEEDS_ATTENTION -> Icons.Default.TrendingDown
                                PerformanceTrend.STABLE -> Icons.Default.TrendingFlat
                            },
                            contentDescription = null,
                            tint = when (trendSummary.trend) {
                                PerformanceTrend.IMPROVING -> if (highContrast) Color(0xFF4ADE80) else Color(0xFF16A34A)
                                PerformanceTrend.NEEDS_ATTENTION -> if (highContrast) Color(0xFFFACC15) else Color(0xFFD97706)
                                PerformanceTrend.STABLE -> if (highContrast) Color(0xFF38BDF8) else Color(0xFF0284C7)
                            },
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = trendSummary.trendTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = (16f * fontSizeScale.scale).sp,
                            color = textPrimary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (trendSummary.trend) {
                            PerformanceTrend.IMPROVING -> if (highContrast) Color(0xFF14532D) else Color(0xFFDCFCE7)
                            PerformanceTrend.NEEDS_ATTENTION -> if (highContrast) Color(0xFF713F12) else Color(0xFFFEF3C7)
                            PerformanceTrend.STABLE -> if (highContrast) Color(0xFF0C4A6E) else Color(0xFFE0F2FE)
                        }
                    ) {
                        Text(
                            text = trendSummary.trend.name.replace("_", " "),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (trendSummary.trend) {
                                PerformanceTrend.IMPROVING -> if (highContrast) Color(0xFF86EFAC) else Color(0xFF15803D)
                                PerformanceTrend.NEEDS_ATTENTION -> if (highContrast) Color(0xFFFDE047) else Color(0xFFB45309)
                                PerformanceTrend.STABLE -> if (highContrast) Color(0xFF7DD3FC) else Color(0xFF0369A1)
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = trendSummary.trendDescription,
                    fontSize = (13f * fontSizeScale.scale).sp,
                    color = textPrimary.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (highContrast) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Earlier Baseline", fontSize = 11.sp, color = textPrimary.copy(alpha = 0.6f))
                            Text("${trendSummary.baselineAccuracy}% accuracy", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = textPrimary)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (highContrast) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Recent Sessions", fontSize = 11.sp, color = textPrimary.copy(alpha = 0.6f))
                            Text("${trendSummary.recentAccuracy}% accuracy", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = textPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "* Non-clinical comparison against individual baseline. Do not interpret as diagnostic stage.",
                    fontSize = 11.sp,
                    color = textPrimary.copy(alpha = 0.5f),
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Stats
        val totalSessions = attempts.size
        val avgAccuracy = if (attempts.isNotEmpty()) attempts.map { it.accuracyPercent }.average().toInt() else 0

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Total Sessions",
                value = "$totalSessions",
                modifier = Modifier.weight(1f),
                emoji = "📝",
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
            StatCard(
                title = "Average Accuracy",
                value = "$avgAccuracy%",
                modifier = Modifier.weight(1f),
                emoji = "🎯",
                highContrast = highContrast,
                fontSizeScale = fontSizeScale
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Accuracy Trend Mini Chart
        AccuracyTrendChart(
            attempts = attempts,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Shortcuts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AccessibleButton(
                text = "Manage Reminders",
                onClick = onSwitchToReminders,
                icon = Icons.Default.Notifications,
                isOutlined = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                modifier = Modifier.weight(1f)
            )
            AccessibleButton(
                text = "Daily Routine",
                onClick = onSwitchToRoutine,
                icon = Icons.Default.Schedule,
                isOutlined = true,
                highContrast = highContrast,
                fontSizeScale = fontSizeScale,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        DisclaimerBanner(
            currentLanguage = currentLanguage,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )
    }
}

@Composable
private fun CaregiverReportsContent(
    linkedPatient: UserEntity?,
    attempts: List<GameAttemptEntity>,
    currentLanguage: String,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale
) {
    val scrollState = rememberScrollState()
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Cognitive Activity Reports",
            fontSize = (22f * fontSizeScale.scale).sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )
        Text(
            text = "Longitudinal session logs and category trends for ${linkedPatient?.fullName ?: "Patient"}.",
            fontSize = (13f * fontSizeScale.scale).sp,
            color = textPrimary.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(14.dp))

        AccuracyTrendChart(
            attempts = attempts,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )

        Spacer(modifier = Modifier.height(14.dp))

        ResponseTimeChart(
            attempts = attempts,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )

        Spacer(modifier = Modifier.height(14.dp))

        CategoryBreakdownChart(
            attempts = attempts,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Attempt History List
        Text(
            text = "Session History Log",
            fontSize = (18f * fontSizeScale.scale).sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (attempts.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = cardBg,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text(
                    text = "No recorded sessions yet.",
                    color = textPrimary.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                attempts.forEach { attempt ->
                    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH).format(Date(attempt.timestamp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = if (highContrast) BorderStroke(1.dp, Color(0xFF475569)) else BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${attempt.gameId.replace("_", " ").replaceFirstChar { it.uppercase() }} (L${attempt.level})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = textPrimary
                                )
                                Text(
                                    text = "${attempt.accuracyPercent}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (attempt.accuracyPercent >= 80) Color(0xFF16A34A) else Color(0xFF0284C7)
                                )
                            }
                            Text(
                                text = "$dateStr • Avg Time: ${(attempt.avgResponseTimeMs / 1000f)}s • Score: ${attempt.totalScore}/100",
                                fontSize = 11.sp,
                                color = textPrimary.copy(alpha = 0.65f)
                            )
                            if (attempt.recommendationMessage.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Adaptive Note: ${attempt.recommendationMessage}",
                                    fontSize = 11.sp,
                                    color = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DisclaimerBanner(
            currentLanguage = currentLanguage,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )
    }
}

@Composable
private fun CaregiverRemindersContent(
    linkedPatient: UserEntity?,
    reminders: List<ReminderEntity>,
    onToggleReminder: (Long, Boolean) -> Unit,
    onAddReminder: (title: String, desc: String, category: String, time: String) -> Unit,
    onDeleteReminder: (Long) -> Unit,
    currentLanguage: String,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("MEDICATION") }
    var timeStr by remember { mutableStateOf("09:00 AM") }

    val scrollState = rememberScrollState()
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Patient Reminders",
                    fontSize = (22f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = "Scheduled reminders for ${linkedPatient?.fullName ?: "Patient"}",
                    fontSize = (13f * fontSizeScale.scale).sp,
                    color = textPrimary.copy(alpha = 0.7f)
                )
            }
            IconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75))
                    .testTag("btn_add_reminder_dialog")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Reminder",
                    tint = if (highContrast) Color.Black else Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (reminders.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = cardBg,
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No reminders set yet.", color = textPrimary.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { showAddDialog = true }) {
                        Text("+ Add First Reminder")
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                reminders.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = if (highContrast) BorderStroke(1.dp, Color(0xFF475569)) else BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isCompleted,
                                onCheckedChange = { onToggleReminder(item.id, it) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75),
                                    checkmarkColor = if (highContrast) Color.Black else Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.title,
                                        fontSize = (15f * fontSizeScale.scale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.isCompleted) textPrimary.copy(alpha = 0.5f) else textPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (highContrast) Color(0xFF334155) else Color(0xFFE2E8F0)
                                    ) {
                                        Text(
                                            text = item.category,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${item.timeStr} • ${item.description}",
                                    fontSize = 12.sp,
                                    color = textPrimary.copy(alpha = 0.65f)
                                )
                            }
                            IconButton(onClick = { onDeleteReminder(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Reminder",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DisclaimerBanner(
            currentLanguage = currentLanguage,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )
    }

    // Add Reminder Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Patient Reminder") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title (e.g. Afternoon Medication)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = timeStr,
                        onValueChange = { timeStr = it },
                        label = { Text("Time (e.g. 02:00 PM)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                AccessibleButton(
                    text = "Save",
                    onClick = {
                        if (title.isNotBlank()) {
                            onAddReminder(title, desc, category, timeStr)
                            showAddDialog = false
                            title = ""
                            desc = ""
                        }
                    },
                    modifier = Modifier.width(100.dp),
                    isPrimary = true
                )
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CaregiverRoutineContent(
    linkedPatient: UserEntity?,
    routineItems: List<RoutineItemEntity>,
    onToggleRoutine: (Long, Boolean) -> Unit,
    onAddRoutine: (period: String, title: String, desc: String, time: String) -> Unit,
    onDeleteRoutine: (Long) -> Unit,
    currentLanguage: String,
    highContrast: Boolean,
    fontSizeScale: FontSizeScale
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var period by remember { mutableStateOf("Morning") }
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var timeStr by remember { mutableStateOf("07:30 AM") }

    val scrollState = rememberScrollState()
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Daily Routine Schedule",
                    fontSize = (22f * fontSizeScale.scale).sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = "Structured calming routines promote reassurance and independence.",
                    fontSize = (13f * fontSizeScale.scale).sp,
                    color = textPrimary.copy(alpha = 0.7f)
                )
            }
            IconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Routine Item",
                    tint = if (highContrast) Color.Black else Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (routineItems.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = cardBg,
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No daily routine items found.", color = textPrimary.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { showAddDialog = true }) {
                        Text("+ Add First Routine Item")
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                routineItems.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = if (highContrast) BorderStroke(1.dp, Color(0xFF475569)) else BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isCompleted,
                                onCheckedChange = { onToggleRoutine(item.id, it) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = if (highContrast) Color(0xFFFACC15) else Color(0xFF0D5C75),
                                    checkmarkColor = if (highContrast) Color.Black else Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.title,
                                        fontSize = (15f * fontSizeScale.scale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.isCompleted) textPrimary.copy(alpha = 0.5f) else textPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (highContrast) Color(0xFF334155) else Color(0xFFE2E8F0)
                                    ) {
                                        Text(
                                            text = item.period,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${item.timeStr} • ${item.description}",
                                    fontSize = 12.sp,
                                    color = textPrimary.copy(alpha = 0.65f)
                                )
                            }
                            IconButton(onClick = { onDeleteRoutine(item.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Routine Item",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DisclaimerBanner(
            currentLanguage = currentLanguage,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Routine Activity") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Activity Title (e.g. Morning Tea & Stroll)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = timeStr,
                        onValueChange = { timeStr = it },
                        label = { Text("Time (e.g. 07:30 AM)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                AccessibleButton(
                    text = "Save",
                    onClick = {
                        if (title.isNotBlank()) {
                            onAddRoutine(period, title, desc, timeStr)
                            showAddDialog = false
                            title = ""
                            desc = ""
                        }
                    },
                    modifier = Modifier.width(100.dp),
                    isPrimary = true
                )
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun CaregiverSettingsContent(
    caregiverUser: UserEntity?,
    linkedPatient: UserEntity?,
    onLinkPatientByCode: (code: String, onResult: (Boolean, String) -> Unit) -> Unit,
    currentLanguage: String,
    highContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    fontSizeScale: FontSizeScale,
    voiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    onLogout: () -> Unit
) {
    var patientCodeInput by remember { mutableStateOf("") }
    var linkResultMsg by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val textPrimary = if (highContrast) Color.White else Color(0xFF0F172A)
    val cardBg = if (highContrast) Color(0xFF1E293B) else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Caregiver Settings",
            fontSize = (22f * fontSizeScale.scale).sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Link Patient Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = if (highContrast) BorderStroke(2.dp, Color.White) else BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Link or Switch Patient",
                    fontWeight = FontWeight.Bold,
                    fontSize = (16f * fontSizeScale.scale).sp,
                    color = textPrimary
                )
                Text(
                    text = "Currently linked: ${linkedPatient?.fullName ?: "None"} (${linkedPatient?.patientCode ?: "None"})",
                    fontSize = 13.sp,
                    color = textPrimary.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = patientCodeInput,
                    onValueChange = { patientCodeInput = it },
                    label = { Text("Enter Patient Code (e.g. NER-6842)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_link_patient_code"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                AccessibleButton(
                    text = "Connect to Patient",
                    onClick = {
                        if (patientCodeInput.isNotBlank()) {
                            onLinkPatientByCode(patientCodeInput) { success, msg ->
                                isError = !success
                                linkResultMsg = msg
                                if (success) patientCodeInput = ""
                            }
                        }
                    },
                    icon = Icons.Default.Link,
                    isPrimary = true,
                    highContrast = highContrast,
                    fontSizeScale = fontSizeScale,
                    testTag = "btn_link_patient_submit"
                )

                if (!linkResultMsg.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = linkResultMsg ?: "",
                        color = if (isError) Color.Red else Color(0xFF16A34A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Caregiver Preferences
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = if (highContrast) BorderStroke(2.dp, Color.White) else BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Accessibility & Voice",
                    fontWeight = FontWeight.Bold,
                    fontSize = (16f * fontSizeScale.scale).sp,
                    color = textPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("High Contrast Display", fontSize = 14.sp, color = textPrimary)
                    Switch(
                        checked = highContrast,
                        onCheckedChange = { onToggleHighContrast() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFACC15))
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Voice Read-Aloud", fontSize = 14.sp, color = textPrimary)
                    Switch(
                        checked = voiceEnabled,
                        onCheckedChange = { onToggleVoice() },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFACC15))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        AccessibleButton(
            text = "Caregiver Sign Out",
            onClick = onLogout,
            icon = Icons.Default.ExitToApp,
            isOutlined = true,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale,
            testTag = "btn_caregiver_logout"
        )

        Spacer(modifier = Modifier.height(16.dp))

        DisclaimerBanner(
            currentLanguage = currentLanguage,
            highContrast = highContrast,
            fontSizeScale = fontSizeScale
        )
    }
}
