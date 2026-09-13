import { PerformanceTrend, GameAttemptEntity } from '../types';

export class ScoringEngine {
  /**
   * Calculates a score calibrated for cognitive care.
   * Emphasizes accuracy and positive reinforcement, discouraging rapid rushing.
   */
  public static calculateScore(
    correctAnswers: number,
    totalQuestions: number,
    avgResponseTimeMs: number,
    level: number
  ): {
    score: number;
    accuracyPercent: number;
  } {
    if (totalQuestions <= 0) {
      return { score: 0, accuracyPercent: 0 };
    }

    const accuracyPercent = Math.round((correctAnswers / totalQuestions) * 100);
    const levelMultiplier = 1.0 + (level - 1) * 0.25;

    // Accuracy contributes 80 points maximum per question
    const accuracyPoints = correctAnswers * 100;

    // Pacing bonus: gentle pacing (between 2s and 12s) is rewarded
    let pacingBonus = 0;
    if (avgResponseTimeMs >= 1500 && avgResponseTimeMs <= 15000) {
      pacingBonus = Math.min(20 * correctAnswers, 100);
    }

    // Completion bonus (encouragement for finishing the session)
    const completionBonus = 50;

    const totalRaw = (accuracyPoints + pacingBonus + completionBonus) * levelMultiplier;
    const finalScore = Math.max(10, Math.round(totalRaw));

    return {
      score: finalScore,
      accuracyPercent
    };
  }
}

export class AdaptiveEngine {
  /**
   * Provides supportive recommendations based on session accuracy and current level.
   */
  public static getRecommendation(accuracyPercent: number, currentLevel: number): string {
    if (accuracyPercent >= 80) {
      if (currentLevel < 3) {
        return "Excellent accuracy! You are ready to try the next level when you feel comfortable.";
      }
      return "Fantastic performance! Keep practicing to maintain this sharp engagement.";
    } else if (accuracyPercent >= 50) {
      return "Good effort! Continuing at this level will help build comfort and confidence.";
    } else {
      if (currentLevel > 1) {
        return "Gentle reminder: Taking a turn at the previous level may feel more relaxing and enjoyable.";
      }
      return "Take your time with each question. Remember there is no rush at all.";
    }
  }

  /**
   * Evaluates overall performance trend by comparing recent attempts against baseline.
   * Useful for caregivers to monitor changes in cognitive engagement.
   */
  public static evaluatePerformanceTrend(attempts: GameAttemptEntity[]): {
    trend: PerformanceTrend;
    baselineAccuracy: number;
    recentAccuracy: number;
    baselineTimeMs: number;
    recentTimeMs: number;
    sampleSize: number;
  } {
    if (attempts.length < 3) {
      return {
        trend: 'STABLE',
        baselineAccuracy: attempts.length > 0 ? attempts[0].accuracyPercent : 75,
        recentAccuracy: attempts.length > 0 ? attempts[attempts.length - 1].accuracyPercent : 75,
        baselineTimeMs: 4500,
        recentTimeMs: 4200,
        sampleSize: attempts.length
      };
    }

    // Sort chronologically ascending
    const sorted = [...attempts].sort((a, b) => a.timestamp - b.timestamp);
    const splitIndex = Math.floor(sorted.length / 2);

    const baselineSet = sorted.slice(0, splitIndex);
    const recentSet = sorted.slice(splitIndex);

    const baselineAccuracy = Math.round(
      baselineSet.reduce((acc, curr) => acc + curr.accuracyPercent, 0) / baselineSet.length
    );
    const recentAccuracy = Math.round(
      recentSet.reduce((acc, curr) => acc + curr.accuracyPercent, 0) / recentSet.length
    );

    const baselineTimeMs = Math.round(
      baselineSet.reduce((acc, curr) => acc + curr.avgResponseTimeMs, 0) / baselineSet.length
    );
    const recentTimeMs = Math.round(
      recentSet.reduce((acc, curr) => acc + curr.avgResponseTimeMs, 0) / recentSet.length
    );

    const accuracyDelta = recentAccuracy - baselineAccuracy;

    let trend: PerformanceTrend = 'STABLE';
    if (accuracyDelta >= 8) {
      trend = 'IMPROVING';
    } else if (accuracyDelta <= -15) {
      trend = 'NEEDS_ATTENTION';
    } else {
      trend = 'STABLE';
    }

    return {
      trend,
      baselineAccuracy,
      recentAccuracy,
      baselineTimeMs,
      recentTimeMs,
      sampleSize: sorted.length
    };
  }
}
