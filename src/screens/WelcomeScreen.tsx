import React from 'react';
import { Play, User, UserCheck, ShieldCheck, HeartHandshake, Sparkles, MapPin } from 'lucide-react';
import { AppLanguageCode, FontSizeScale } from '../types';
import { t } from '../i18n/translations';
import { DisclaimerBanner } from '../components/DisclaimerBanner';

interface WelcomeScreenProps {
  currentLanguage: AppLanguageCode;
  highContrast: boolean;
  fontSizeScale: FontSizeScale;
  onPlayGames: () => void;
  onPatientLogin: () => void;
  onCaregiverLogin: () => void;
  onQuickDemoPatient: () => void;
  onQuickDemoCaregiver: () => void;
  onSpeak: (text: string) => void;
}

export const WelcomeScreen: React.FC<WelcomeScreenProps> = ({
  currentLanguage,
  highContrast,
  fontSizeScale,
  onPlayGames,
  onPatientLogin,
  onCaregiverLogin,
  onQuickDemoPatient,
  onQuickDemoCaregiver,
  onSpeak
}) => {
  const fontClass =
    fontSizeScale === 'EXTRA_LARGE'
      ? 'text-lg'
      : fontSizeScale === 'LARGE'
      ? 'text-base'
      : 'text-sm';

  const welcomeGreeting = `${t('app_name', currentLanguage)}. ${t('app_tagline', currentLanguage)}. Welcome to cognitive exercises and caregiving tools.`;

  return (
    <div className={`min-h-[calc(100vh-60px)] px-4 py-8 sm:py-12 flex flex-col items-center justify-center ${
      highContrast ? 'bg-slate-950 text-white' : 'bg-gradient-to-b from-teal-50/50 via-slate-50 to-white text-slate-900'
    }`}>
      <div className="w-full max-w-2xl mx-auto space-y-6">
        {/* Visual Badge & Emblem */}
        <div className="text-center space-y-3">
          <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full text-xs font-bold uppercase tracking-wider bg-teal-100 text-teal-800 border border-teal-200">
            <MapPin className="w-3.5 h-3.5 text-teal-700" />
            <span>North East India • Cognitive Care</span>
          </div>

          <div className={`w-24 h-24 mx-auto rounded-3xl flex items-center justify-center text-4xl shadow-md ${
            highContrast ? 'bg-yellow-400 text-slate-950 ring-4 ring-white' : 'bg-teal-700 text-white shadow-teal-200'
          }`}>
            <span>🌸</span>
          </div>

          <h2 className="text-3xl sm:text-4xl font-extrabold tracking-tight text-slate-900 dark:text-white">
            {t('app_name', currentLanguage)}
          </h2>

          <p className={`max-w-lg mx-auto font-medium ${fontClass} ${
            highContrast ? 'text-yellow-300' : 'text-slate-600'
          }`}>
            {t('app_tagline', currentLanguage)}
          </p>
        </div>

        {/* Quick Demo Instant Access Buttons */}
        <div className={`p-4 sm:p-5 rounded-2xl border ${
          highContrast ? 'bg-slate-900 border-yellow-400' : 'bg-white border-teal-200 shadow-md shadow-teal-100/50'
        }`}>
          <div className="flex items-center gap-2 mb-3">
            <Sparkles className={`w-4 h-4 ${highContrast ? 'text-yellow-400' : 'text-teal-600'}`} />
            <h3 className={`text-xs sm:text-sm font-bold uppercase tracking-wider ${
              highContrast ? 'text-yellow-400' : 'text-teal-800'
            }`}>
              Quick 1-Click Interactive Test Drive
            </h3>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <button
              id="btn-quick-patient"
              type="button"
              onClick={onQuickDemoPatient}
              className={`p-3.5 rounded-xl font-bold flex items-center gap-3 text-left transition-all ${
                highContrast
                  ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                  : 'bg-teal-700 hover:bg-teal-800 text-white shadow-sm'
              }`}
            >
              <div className="w-9 h-9 rounded-lg bg-black/10 flex items-center justify-center flex-shrink-0">
                <User className="w-5 h-5" />
              </div>
              <div>
                <span className="block text-sm font-extrabold">Patient Mode</span>
                <span className="block text-xs opacity-90">Arun Sharma (Assam, 68)</span>
              </div>
            </button>

            <button
              id="btn-quick-caregiver"
              type="button"
              onClick={onQuickDemoCaregiver}
              className={`p-3.5 rounded-xl font-bold flex items-center gap-3 text-left transition-all ${
                highContrast
                  ? 'bg-slate-800 border-2 border-yellow-400 text-yellow-400 hover:bg-slate-700'
                  : 'bg-slate-800 hover:bg-slate-900 text-white shadow-sm'
              }`}
            >
              <div className="w-9 h-9 rounded-lg bg-white/10 flex items-center justify-center flex-shrink-0">
                <HeartHandshake className="w-5 h-5 text-teal-300" />
              </div>
              <div>
                <span className="block text-sm font-extrabold">Caregiver Mode</span>
                <span className="block text-xs opacity-90">Priya Sharma (Analytics)</span>
              </div>
            </button>
          </div>
        </div>

        {/* Primary Action Buttons */}
        <div className="space-y-3 pt-2">
          <button
            id="btn-play-now"
            type="button"
            onClick={onPlayGames}
            className={`w-full py-4 px-6 rounded-2xl font-extrabold text-base sm:text-lg flex items-center justify-center gap-3 shadow-md transition-all ${
              highContrast
                ? 'bg-white text-slate-950 hover:bg-slate-200'
                : 'bg-emerald-600 hover:bg-emerald-700 text-white shadow-emerald-200'
            }`}
          >
            <Play className="w-6 h-6 fill-current" />
            <span>{t('btn_play_games', currentLanguage)}</span>
          </button>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <button
              id="btn-patient-login"
              type="button"
              onClick={onPatientLogin}
              className={`py-3.5 px-4 rounded-xl font-bold text-sm sm:text-base flex items-center justify-center gap-2 transition-all ${
                highContrast
                  ? 'bg-slate-900 border border-slate-700 text-white hover:bg-slate-800'
                  : 'bg-white border border-slate-300 text-slate-700 hover:bg-slate-100 shadow-sm'
              }`}
            >
              <UserCheck className="w-4 h-4 text-teal-600" />
              <span>{t('btn_patient_login', currentLanguage)}</span>
            </button>

            <button
              id="btn-caregiver-login"
              type="button"
              onClick={onCaregiverLogin}
              className={`py-3.5 px-4 rounded-xl font-bold text-sm sm:text-base flex items-center justify-center gap-2 transition-all ${
                highContrast
                  ? 'bg-slate-900 border border-slate-700 text-white hover:bg-slate-800'
                  : 'bg-white border border-slate-300 text-slate-700 hover:bg-slate-100 shadow-sm'
              }`}
            >
              <ShieldCheck className="w-4 h-4 text-indigo-600" />
              <span>{t('btn_caregiver_login', currentLanguage)}</span>
            </button>
          </div>
        </div>

        {/* Medical Disclaimer */}
        <DisclaimerBanner currentLanguage={currentLanguage} highContrast={highContrast} />
      </div>
    </div>
  );
};
