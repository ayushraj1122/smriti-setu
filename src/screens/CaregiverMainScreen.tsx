import React, { useState } from 'react';
import {
  LayoutDashboard,
  TrendingUp,
  Bell,
  CalendarCheck,
  Settings,
  User,
  Plus,
  Trash2,
  CheckCircle2,
  Circle,
  AlertTriangle,
  TrendingDown,
  Minus,
  Sparkles,
  Link as LinkIcon,
  LogOut,
  Clock,
  Pill,
  Droplets,
  Calendar,
  Gamepad2,
  Activity
} from 'lucide-react';
import {
  UserEntity,
  GameAttemptEntity,
  ReminderEntity,
  RoutineItemEntity,
  CaregiverTab,
  ReminderCategory,
  DayPeriod,
  AppLanguageCode,
  FontSizeScale
} from '../types';
import { AdaptiveEngine } from '../engine/scoring';
import { StorageService } from '../data/storage';
import { t } from '../i18n/translations';
import { StatCard } from '../components/StatCard';
import { AccuracyTrendChart, ResponseTimeChart, CategoryBreakdownChart } from '../components/Charts';
import { DisclaimerBanner } from '../components/DisclaimerBanner';

interface CaregiverMainScreenProps {
  currentTab: CaregiverTab;
  onSelectTab: (tab: CaregiverTab) => void;
  caregiverUser: UserEntity;
  linkedPatient: UserEntity;
  allPatients: UserEntity[];
  onSelectPatient: (patient: UserEntity) => void;
  attempts: GameAttemptEntity[];
  reminders: ReminderEntity[];
  onToggleReminder: (id: string, isCompleted: boolean) => void;
  onAddReminder: (reminder: ReminderEntity) => void;
  onDeleteReminder: (id: string) => void;
  routines: RoutineItemEntity[];
  onToggleRoutine: (id: string, isCompleted: boolean) => void;
  onAddRoutine: (routine: RoutineItemEntity) => void;
  onDeleteRoutine: (id: string) => void;
  onLinkPatientByCode: (code: string) => { success: boolean; message: string };
  currentLanguage: AppLanguageCode;
  onLanguageSelected: (lang: AppLanguageCode) => void;
  highContrast: boolean;
  onToggleHighContrast: () => void;
  fontSizeScale: FontSizeScale;
  onCycleFontSize: () => void;
  voiceEnabled: boolean;
  onToggleVoice: () => void;
  onSpeak: (text: string) => void;
  onLogout: () => void;
}

export const CaregiverMainScreen: React.FC<CaregiverMainScreenProps> = ({
  currentTab,
  onSelectTab,
  caregiverUser,
  linkedPatient,
  allPatients,
  onSelectPatient,
  attempts,
  reminders,
  onToggleReminder,
  onAddReminder,
  onDeleteReminder,
  routines,
  onToggleRoutine,
  onAddRoutine,
  onDeleteRoutine,
  onLinkPatientByCode,
  currentLanguage,
  highContrast,
  onToggleHighContrast,
  fontSizeScale,
  onCycleFontSize,
  voiceEnabled,
  onToggleVoice,
  onSpeak,
  onLogout
}) => {
  // Modal states for adding reminders and routines
  const [showAddReminderModal, setShowAddReminderModal] = useState(false);
  const [remTitle, setRemTitle] = useState('');
  const [remDesc, setRemDesc] = useState('');
  const [remCat, setRemCat] = useState<ReminderCategory>('MEDICATION');
  const [remTime, setRemTime] = useState('09:00 AM');

  const [showAddRoutineModal, setShowAddRoutineModal] = useState(false);
  const [routPeriod, setRoutPeriod] = useState<DayPeriod>('MORNING');
  const [routTitle, setRoutTitle] = useState('');
  const [routDesc, setRoutDesc] = useState('');
  const [routTime, setRoutTime] = useState('08:00 AM');

  const [linkCodeInput, setLinkCodeInput] = useState('');
  const [linkMsg, setLinkMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  const patientAttempts = attempts.filter((a) => a.userId === linkedPatient.id);
  const trendAnalysis = AdaptiveEngine.evaluatePerformanceTrend(patientAttempts);

  const handleCreateReminder = (e: React.FormEvent) => {
    e.preventDefault();
    if (!remTitle.trim()) return;

    const newReminder: ReminderEntity = {
      id: `rem_${Date.now()}`,
      patientId: linkedPatient.id,
      title: remTitle.trim(),
      description: remDesc.trim(),
      category: remCat,
      timeStr: remTime,
      dateStr: 'Daily',
      repeatFrequency: 'Daily',
      isActive: true,
      isCompleted: false,
      createdAt: Date.now()
    };
    onAddReminder(newReminder);
    setShowAddReminderModal(false);
    setRemTitle('');
    setRemDesc('');
  };

  const handleCreateRoutine = (e: React.FormEvent) => {
    e.preventDefault();
    if (!routTitle.trim()) return;

    const newRoutine: RoutineItemEntity = {
      id: `rt_${Date.now()}`,
      patientId: linkedPatient.id,
      period: routPeriod,
      timeStr: routTime,
      title: routTitle.trim(),
      description: routDesc.trim(),
      isCompleted: false,
      orderIndex: routines.length + 1
    };
    onAddRoutine(newRoutine);
    setShowAddRoutineModal(false);
    setRoutTitle('');
    setRoutDesc('');
  };

  const handleLinkCodeSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!linkCodeInput.trim()) return;
    const res = onLinkPatientByCode(linkCodeInput.trim());
    if (res.success) {
      setLinkMsg({ type: 'success', text: res.message });
      setLinkCodeInput('');
    } else {
      setLinkMsg({ type: 'error', text: res.message });
    }
  };

  const cardStyle = highContrast
    ? 'bg-slate-900 border-2 border-yellow-400 text-white'
    : 'bg-white border border-slate-200 text-slate-900 shadow-sm';

  const navItemClass = (tab: CaregiverTab) => {
    const isSelected = currentTab === tab;
    if (highContrast) {
      return isSelected
        ? 'bg-yellow-400 text-slate-950 font-extrabold border-2 border-white'
        : 'text-slate-300 hover:text-white hover:bg-slate-800';
    }
    return isSelected
      ? 'bg-indigo-700 text-white font-bold shadow-sm'
      : 'text-slate-600 hover:text-indigo-800 hover:bg-indigo-50';
  };

  const getCategoryIcon = (cat: ReminderCategory) => {
    switch (cat) {
      case 'MEDICATION':
        return <Pill className="w-4 h-4 text-rose-500" />;
      case 'HYDRATION':
        return <Droplets className="w-4 h-4 text-sky-500" />;
      case 'GAME_PRACTICE':
        return <Gamepad2 className="w-4 h-4 text-emerald-500" />;
      case 'APPOINTMENT':
        return <Calendar className="w-4 h-4 text-indigo-500" />;
      default:
        return <Clock className="w-4 h-4 text-amber-500" />;
    }
  };

  return (
    <div className={`min-h-[calc(100vh-60px)] pb-24 ${highContrast ? 'bg-slate-950 text-white' : 'bg-slate-50 text-slate-900'}`}>
      <div className="max-w-6xl mx-auto px-4 sm:px-6 py-6 space-y-6">
        {/* Navigation Tabs Bar */}
        <nav
          aria-label="Caregiver navigation"
          className={`flex items-center justify-between p-1.5 rounded-2xl border ${
            highContrast ? 'bg-slate-900 border-yellow-400' : 'bg-white border-slate-200 shadow-sm'
          }`}
        >
          {(
            [
              { tab: 'dashboard', label: 'Overview', icon: <LayoutDashboard className="w-5 h-5" /> },
              { tab: 'progress', label: 'Analytics', icon: <TrendingUp className="w-5 h-5" /> },
              { tab: 'reminders', label: 'Reminders', icon: <Bell className="w-5 h-5" /> },
              { tab: 'routine', label: 'Routines', icon: <CalendarCheck className="w-5 h-5" /> },
              { tab: 'settings', label: 'Settings', icon: <Settings className="w-5 h-5" /> }
            ] as const
          ).map((item) => (
            <button
              key={item.tab}
              id={`caregiver-nav-${item.tab}`}
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

        {/* Patient Selector Switcher Banner */}
        <div className={`p-4 rounded-2xl flex flex-col sm:flex-row sm:items-center justify-between gap-3 border ${
          highContrast ? 'bg-slate-900 border-yellow-400' : 'bg-white border-indigo-100 shadow-sm'
        }`}>
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-indigo-100 text-indigo-800 flex items-center justify-center font-bold text-base">
              {linkedPatient.fullName.charAt(0)}
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="text-xs font-bold uppercase tracking-wider text-indigo-700 dark:text-yellow-400">
                  {t('caregiver_linked_patient', currentLanguage)}:
                </span>
                <span className="font-extrabold text-sm sm:text-base text-slate-900 dark:text-white">
                  {linkedPatient.fullName}
                </span>
              </div>
              <span className="text-xs text-slate-500">
                Code: <strong className="font-mono">{linkedPatient.patientCode}</strong> • Age {linkedPatient.age || 68} ({linkedPatient.locationState})
              </span>
            </div>
          </div>

          {allPatients.length > 1 && (
            <div className="flex items-center gap-2">
              <span className="text-xs text-slate-500">Switch:</span>
              <select
                aria-label="Switch monitored patient"
                value={linkedPatient.id}
                onChange={(e) => {
                  const target = allPatients.find((p) => p.id === e.target.value);
                  if (target) onSelectPatient(target);
                }}
                className={`py-1.5 px-3 rounded-lg text-xs font-bold border ${
                  highContrast ? 'bg-slate-800 border-yellow-400 text-yellow-300' : 'bg-slate-50 border-slate-300 text-slate-700'
                }`}
              >
                {allPatients.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.fullName} ({p.patientCode})
                  </option>
                ))}
              </select>
            </div>
          )}
        </div>

        {/* TAB 1: OVERVIEW DASHBOARD */}
        {currentTab === 'dashboard' && (
          <div className="space-y-6">
            {/* Cognitive Performance Trend Card */}
            <div className={`p-6 rounded-3xl border ${
              trendAnalysis.trend === 'IMPROVING'
                ? highContrast
                  ? 'bg-slate-900 border-2 border-emerald-400'
                  : 'bg-emerald-50/70 border-emerald-200'
                : trendAnalysis.trend === 'NEEDS_ATTENTION'
                ? highContrast
                  ? 'bg-slate-900 border-2 border-amber-400'
                  : 'bg-amber-50/70 border-amber-200'
                : highContrast
                ? 'bg-slate-900 border-2 border-yellow-400'
                : 'bg-indigo-50/70 border-indigo-200'
            }`}>
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-4">
                <div className="flex items-center gap-3">
                  <div className={`w-12 h-12 rounded-2xl flex items-center justify-center font-bold ${
                    trendAnalysis.trend === 'IMPROVING'
                      ? 'bg-emerald-100 text-emerald-800'
                      : trendAnalysis.trend === 'NEEDS_ATTENTION'
                      ? 'bg-amber-100 text-amber-800'
                      : 'bg-indigo-100 text-indigo-800'
                  }`}>
                    {trendAnalysis.trend === 'IMPROVING' ? (
                      <TrendingUp className="w-6 h-6" />
                    ) : trendAnalysis.trend === 'NEEDS_ATTENTION' ? (
                      <AlertTriangle className="w-6 h-6" />
                    ) : (
                      <Minus className="w-6 h-6" />
                    )}
                  </div>
                  <div>
                    <span className="text-xs font-bold uppercase tracking-wider block opacity-75">
                      {t('caregiver_trends', currentLanguage)}
                    </span>
                    <h3 className="text-xl sm:text-2xl font-extrabold tracking-tight">
                      {trendAnalysis.trend === 'IMPROVING'
                        ? 'Steady Cognitive Engagement (Improving)'
                        : trendAnalysis.trend === 'NEEDS_ATTENTION'
                        ? 'Noticeable Variation (Monitor Gently)'
                        : 'Stable Engagement Baseline'}
                    </h3>
                  </div>
                </div>

                <div className="text-left sm:text-right">
                  <span className="text-xs opacity-75 block">Comparison Range</span>
                  <span className="text-xs font-extrabold font-mono">
                    Baseline: {trendAnalysis.baselineAccuracy}% ➔ Recent: {trendAnalysis.recentAccuracy}%
                  </span>
                </div>
              </div>

              <p className="text-xs sm:text-sm leading-relaxed opacity-90 mb-4">
                {trendAnalysis.trend === 'IMPROVING'
                  ? `${linkedPatient.fullName} demonstrates consistent or improved accuracy and steady response pacing across orientation and memory sessions.`
                  : trendAnalysis.trend === 'NEEDS_ATTENTION'
                  ? `${linkedPatient.fullName}'s recent attempts exhibit more variation in accuracy. This is a monitoring indicator, not a clinical diagnosis. Consider checking sleep, hydration, or stress.`
                  : `${linkedPatient.fullName} is maintaining a healthy, steady cognitive baseline with consistent game completion.`}
              </p>

              <div className="p-3 rounded-xl bg-white/60 dark:bg-black/40 text-xs flex items-center gap-2">
                <Sparkles className="w-4 h-4 text-indigo-600 dark:text-yellow-400 flex-shrink-0" />
                <span>{t('caregiver_trend_note', currentLanguage)}</span>
              </div>
            </div>

            {/* Quick Metrics Grid */}
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
              <StatCard
                id="cg-stat-sessions"
                label="Total Sessions"
                value={patientAttempts.length}
                subtitle="Logged Games"
                icon={<Activity className="w-5 h-5" />}
                highContrast={highContrast}
              />
              <StatCard
                id="cg-stat-accuracy"
                label="Recent Accuracy"
                value={`${trendAnalysis.recentAccuracy}%`}
                subtitle={`Baseline: ${trendAnalysis.baselineAccuracy}%`}
                icon={<TrendingUp className="w-5 h-5" />}
                highContrast={highContrast}
              />
              <StatCard
                id="cg-stat-pacing"
                label="Avg Pacing"
                value={`${(trendAnalysis.recentTimeMs / 1000).toFixed(1)}s`}
                subtitle="Per Question"
                icon={<Clock className="w-5 h-5" />}
                highContrast={highContrast}
              />
              <StatCard
                id="cg-stat-reminders"
                label="Active Tasks"
                value={reminders.filter((r) => !r.isCompleted).length}
                subtitle={`${reminders.length} Total`}
                icon={<Bell className="w-5 h-5" />}
                highContrast={highContrast}
              />
            </div>

            {/* Recent Patient Activity Log */}
            <div className={`p-6 rounded-3xl ${cardStyle}`}>
              <h3 className="text-lg font-extrabold mb-4">{t('caregiver_summary', currentLanguage)}</h3>
              {patientAttempts.length === 0 ? (
                <p className="text-sm text-slate-400 text-center py-6">No game activity recorded yet.</p>
              ) : (
                <div className="space-y-3">
                  {[...patientAttempts].reverse().slice(0, 4).map((att) => (
                    <div
                      key={att.id}
                      className={`p-4 rounded-2xl border flex flex-col sm:flex-row sm:items-center justify-between gap-3 ${
                        highContrast ? 'bg-slate-800 border-slate-700' : 'bg-slate-50 border-slate-200'
                      }`}
                    >
                      <div>
                        <div className="flex items-center gap-2">
                          <span className="font-bold text-sm capitalize">
                            {att.gameId.replace('_', ' ')} (Level {att.level})
                          </span>
                          <span className="text-xs font-semibold text-slate-500">
                            • {new Date(att.timestamp).toLocaleDateString()}
                          </span>
                        </div>
                        <p className="text-xs text-slate-500 mt-1">{att.recommendationMessage}</p>
                      </div>
                      <div className="text-right flex-shrink-0">
                        <span className="text-sm font-extrabold block text-indigo-700 dark:text-yellow-400">
                          {att.accuracyPercent}% Accuracy
                        </span>
                        <span className="text-xs text-slate-400">
                          {(att.avgResponseTimeMs / 1000).toFixed(1)}s response time
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        )}

        {/* TAB 2: ANALYTICS */}
        {currentTab === 'progress' && (
          <div className="space-y-6">
            <div>
              <h2 className="text-2xl font-extrabold tracking-tight mb-1">Cognitive Trends & Analytics</h2>
              <p className="text-xs sm:text-sm text-slate-500">
                Detailed longitudinal tracking of {linkedPatient.fullName}'s accuracy and pacing.
              </p>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div className={`p-5 rounded-3xl ${cardStyle}`}>
                <h3 className="text-base font-extrabold mb-1">Accuracy Trend Over Sessions</h3>
                <p className="text-xs text-slate-500 mb-4">Percentage of correct answers over time</p>
                <AccuracyTrendChart attempts={patientAttempts} highContrast={highContrast} />
              </div>

              <div className={`p-5 rounded-3xl ${cardStyle}`}>
                <h3 className="text-base font-extrabold mb-1">Pacing & Response Times</h3>
                <p className="text-xs text-slate-500 mb-4">Average seconds spent per question</p>
                <ResponseTimeChart attempts={patientAttempts} highContrast={highContrast} />
              </div>
            </div>

            <div className={`p-6 rounded-3xl ${cardStyle}`}>
              <h3 className="text-base font-extrabold mb-1">Cognitive Domain Performance</h3>
              <p className="text-xs text-slate-500 mb-4">Breakdown across orientation, memory, attention, and reasoning</p>
              <CategoryBreakdownChart attempts={patientAttempts} highContrast={highContrast} />
            </div>
          </div>
        )}

        {/* TAB 3: REMINDERS MANAGEMENT */}
        {currentTab === 'reminders' && (
          <div className="space-y-6">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
              <div>
                <h2 className="text-2xl font-extrabold tracking-tight mb-1">Reminders & Medication Care</h2>
                <p className="text-xs sm:text-sm text-slate-500">
                  Manage medication, hydration, and doctor appointments for {linkedPatient.fullName}.
                </p>
              </div>

              <button
                type="button"
                onClick={() => setShowAddReminderModal(true)}
                className={`py-2.5 px-4 rounded-xl font-bold text-xs sm:text-sm flex items-center gap-2 shadow transition-all ${
                  highContrast
                    ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                    : 'bg-indigo-700 text-white hover:bg-indigo-800'
                }`}
              >
                <Plus className="w-4 h-4" />
                <span>{t('btn_add_reminder', currentLanguage)}</span>
              </button>
            </div>

            <div className="space-y-3">
              {reminders.map((rem) => (
                <div
                  key={rem.id}
                  className={`p-4 rounded-2xl border flex items-center justify-between gap-3 transition-all ${
                    rem.isCompleted
                      ? 'bg-slate-50 dark:bg-slate-900 border-slate-200 dark:border-slate-800 opacity-70'
                      : cardStyle
                  }`}
                >
                  <div className="flex items-center gap-3">
                    <button
                      type="button"
                      onClick={() => onToggleReminder(rem.id, !rem.isCompleted)}
                      className="p-1 text-slate-400 hover:text-emerald-600"
                    >
                      {rem.isCompleted ? (
                        <CheckCircle2 className="w-6 h-6 text-emerald-600" />
                      ) : (
                        <Circle className="w-6 h-6" />
                      )}
                    </button>
                    <div>
                      <div className="flex items-center gap-2">
                        {getCategoryIcon(rem.category)}
                        <span className="text-xs font-bold uppercase tracking-wider text-slate-500">
                          {rem.category} • {rem.timeStr}
                        </span>
                      </div>
                      <h4 className={`text-base font-bold mt-0.5 ${rem.isCompleted ? 'line-through text-slate-400' : ''}`}>
                        {rem.title}
                      </h4>
                      <p className="text-xs text-slate-500">{rem.description}</p>
                    </div>
                  </div>

                  <button
                    type="button"
                    onClick={() => onDeleteReminder(rem.id)}
                    className="p-2 text-slate-400 hover:text-rose-600 transition-colors"
                    title="Delete Reminder"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              ))}
            </div>

            {/* Add Reminder Modal */}
            {showAddReminderModal && (
              <div className="fixed inset-0 bg-black/60 z-50 flex items-center justify-center p-4">
                <div className={`w-full max-w-md p-6 rounded-3xl ${cardStyle} space-y-4`}>
                  <h3 className="text-lg font-extrabold">Add New Care Reminder</h3>
                  <form onSubmit={handleCreateReminder} className="space-y-3">
                    <div>
                      <label className="block text-xs font-bold uppercase mb-1">Title</label>
                      <input
                        type="text"
                        required
                        value={remTitle}
                        onChange={(e) => setRemTitle(e.target.value)}
                        placeholder="e.g. Afternoon Blood Pressure Tablet"
                        className="w-full p-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-transparent text-sm"
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-bold uppercase mb-1">Instructions / Description</label>
                      <input
                        type="text"
                        value={remDesc}
                        onChange={(e) => setRemDesc(e.target.value)}
                        placeholder="e.g. Take 1 tablet with fresh water after food"
                        className="w-full p-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-transparent text-sm"
                      />
                    </div>
                    <div className="grid grid-cols-2 gap-2">
                      <div>
                        <label className="block text-xs font-bold uppercase mb-1">Category</label>
                        <select
                          value={remCat}
                          onChange={(e) => setRemCat(e.target.value as ReminderCategory)}
                          className="w-full p-2 rounded-xl border border-slate-300 dark:border-slate-700 bg-transparent text-xs"
                        >
                          <option value="MEDICATION">Medication</option>
                          <option value="HYDRATION">Hydration</option>
                          <option value="GAME_PRACTICE">Brain Games</option>
                          <option value="APPOINTMENT">Appointment</option>
                          <option value="DAILY_ROUTINE">Routine</option>
                          <option value="CUSTOM">Custom</option>
                        </select>
                      </div>
                      <div>
                        <label className="block text-xs font-bold uppercase mb-1">Scheduled Time</label>
                        <input
                          type="text"
                          value={remTime}
                          onChange={(e) => setRemTime(e.target.value)}
                          placeholder="09:00 AM"
                          className="w-full p-2 rounded-xl border border-slate-300 dark:border-slate-700 bg-transparent text-xs"
                        />
                      </div>
                    </div>
                    <div className="flex justify-end gap-2 pt-3">
                      <button
                        type="button"
                        onClick={() => setShowAddReminderModal(false)}
                        className="py-2 px-4 rounded-xl text-xs font-bold border border-slate-300 dark:border-slate-700"
                      >
                        Cancel
                      </button>
                      <button
                        type="submit"
                        className={`py-2 px-4 rounded-xl text-xs font-bold ${
                          highContrast ? 'bg-yellow-400 text-slate-950' : 'bg-indigo-700 text-white'
                        }`}
                      >
                        Save Reminder
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            )}
          </div>
        )}

        {/* TAB 4: ROUTINE MANAGEMENT */}
        {currentTab === 'routine' && (
          <div className="space-y-6">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
              <div>
                <h2 className="text-2xl font-extrabold tracking-tight mb-1">Daily Routine Schedule</h2>
                <p className="text-xs sm:text-sm text-slate-500">
                  Structure predictable, comforting morning, afternoon, and evening patterns for {linkedPatient.fullName}.
                </p>
              </div>

              <button
                type="button"
                onClick={() => setShowAddRoutineModal(true)}
                className={`py-2.5 px-4 rounded-xl font-bold text-xs sm:text-sm flex items-center gap-2 shadow transition-all ${
                  highContrast
                    ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                    : 'bg-indigo-700 text-white hover:bg-indigo-800'
                }`}
              >
                <Plus className="w-4 h-4" />
                <span>{t('btn_add_routine', currentLanguage)}</span>
              </button>
            </div>

            <div className="space-y-3">
              {routines.map((routine) => (
                <div
                  key={routine.id}
                  className={`p-4 rounded-2xl border flex items-center justify-between gap-3 ${cardStyle}`}
                >
                  <div className="flex items-center gap-3">
                    <button
                      type="button"
                      onClick={() => onToggleRoutine(routine.id, !routine.isCompleted)}
                      className="p-1 text-slate-400 hover:text-emerald-600"
                    >
                      {routine.isCompleted ? (
                        <CheckCircle2 className="w-6 h-6 text-emerald-600" />
                      ) : (
                        <Circle className="w-6 h-6" />
                      )}
                    </button>
                    <div>
                      <span className="text-xs font-bold uppercase tracking-wider text-indigo-700 dark:text-yellow-400">
                        {routine.period} • {routine.timeStr}
                      </span>
                      <h4 className={`text-base font-bold ${routine.isCompleted ? 'line-through text-slate-400' : ''}`}>
                        {routine.title}
                      </h4>
                      <p className="text-xs text-slate-500">{routine.description}</p>
                    </div>
                  </div>

                  <button
                    type="button"
                    onClick={() => onDeleteRoutine(routine.id)}
                    className="p-2 text-slate-400 hover:text-rose-600 transition-colors"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              ))}
            </div>

            {/* Add Routine Modal */}
            {showAddRoutineModal && (
              <div className="fixed inset-0 bg-black/60 z-50 flex items-center justify-center p-4">
                <div className={`w-full max-w-md p-6 rounded-3xl ${cardStyle} space-y-4`}>
                  <h3 className="text-lg font-extrabold">Add Daily Routine Item</h3>
                  <form onSubmit={handleCreateRoutine} className="space-y-3">
                    <div>
                      <label className="block text-xs font-bold uppercase mb-1">Routine Action</label>
                      <input
                        type="text"
                        required
                        value={routTitle}
                        onChange={(e) => setRoutTitle(e.target.value)}
                        placeholder="e.g. Garden Walk and Fresh Tea"
                        className="w-full p-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-transparent text-sm"
                      />
                    </div>
                    <div>
                      <label className="block text-xs font-bold uppercase mb-1">Notes / Description</label>
                      <input
                        type="text"
                        value={routDesc}
                        onChange={(e) => setRoutDesc(e.target.value)}
                        placeholder="e.g. 15 minutes calm walking"
                        className="w-full p-2.5 rounded-xl border border-slate-300 dark:border-slate-700 bg-transparent text-sm"
                      />
                    </div>
                    <div className="grid grid-cols-2 gap-2">
                      <div>
                        <label className="block text-xs font-bold uppercase mb-1">Day Period</label>
                        <select
                          value={routPeriod}
                          onChange={(e) => setRoutPeriod(e.target.value as DayPeriod)}
                          className="w-full p-2 rounded-xl border border-slate-300 dark:border-slate-700 bg-transparent text-xs"
                        >
                          <option value="MORNING">Morning</option>
                          <option value="AFTERNOON">Afternoon</option>
                          <option value="EVENING">Evening</option>
                        </select>
                      </div>
                      <div>
                        <label className="block text-xs font-bold uppercase mb-1">Time</label>
                        <input
                          type="text"
                          value={routTime}
                          onChange={(e) => setRoutTime(e.target.value)}
                          placeholder="08:00 AM"
                          className="w-full p-2 rounded-xl border border-slate-300 dark:border-slate-700 bg-transparent text-xs"
                        />
                      </div>
                    </div>
                    <div className="flex justify-end gap-2 pt-3">
                      <button
                        type="button"
                        onClick={() => setShowAddRoutineModal(false)}
                        className="py-2 px-4 rounded-xl text-xs font-bold border border-slate-300 dark:border-slate-700"
                      >
                        Cancel
                      </button>
                      <button
                        type="submit"
                        className={`py-2 px-4 rounded-xl text-xs font-bold ${
                          highContrast ? 'bg-yellow-400 text-slate-950' : 'bg-indigo-700 text-white'
                        }`}
                      >
                        Save Item
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            )}
          </div>
        )}

        {/* TAB 5: SETTINGS & PATIENT LINKING */}
        {currentTab === 'settings' && (
          <div className="space-y-6">
            <div>
              <h2 className="text-2xl font-extrabold tracking-tight mb-1">Caregiver Settings & Linking</h2>
              <p className="text-xs sm:text-sm text-slate-500">
                Link new patients via code, review security, and configure notifications.
              </p>
            </div>

            {/* Link New Patient Card */}
            <div className={`p-6 rounded-3xl ${cardStyle} space-y-4`}>
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-xl bg-indigo-100 text-indigo-800 flex items-center justify-center font-bold">
                  <LinkIcon className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="text-base font-extrabold">Link Patient by Code</h3>
                  <p className="text-xs text-slate-500">Enter the patient's unique 8-character NER code</p>
                </div>
              </div>

              {linkMsg && (
                <div
                  className={`p-3 rounded-xl text-xs font-bold ${
                    linkMsg.type === 'success'
                      ? 'bg-emerald-50 text-emerald-800 border border-emerald-200'
                      : 'bg-rose-50 text-rose-800 border border-rose-200'
                  }`}
                >
                  {linkMsg.text}
                </div>
              )}

              <form onSubmit={handleLinkCodeSubmit} className="flex gap-2">
                <input
                  type="text"
                  value={linkCodeInput}
                  onChange={(e) => setLinkCodeInput(e.target.value)}
                  placeholder="e.g. NER-6842"
                  className="flex-1 p-3 rounded-xl border border-slate-300 dark:border-slate-700 bg-transparent text-sm font-mono uppercase"
                />
                <button
                  type="submit"
                  className={`py-3 px-6 rounded-xl text-sm font-extrabold shadow ${
                    highContrast ? 'bg-yellow-400 text-slate-950' : 'bg-indigo-700 text-white hover:bg-indigo-800'
                  }`}
                >
                  Link Patient
                </button>
              </form>
            </div>

            {/* Account Card */}
            <div className={`p-6 rounded-3xl ${cardStyle}`}>
              <h3 className="text-base font-extrabold mb-2">Caregiver Account</h3>
              <p className="text-sm font-bold">{caregiverUser.fullName}</p>
              <p className="text-xs text-slate-500">{caregiverUser.email}</p>
              <div className="mt-4 pt-4 border-t border-slate-200 dark:border-slate-800">
                <button
                  type="button"
                  onClick={onLogout}
                  className="w-full py-3 px-4 rounded-xl font-bold text-sm text-rose-600 dark:text-rose-400 border border-rose-200 dark:border-rose-900 bg-rose-50/50 dark:bg-rose-950/40 hover:bg-rose-100 flex items-center justify-center gap-2 transition-colors"
                >
                  <LogOut className="w-4 h-4" />
                  <span>Log Out of Caregiver Portal</span>
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Medical Disclaimer */}
        <DisclaimerBanner currentLanguage={currentLanguage} highContrast={highContrast} />
      </div>
    </div>
  );
};
