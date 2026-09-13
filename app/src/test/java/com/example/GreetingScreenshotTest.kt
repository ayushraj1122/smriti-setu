package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.FontSizeScale
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [34])
class GreetingScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun welcome_screenshot() {
        composeTestRule.setContent {
            MyApplicationTheme {
                WelcomeScreen(
                    currentLanguage = "en",
                    onLanguageSelected = {},
                    voiceEnabled = true,
                    onToggleVoice = {},
                    highContrast = false,
                    onToggleHighContrast = {},
                    fontSizeScale = FontSizeScale.STANDARD,
                    onCycleFontSize = {},
                    onSpeakContext = {},
                    onPlayGames = {},
                    onPatientLogin = {},
                    onSignUp = {},
                    onCaregiverLogin = {},
                    onQuickDemoPatient = {},
                    onQuickDemoCaregiver = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/welcome.png")
    }
}

