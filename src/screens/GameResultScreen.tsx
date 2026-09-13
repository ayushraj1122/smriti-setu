import React, { useEffect } from 'react';
import {
  Award,
  TrendingUp,
  Clock,
  RotateCcw,
  Home,
  Volume2,
  Sparkles,
  CheckCircle,
  HelpCircle
} from 'lucide-react';
import { AppLanguageCode, FontSizeScale } from '../types';
import { getGameById } from '../data/gameCatalog';
import { TextToSpeechService } from '../engine/tts';
import { t } from '../i18n/translations';
import { StatCard } from '../components/StatCard';
import { DisclaimerBanner } from '../components/DisclaimerBanner';

interface GameResultScreenProps {
  gameId: string;
  level: number;
  score: number;
  accuracy: number;
  avgTimeMs: number;
  correct: number;
  total: number;
  recommendation: string;
  currentLanguage: AppLanguageCode;
  highContrast: boolean;
  fontSizeScale: FontSizeScale;
  voiceEnabled: boolean;
  onReplay: () => void;
  onBackToGames: () => void;
  onGoHome: () => void;
  onSpeak: (text: string) => void;
}

export const GameResultScreen: React.FC<GameResultScreenProps> = ({
  gameId,
  level,
  score,
  accuracy,
  avgTimeMs,
  correct,
  total,
  recommendation,
  currentLanguage,
  highContrast,
  fontSizeScale,
  voiceEnabled,
  onReplay,
  onBackToGames,
  onGoHome,
  onSpeak
}) => {
  const gameDef = getGameById(gameId);
  const gameTitle = gameDef ? t(gameDef.titleKey, currentLanguage) : 'Cognitive Game';

  const isHighAccuracy = accuracy >= 80;
  const greeting = isHighAccuracy
    ? t('msg_good_job', currentLanguage)
    : t('msg_improving', currentLanguage);

  useEffect(() => {
    if (voiceEnabled) {
      const summaryText = `${greeting}. You scored ${score} points with ${accuracy} percent accuracy. ${recommendation}`;
      TextToSpeechService.speak(summaryText, currentLanguage);
    }
  }, [accuracy, score, recommendation, greeting, voiceEnabled, currentLanguage]);

  const cardStyle = highContrast
    ? 'bg-slate-900 border-2 border-yellow-400 text-white'
    : 'bg-white border border-slate-200 text-slate-900 shadow-md';

  return (
    <div className={`min-h-[calc(100vh-60px)] px-4 py-8 flex flex-col items-center justify-center ${
      highContrast ? 'bg-slate-950 text-white' : 'bg-slate-50 text-slate-900'
    }`}>
      <div className={`w-full max-w-xl p-6 sm:p-8 rounded-3xl ${cardStyle} space-y-6 text-center`}>
        {/* Celebration Badge */}
        <div className="space-y-3">
          <div className={`w-20 h-20 mx-auto rounded-3xl flex items-center justify-center text-4xl shadow-md ${
            highContrast
              ? 'bg-yellow-400 text-slate-950'
              : 'bg-teal-700 text-white shadow-teal-200'
          }`}>
            <span>{isHighAccuracy ? '🎉' : '🌟'}</span>
          </div>

          <h2 className="text-2xl sm:text-3xl font-extrabold tracking-tight">
            {greeting}
          </h2>

          <p className="text-xs sm:text-sm text-slate-500">
            Completed <strong>{gameTitle}</strong> (Level {level})
          </p>
        </div>

        {/* Adaptive Recommendation Card */}
        <div className={`p-4 rounded-2xl text-left flex items-start gap-3 border ${
          highContrast ? 'bg-slate-800 border-yellow-400' : 'bg-teal-50 border-teal-200'
        }`}>
          <Sparkles className={`w-5 h-5 flex-shrink-0 mt-0.5 ${
            highContrast ? 'text-yellow-400' : 'text-teal-700'
          }`} />
          <div>
            <span className="font-extrabold text-xs uppercase tracking-wider block mb-0.5 text-teal-800 dark:text-yellow-400">
              Personalized Guidance:
            </span>
            <p className="text-xs sm:text-sm font-medium leading-relaxed">
              {recommendation}
            </p>
          </div>
        </div>

        {/* Results Metrics Grid */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
          <StatCard
            id="result-stat-score"
            label={t('label_score', currentLanguage)}
            value={score}
            subtitle="Points"
            icon={<Award className="w-5 h-5" />}
            highContrast={highContrast}
          />
          <StatCard
            id="result-stat-accuracy"
            label={t('label_accuracy', currentLanguage)}
            value={`${accuracy}%`}
            subtitle={`${correct}/${total} Qs`}
            icon={<CheckCircle className="w-5 h-5" />}
            highContrast={highContrast}
          />
          <StatCard
            id="result-stat-time"
            label={t('label_response_time', currentLanguage)}
            value={`${(avgTimeMs / 1000).toFixed(1)}s`}
            subtitle="Avg Response"
            icon={<Clock className="w-5 h-5" />}
            highContrast={highContrast}
          />
          <StatCard
            id="result-stat-level"
            label="Level"
            value={level}
            subtitle={level === 1 ? 'Gentle' : level === 2 ? 'Moderate' : 'Engaging'}
            icon={<TrendingUp className="w-5 h-5" />}
            highContrast={highContrast}
          />
        </div>

        {/* Actions Buttons */}
        <div className="space-y-3 pt-2">
          <button
            id="btn-result-replay"
            type="button"
            onClick={onReplay}
            className={`w-full py-3.5 rounded-xl font-extrabold text-sm sm:text-base flex items-center justify-center gap-2 shadow transition-all ${
              highContrast
                ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                : 'bg-teal-700 hover:bg-teal-800 text-white'
            }`}
          >
            <RotateCcw className="w-4 h-4" />
            <span>{t('btn_replay', currentLanguage)}</span>
          </button>

          <div className="grid grid-cols-2 gap-3">
            <button
              id="btn-result-games"
              type="button"
              onClick={onBackToGames}
              className={`py-3 rounded-xl font-bold text-xs sm:text-sm flex items-center justify-center gap-1.5 border ${
                highContrast
                  ? 'bg-slate-800 border-slate-700 text-white hover:bg-slate-750'
                  : 'bg-white border-slate-300 text-slate-700 hover:bg-slate-100'
              }`}
            >
              <span>Browse All Games</span>
            </button>

            <button
              id="btn-result-home"
              type="button"
              onClick={onGoHome}
              className={`py-3 rounded-xl font-bold text-xs sm:text-sm flex items-center justify-center gap-1.5 border ${
                highContrast
                  ? 'bg-slate-800 border-slate-700 text-white hover:bg-slate-750'
                  : 'bg-white border-slate-300 text-slate-700 hover:bg-slate-100'
              }`}
            >
              <Home className="w-4 h-4" />
              <span>{t('btn_home', currentLanguage)}</span>
            </button>
          </div>
        </div>

        {/* Medical disclaimer note */}
        <DisclaimerBanner currentLanguage={currentLanguage} highContrast={highContrast} />
      </div>
    </div>
  );
};
