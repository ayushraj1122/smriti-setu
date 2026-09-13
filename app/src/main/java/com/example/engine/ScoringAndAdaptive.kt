package com.example.engine

import com.example.data.model.GameAttemptEntity
import com.example.data.model.PerformanceTrend

object ScoringEngine {

    data class ScoreCalculationResult(
        val totalQuestions: Int,
        val correctAnswers: Int,
        val incorrectAnswers: Int,
        val accuracyPercent: Int,
        val avgResponseTimeMs: Long,
        val calculatedScore: Int
    )

    fun calculate(
        totalQuestions: Int,
        correctAnswers: Int,
        responseTimesMs: List<Long>
    ): ScoreCalculationResult {
        val safeTotal = if (totalQuestions <= 0) 1 else totalQuestions
        val correct = correctAnswers.coerceIn(0, safeTotal)
        val incorrect = safeTotal - correct
        val accuracyPercent = ((correct.toFloat() / safeTotal.toFloat()) * 100).toInt()

        val avgTime = if (responseTimesMs.isNotEmpty()) {
            responseTimesMs.average().toLong().coerceAtLeast(500L)
        } else {
            3000L
        }

        // Dementia-appropriate balanced scoring:
        // Heavily weights accuracy (80 points max)
        // Completion reward (15 points)
        // Gentle pacing bonus (up to 5 points without rushing the user)
        val accuracyPart = (accuracyPercent * 0.80f).toInt()
        val completionPart = 15
        val pacingBonus = if (avgTime in 1500..8000) 5 else 2
        val finalScore = (accuracyPart + completionPart + pacingBonus).coerceIn(10, 100)

        return ScoreCalculationResult(
            totalQuestions = safeTotal,
            correctAnswers = correct,
            incorrectAnswers = incorrect,
            accuracyPercent = accuracyPercent,
            avgResponseTimeMs = avgTime,
            calculatedScore = finalScore
        )
    }
}

object AdaptiveEngine {

    data class RecommendationResult(
        val recommendedLevel: Int,
        val recommendationText: String,
        val shouldAdvance: Boolean
    )

    fun getRecommendation(
        currentLevel: Int,
        accuracyPercent: Int,
        avgResponseTimeMs: Long
    ): RecommendationResult {
        return when {
            accuracyPercent >= 80 -> {
                if (currentLevel < 3) {
                    val nextLevel = currentLevel + 1
                    RecommendationResult(
                        recommendedLevel = nextLevel,
                        recommendationText = "You're doing very well! Ready for Level $nextLevel.",
                        shouldAdvance = true
                    )
                } else {
                    RecommendationResult(
                        recommendedLevel = 3,
                        recommendationText = "Wonderful mastery! You have completed all levels.",
                        shouldAdvance = false
                    )
                }
            }
            accuracyPercent in 50..79 -> {
                RecommendationResult(
                    recommendedLevel = currentLevel,
                    recommendationText = "Good progress! Let's practice Level $currentLevel again to build confidence.",
                    shouldAdvance = false
                )
            }
            else -> {
                val gentleLevel = (currentLevel - 1).coerceAtLeast(1)
                RecommendationResult(
                    recommendedLevel = gentleLevel,
                    recommendationText = "That's completely okay. Let's take our time and practice with simpler steps.",
                    shouldAdvance = false
                )
            }
        }
    }

    data class CaregiverTrendSummary(
        val trend: PerformanceTrend,
        val trendTitle: String,
        val trendDescription: String,
        val baselineAccuracy: Int,
        val recentAccuracy: Int,
        val baselineAvgTimeSec: Float,
        val recentAvgTimeSec: Float
    )

    fun evaluatePerformanceTrend(attempts: List<GameAttemptEntity>): CaregiverTrendSummary {
        if (attempts.size < 2) {
            return CaregiverTrendSummary(
                trend = PerformanceTrend.STABLE,
                trendTitle = "Establishing Baseline",
                trendDescription = "More game sessions will establish a clearer baseline trend.",
                baselineAccuracy = if (attempts.isNotEmpty()) attempts[0].accuracyPercent else 75,
                recentAccuracy = if (attempts.isNotEmpty()) attempts[0].accuracyPercent else 75,
                baselineAvgTimeSec = 4.0f,
                recentAvgTimeSec = 4.0f
            )
        }

        // Recent attempts (up to 3) vs Earlier attempts
        val recent = attempts.take(3)
        val baseline = attempts.drop(3).ifEmpty { attempts.takeLast(attempts.size / 2 + 1) }

        val recentAcc = recent.map { it.accuracyPercent }.average().toInt()
        val baselineAcc = baseline.map { it.accuracyPercent }.average().toInt()

        val recentTime = (recent.map { it.avgResponseTimeMs }.average() / 1000.0).toFloat()
        val baselineTime = (baseline.map { it.avgResponseTimeMs }.average() / 1000.0).toFloat()

        val diff = recentAcc - baselineAcc

        return when {
            diff >= 8 -> CaregiverTrendSummary(
                trend = PerformanceTrend.IMPROVING,
                trendTitle = "Improving Trend",
                trendDescription = "Recent game accuracy is $diff% higher than earlier baseline. Patient shows engaged focus.",
                baselineAccuracy = baselineAcc,
                recentAccuracy = recentAcc,
                baselineAvgTimeSec = baselineTime,
                recentAvgTimeSec = recentTime
            )
            diff <= -15 -> CaregiverTrendSummary(
                trend = PerformanceTrend.NEEDS_ATTENTION,
                trendTitle = "Noticeable Variation",
                trendDescription = "Recent accuracy is lower than earlier baseline. Patient may be fatigued, distracted, or experiencing temporary fluctuations.",
                baselineAccuracy = baselineAcc,
                recentAccuracy = recentAcc,
                baselineAvgTimeSec = baselineTime,
                recentAvgTimeSec = recentTime
            )
            else -> CaregiverTrendSummary(
                trend = PerformanceTrend.STABLE,
                trendTitle = "Stable Engagement",
                trendDescription = "Activity accuracy and timing are consistent with established baseline records.",
                baselineAccuracy = baselineAcc,
                recentAccuracy = recentAcc,
                baselineAvgTimeSec = baselineTime,
                recentAvgTimeSec = recentTime
            )
        }
    }
}
