package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.GameCategory
import com.example.engine.AdaptiveEngine
import com.example.engine.ScoringEngine
import com.example.i18n.StringsProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Smriti NER", appName)
    }

    @Test
    fun `test scoring engine calculation`() {
        val result = ScoringEngine.calculate(
            totalQuestions = 4,
            correctAnswers = 4,
            responseTimesMs = listOf(2000L, 2500L, 3000L, 2200L)
        )
        assertEquals(100, result.accuracyPercent)
        assertTrue(result.calculatedScore >= 95)
    }

    @Test
    fun `test multilingual strings provider fallback`() {
        val assameseHome = StringsProvider.get("nav_home", "as")
        assertEquals("ঘৰ (Home)", assameseHome)
        val englishHome = StringsProvider.get("nav_home", "en")
        assertEquals("Home", englishHome)
    }

    @Test
    fun `test adaptive recommendation progression`() {
        val rec = AdaptiveEngine.getRecommendation(
            currentLevel = 1,
            accuracyPercent = 85,
            avgResponseTimeMs = 2500L
        )
        assertEquals(2, rec.recommendedLevel)
        assertTrue(rec.shouldAdvance)
    }
}

