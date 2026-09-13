import React, { useState } from 'react';
import {
  Home,
  Gamepad2,
  TrendingUp,
  User,
  Clock,
  CheckCircle2,
  Circle,
  Play,
  Volume2,
  Award,
  Sparkles,
  Calendar,
  LogOut,
  MapPin,
  ChevronRight,
  Heart,
  Sliders
} from 'lucide-react';
import {
  UserEntity,
  GameAttemptEntity,
  RoutineItemEntity,
  PatientTab,
  GameCategory,
  AppLanguageCode,
  FontSizeScale
} from '../types';
import { GAME_CATALOG, getGamesByCategory } from '../data/gameCatalog';
import { t } from '../i18n/translations';
import { StatCard } from '../components/StatCard';
import { AccuracyTrendChart, ResponseTimeChart, CategoryBreakdownChart } from '../components/Charts';
import { DisclaimerBanner } from '../components/DisclaimerBanner';

interface PatientMainScreenProps {
  currentTab: PatientTab;
  onSelectTab: (tab: PatientTab) => void;
  user: UserEntity;
  attempts: GameAttemptEntity[];
  routines: RoutineItemEntity[];
  onToggleRoutine: (id: string, completed: boolean) => void;
  currentLanguage: AppLanguageCode;
  onLanguageSelected: (lang: AppLanguageCode) => void;
  highContrast: boolean;
  onToggleHighContrast: () => void;
  fontSizeScale: FontSizeScale;
  onCycleFontSize: () => void;
  largeButtons: boolean;
  onToggleLargeButtons: () => void;
  voiceEnabled: boolean;
  onToggleVoice: () => void;
  onSpeak: (text: string) => void;
  onLaunchGame: (gameId: string, level: number) => void;
  onLogout: () => void;
}

export const PatientMainScreen: React.FC<PatientMainScreenProps> = ({
  currentTab,
  onSelectTab,
  user,
  attempts,
  routines,
  onToggleRoutine,
  currentLanguage,
  onLanguageSelected,
  highContrast,
  onToggleHighContrast,
  fontSizeScale,
  onCycleFontSize,
  largeButtons,
  onToggleLargeButtons,
  voiceEnabled,
  onToggleVoice,
  onSpeak,
  onLaunchGame,
  onLogout
}) => {
  const [selectedCategory, setSelectedCategory] = useState<GameCategory>('all');
  const [selectedLevels, setSelectedLevels] = useState<Record<string, number>>({});

  const userAttempts = attempts.filter((a) => a.userId === user.id);
  const totalScore = userAttempts.reduce((acc, curr) => acc + curr.totalScore, 0);
  const avgAccuracy =
    userAttempts.length > 0
      ? Math.round(userAttempts.reduce((acc, curr) => acc + curr.accuracyPercent, 0) / userAttempts.length)
      : 85;
  const avgResponseTimeSec =
    userAttempts.length > 0
      ? (userAttempts.reduce((acc, curr) => acc + curr.avgResponseTimeMs, 0) / userAttempts.length / 1000).toFixed(1)
      : '3.8';

  const getLevelForGame = (gameId: string) => selectedLevels[gameId] || 1;
  const setLevelForGame = (gameId: string, level: number) => {
    setSelectedLevels((prev) => ({ ...prev, [gameId]: level }));
  };

  const navItemClass = (tab: PatientTab) => {
    const isSelected = currentTab === tab;
    if (highContrast) {
      return isSelected
        ? 'bg-yellow-400 text-slate-950 font-extrabold border-2 border-white'
        : 'text-slate-300 hover:text-white hover:bg-slate-800';
    }
    return isSelected
      ? 'bg-teal-700 text-white font-bold shadow-sm'
      : 'text-slate-600 hover:text-teal-800 hover:bg-teal-50';
  };

  const cardStyle = highContrast
    ? 'bg-slate-900 border-2 border-yellow-400 text-white'
    : 'bg-white border border-slate-200 text-slate-900 shadow-sm';

  const fontClass =
    fontSizeScale === 'EXTRA_LARGE'
      ? 'text-lg'
      : fontSizeScale === 'LARGE'
      ? 'text-base'
      : 'text-sm';

  return (
    <div className={`min-h-[calc(100vh-60px)] pb-24 ${highContrast ? 'bg-slate-950 text-white' : 'bg-slate-50 text-slate-900'}`}>
      <div className="max-w-5xl mx-auto px-4 sm:px-6 py-6 space-y-6">
        {/* Navigation Tabs Bar */}
        <nav
          aria-label="Patient navigation"
          className={`flex items-center justify-between p-1.5 rounded-2xl border ${
            highContrast ? 'bg-slate-900 border-yellow-400' : 'bg-white border-slate-200 shadow-sm'
          }`}
        >
          {(
            [
              { tab: 'home', label: t('nav_home', currentLanguage), icon: <Home className="w-5 h-5" /> },
              { tab: 'games', label: t('nav_games', currentLanguage), icon: <Gamepad2 className="w-5 h-5" /> },
              { tab: 'progress', label: t('nav_progress', currentLanguage), icon: <TrendingUp className="w-5 h-5" /> },
              { tab: 'profile', label: t('nav_profile', currentLanguage), icon: <User className="w-5 h-5" /> }
            ] as const
          ).map((item) => (
            <button
              key={item.tab}
              id={`patient-nav-${item.tab}`}
              type="button"
              onClick={() => onSelectTab(item.tab)}
              className={`flex-1 py-3 px-2 rounded-xl flex flex-col sm:flex-row items-center justify-center gap-1.5 text-xs sm:text-sm transition-all ${navItemClass(
                item.tab
              )}`}
            >
              {item.icon}
              <span>{item.label}</span>
            </button>
          ))}
        </nav>

        {/* TAB 1: HOME */}
        {currentTab === 'home' && (
          <div className="space-y-6">
            {/* Welcome Greeting Banner */}
            <div className={`p-6 rounded-3xl relative overflow-hidden ${
              highContrast ? 'bg-slate-900 border-2 border-yellow-400 text-white' : 'bg-gradient-to-r from-teal-800 to-teal-700 text-white shadow-md'
            }`}>
              <div className="relative z-10 space-y-2">
                <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-white/20 text-white">
                  <MapPin className="w-3 h-3" />
                  <span>{user.locationState || 'Assam'}</span>
                  <span>•</span>
                  <span>Code: {user.patientCode}</span>
                </div>
                <h2 className="text-2xl sm:text-3xl font-extrabold tracking-tight">
                  Welcome back, {user.fullName}!
                </h2>
                <p className="text-sm opacity-90 max-w-xl">
                  Take a calm moment for yourself today. Enjoy gentle cognitive games and check your daily routine.
                </p>
                <div className="pt-2 flex flex-wrap gap-2">
                  <button
                    type="button"
                    onClick={() => onSelectTab('games')}
                    className={`py-2.5 px-5 rounded-xl font-bold text-xs sm:text-sm flex items-center gap-2 transition-all ${
                      highContrast
                        ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                        : 'bg-white text-teal-800 hover:bg-teal-50 shadow'
                    }`}
                  >
                    <Play className="w-4 h-4 fill-current" />
                    <span>Start Today's Games</span>
                  </button>
                  {voiceEnabled && (
                    <button
                      type="button"
                      onClick={() =>
                        onSpeak(`Welcome back ${user.fullName}. You are in ${user.locationState}. Would you like to play some cognitive games?`)
                      }
                      className="py-2.5 px-4 rounded-xl font-bold text-xs sm:text-sm flex items-center gap-2 bg-white/15 hover:bg-white/25 text-white"
                    >
                      <Volume2 className="w-4 h-4" />
                      <span>Listen Greeting</span>
                    </button>
                  )}
                </div>
              </div>
            </div>

            {/* Quick Stats Grid */}
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
              <StatCard
                id="stat-score"
                label={t('label_score', currentLanguage)}
                value={totalScore}
                subtitle="Earned Points"
                icon={<Award className="w-5 h-5" />}
                highContrast={highContrast}
              />
              <StatCard
                id="stat-accuracy"
                label={t('label_accuracy', currentLanguage)}
                value={`${avgAccuracy}%`}
                subtitle="Overall"
                icon={<TrendingUp className="w-5 h-5" />}
                highContrast={highContrast}
              />
              <StatCard
                id="stat-time"
                label={t('label_response_time', currentLanguage)}
                value={`${avgResponseTimeSec}s`}
                subtitle="Per Question"
                icon={<Clock className="w-5 h-5" />}
                highContrast={highContrast}
              />
              <StatCard
                id="stat-games"
                label="Games"
                value={userAttempts.length}
                subtitle="Completed"
                icon={<Gamepad2 className="w-5 h-5" />}
                highContrast={highContrast}
              />
            </div>

            {/* Daily Routine Section */}
            <div className={`p-6 rounded-3xl ${cardStyle}`}>
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center gap-2">
                  <Calendar className={`w-5 h-5 ${highContrast ? 'text-yellow-400' : 'text-teal-700'}`} />
                  <h3 className="text-lg font-extrabold tracking-tight">Today's Daily Routine</h3>
                </div>
                <span className="text-xs font-semibold opacity-70">
                  {routines.filter((r) => r.isCompleted).length}/{routines.length} Done
                </span>
              </div>

              <div className="space-y-2.5">
                {routines.map((routine) => (
                  <div
                    key={routine.id}
                    onClick={() => onToggleRoutine(routine.id, !routine.isCompleted)}
                    className={`p-3.5 rounded-2xl flex items-center justify-between gap-3 cursor-pointer transition-all ${
                      routine.isCompleted
                        ? highContrast
                          ? 'bg-slate-800/80 border border-slate-700 opacity-75'
                          : 'bg-slate-50 border border-slate-200 opacity-75'
                        : highContrast
                        ? 'bg-slate-800 border-2 border-yellow-400 hover:bg-slate-750'
                        : 'bg-white border border-teal-200 hover:border-teal-400 shadow-sm'
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      {routine.isCompleted ? (
                        <CheckCircle2 className={`w-6 h-6 flex-shrink-0 ${highContrast ? 'text-yellow-400' : 'text-emerald-600'}`} />
                      ) : (
                        <Circle className="w-6 h-6 flex-shrink-0 text-slate-400" />
                      )}
                      <div>
                        <span className={`text-xs font-bold uppercase tracking-wider block ${
                          highContrast ? 'text-yellow-300' : 'text-teal-700'
                        }`}>
                          {routine.timeStr} • {routine.period}
                        </span>
                        <h4 className={`text-sm sm:text-base font-bold ${routine.isCompleted ? 'line-through text-slate-400' : ''}`}>
                          {routine.title}
                        </h4>
                        <p className="text-xs text-slate-500">{routine.description}</p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Quick Play 2 Games Feature */}
            <div className={`p-6 rounded-3xl ${cardStyle}`}>
              <h3 className="text-lg font-extrabold mb-4">Recommended for Today</h3>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {GAME_CATALOG.slice(0, 2).map((game) => (
                  <div
                    key={game.id}
                    className={`p-4 rounded-2xl border flex flex-col justify-between ${
                      highContrast ? 'bg-slate-800 border-yellow-400' : 'bg-teal-50/60 border-teal-200'
                    }`}
                  >
                    <div>
                      <div className="text-3xl mb-2">{game.iconEmoji}</div>
                      <h4 className="text-base font-bold">{t(game.titleKey, currentLanguage)}</h4>
                      <p className="text-xs text-slate-500 mb-4">{t(game.subtitleKey, currentLanguage)}</p>
                    </div>
                    <button
                      type="button"
                      onClick={() => onLaunchGame(game.id, 1)}
                      className={`w-full py-2.5 rounded-xl font-bold text-xs sm:text-sm flex items-center justify-center gap-2 ${
                        highContrast
                          ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                          : 'bg-teal-700 text-white hover:bg-teal-800'
                      }`}
                    >
                      <Play className="w-4 h-4 fill-current" />
                      <span>Play Gentle (Level 1)</span>
                    </button>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* TAB 2: GAMES */}
        {currentTab === 'games' && (
          <div className="space-y-6">
            <div>
              <h2 className="text-2xl font-extrabold tracking-tight mb-1">Cognitive Games</h2>
              <p className="text-xs sm:text-sm text-slate-500">
                Gentle, culturally attuned brain exercises designed with zero pressure.
              </p>
            </div>

            {/* Category Filter Chips */}
            <div className="flex flex-wrap gap-2">
              {(
                [
                  { cat: 'all', label: t('cat_all', currentLanguage) },
                  { cat: 'orientation', label: t('cat_orientation', currentLanguage) },
                  { cat: 'memory', label: t('cat_memory', currentLanguage) },
                  { cat: 'attention', label: t('cat_attention', currentLanguage) },
                  { cat: 'reasoning', label: t('cat_reasoning', currentLanguage) }
                ] as const
              ).map((item) => (
                <button
                  key={item.cat}
                  type="button"
                  onClick={() => setSelectedCategory(item.cat)}
                  className={`py-2 px-4 rounded-xl text-xs sm:text-sm font-bold transition-all ${
                    selectedCategory === item.cat
                      ? highContrast
                        ? 'bg-yellow-400 text-slate-950 border-2 border-white'
                        : 'bg-teal-700 text-white shadow-sm'
                      : highContrast
                      ? 'bg-slate-900 border border-slate-700 text-slate-300 hover:text-white'
                      : 'bg-white border border-slate-300 text-slate-700 hover:bg-slate-100'
                  }`}
                >
                  {item.label}
                </button>
              ))}
            </div>

            {/* Game Cards Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {getGamesByCategory(selectedCategory).map((game) => {
                const currentLevel = getLevelForGame(game.id);
                return (
                  <div
                    key={game.id}
                    id={`game-card-${game.id}`}
                    className={`p-5 rounded-3xl flex flex-col justify-between space-y-4 ${cardStyle}`}
                  >
                    <div>
                      <div className="flex items-start justify-between gap-3 mb-2">
                        <div className="w-12 h-12 rounded-2xl bg-teal-100 dark:bg-slate-800 flex items-center justify-center text-2xl">
                          {game.iconEmoji}
                        </div>
                        <span className={`text-[11px] font-bold uppercase tracking-wider px-2.5 py-1 rounded-full ${
                          highContrast ? 'bg-yellow-400 text-slate-950' : 'bg-teal-100 text-teal-800'
                        }`}>
                          {game.category}
                        </span>
                      </div>

                      <h3 className="text-lg font-bold">{t(game.titleKey, currentLanguage)}</h3>
                      <p className="text-xs text-slate-500 mt-1">{t(game.subtitleKey, currentLanguage)}</p>
                    </div>

                    {/* Level Picker */}
                    <div className="space-y-2 pt-2 border-t border-slate-100 dark:border-slate-800">
                      <div className="flex justify-between items-center text-xs font-semibold text-slate-500">
                        <span>Select Difficulty</span>
                        <span className="font-bold text-teal-700 dark:text-yellow-400">
                          {currentLevel === 1 ? t('level_1', currentLanguage) : currentLevel === 2 ? t('level_2', currentLanguage) : t('level_3', currentLanguage)}
                        </span>
                      </div>
                      <div className="grid grid-cols-3 gap-1.5">
                        {[1, 2, 3].map((lvl) => (
                          <button
                            key={lvl}
                            type="button"
                            onClick={() => setLevelForGame(game.id, lvl)}
                            className={`py-1.5 px-2 rounded-lg text-xs font-bold transition-colors ${
                              currentLevel === lvl
                                ? highContrast
                                  ? 'bg-yellow-400 text-slate-950 font-black'
                                  : 'bg-teal-700 text-white'
                                : highContrast
                                ? 'bg-slate-800 text-slate-300 hover:bg-slate-700'
                                : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                            }`}
                          >
                            Lvl {lvl}
                          </button>
                        ))}
                      </div>
                    </div>

                    {/* Play Button */}
                    <button
                      type="button"
                      onClick={() => onLaunchGame(game.id, currentLevel)}
                      className={`w-full py-3.5 rounded-xl font-extrabold text-sm flex items-center justify-center gap-2 shadow transition-all ${
                        largeButtons ? 'py-4 text-base' : ''
                      } ${
                        highContrast
                          ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                          : 'bg-teal-700 hover:bg-teal-800 text-white'
                      }`}
                    >
                      <Play className="w-4 h-4 fill-current" />
                      <span>Play Now</span>
                    </button>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* TAB 3: PROGRESS */}
        {currentTab === 'progress' && (
          <div className="space-y-6">
            <div>
              <h2 className="text-2xl font-extrabold tracking-tight mb-1">Your Engagement Progress</h2>
              <p className="text-xs sm:text-sm text-slate-500">
                Track how often you play and your comfortable response rhythms.
              </p>
            </div>

            {/* Charts Grid */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div className={`p-5 rounded-3xl ${cardStyle}`}>
                <h3 className="text-base font-extrabold mb-1">Accuracy Trend (%)</h3>
                <p className="text-xs text-slate-500 mb-3">Recent game sessions</p>
                <AccuracyTrendChart attempts={userAttempts} highContrast={highContrast} />
              </div>

              <div className={`p-5 rounded-3xl ${cardStyle}`}>
                <h3 className="text-base font-extrabold mb-1">Average Response Time (Seconds)</h3>
                <p className="text-xs text-slate-500 mb-3">Time taken per question comfortably</p>
                <ResponseTimeChart attempts={userAttempts} highContrast={highContrast} />
              </div>
            </div>

            {/* Domain Breakdown */}
            <div className={`p-6 rounded-3xl ${cardStyle}`}>
              <h3 className="text-base font-extrabold mb-1">Cognitive Domain Activity</h3>
              <p className="text-xs text-slate-500 mb-4">Accuracy and activity across different skills</p>
              <CategoryBreakdownChart attempts={userAttempts} highContrast={highContrast} />
            </div>

            {/* Historical Attempt List */}
            <div className={`p-6 rounded-3xl ${cardStyle}`}>
              <h3 className="text-base font-extrabold mb-4">Recent Session Log</h3>
              {userAttempts.length === 0 ? (
                <p className="text-sm text-slate-400 text-center py-6">No game sessions logged yet.</p>
              ) : (
                <div className="space-y-3">
                  {[...userAttempts].reverse().slice(0, 5).map((att) => (
                    <div
                      key={att.id}
                      className={`p-3.5 rounded-2xl border flex flex-col sm:flex-row sm:items-center justify-between gap-3 ${
                        highContrast ? 'bg-slate-800 border-slate-700' : 'bg-slate-50 border-slate-200'
                      }`}
                    >
                      <div>
                        <div className="flex items-center gap-2">
                          <span className="font-bold text-sm capitalize">
                            {att.gameId.replace('_', ' ')} (Level {att.level})
                          </span>
                          <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full ${
                            highContrast ? 'bg-yellow-400 text-slate-950' : 'bg-teal-100 text-teal-800'
                          }`}>
                            {att.accuracyPercent}% Accuracy
                          </span>
                        </div>
                        <p className="text-xs text-slate-500 mt-1">{att.recommendationMessage}</p>
                      </div>
                      <div className="text-right flex-shrink-0">
                        <span className="text-sm font-extrabold block text-teal-700 dark:text-yellow-400">
                          +{att.totalScore} pts
                        </span>
                        <span className="text-[11px] text-slate-400 block">
                          {(att.avgResponseTimeMs / 1000).toFixed(1)}s avg
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        )}

        {/* TAB 4: PROFILE */}
        {currentTab === 'profile' && (
          <div className="space-y-6">
            <div>
              <h2 className="text-2xl font-extrabold tracking-tight mb-1">Patient Profile & Settings</h2>
              <p className="text-xs sm:text-sm text-slate-500">
                Personalize your experience, language, and accessibility preferences.
              </p>
            </div>

            {/* Profile Info Card */}
            <div className={`p-6 rounded-3xl ${cardStyle}`}>
              <div className="flex items-center gap-4 mb-4">
                <div className="w-16 h-16 rounded-2xl bg-teal-100 text-teal-800 flex items-center justify-center font-extrabold text-2xl">
                  {user.fullName.charAt(0)}
                </div>
                <div>
                  <h3 className="text-xl font-extrabold">{user.fullName}</h3>
                  <p className="text-xs text-slate-500">
                    Age: {user.age || 68} • Location: {user.locationState}
                  </p>
                  <div className="mt-1.5 inline-flex items-center gap-1.5 px-3 py-1 rounded-lg bg-teal-50 dark:bg-slate-800 border border-teal-200 dark:border-yellow-400 text-xs font-bold text-teal-900 dark:text-yellow-300">
                    <span>Patient Code:</span>
                    <span className="font-mono text-sm tracking-wider">{user.patientCode}</span>
                  </div>
                </div>
              </div>
              <p className="text-xs text-slate-400">
                Share this unique Patient Code with your family caregiver so they can securely link and monitor your cognitive exercises.
              </p>
            </div>

            {/* Accessibility Center */}
            <div className={`p-6 rounded-3xl ${cardStyle} space-y-4`}>
              <h3 className="text-base font-extrabold mb-2 flex items-center gap-2">
                <Sliders className="w-4 h-4 text-teal-700 dark:text-yellow-400" />
                <span>Accessibility Options</span>
              </h3>

              {/* High Contrast */}
              <div className="flex items-center justify-between p-3 rounded-xl bg-slate-50 dark:bg-slate-800">
                <div>
                  <span className="font-bold text-sm block">High Contrast Mode</span>
                  <span className="text-xs text-slate-500">Maximum visibility and sharp borders</span>
                </div>
                <button
                  type="button"
                  onClick={onToggleHighContrast}
                  className={`w-12 h-7 rounded-full transition-colors relative ${
                    highContrast ? 'bg-yellow-400' : 'bg-slate-300'
                  }`}
                >
                  <div className={`w-5 h-5 rounded-full bg-white transition-transform absolute top-1 ${
                    highContrast ? 'translate-x-6 bg-slate-950' : 'translate-x-1'
                  }`} />
                </button>
              </div>

              {/* Large Buttons */}
              <div className="flex items-center justify-between p-3 rounded-xl bg-slate-50 dark:bg-slate-800">
                <div>
                  <span className="font-bold text-sm block">Large Touch Buttons</span>
                  <span className="text-xs text-slate-500">Easier tapping for shaky hands or tablets</span>
                </div>
                <button
                  type="button"
                  onClick={onToggleLargeButtons}
                  className={`w-12 h-7 rounded-full transition-colors relative ${
                    largeButtons ? 'bg-teal-700 dark:bg-yellow-400' : 'bg-slate-300'
                  }`}
                >
                  <div className={`w-5 h-5 rounded-full bg-white transition-transform absolute top-1 ${
                    largeButtons ? 'translate-x-6' : 'translate-x-1'
                  }`} />
                </button>
              </div>

              {/* Voice Read Aloud */}
              <div className="flex items-center justify-between p-3 rounded-xl bg-slate-50 dark:bg-slate-800">
                <div>
                  <span className="font-bold text-sm block">Voice Read-Aloud (TTS)</span>
                  <span className="text-xs text-slate-500">Read questions and feedback aloud</span>
                </div>
                <button
                  type="button"
                  onClick={onToggleVoice}
                  className={`w-12 h-7 rounded-full transition-colors relative ${
                    voiceEnabled ? 'bg-teal-700 dark:bg-yellow-400' : 'bg-slate-300'
                  }`}
                >
                  <div className={`w-5 h-5 rounded-full bg-white transition-transform absolute top-1 ${
                    voiceEnabled ? 'translate-x-6' : 'translate-x-1'
                  }`} />
                </button>
              </div>
            </div>

            {/* Logout Button */}
            <div className="pt-2">
              <button
                type="button"
                onClick={onLogout}
                className="w-full py-3 px-4 rounded-xl font-bold text-sm text-rose-600 dark:text-rose-400 border border-rose-200 dark:border-rose-900 bg-rose-50/50 dark:bg-rose-950/40 hover:bg-rose-100 flex items-center justify-center gap-2 transition-colors"
              >
                <LogOut className="w-4 h-4" />
                <span>Log Out of Profile</span>
              </button>
            </div>
          </div>
        )}

        {/* Global Medical Disclaimer */}
        <DisclaimerBanner currentLanguage={currentLanguage} highContrast={highContrast} />
      </div>
    </div>
  );
};
