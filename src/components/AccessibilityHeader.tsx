import React from 'react';
import { Volume2, VolumeX, Eye, Type, Globe, Sparkles } from 'lucide-react';
import { AppLanguageCode, FontSizeScale } from '../types';
import { SUPPORTED_LANGUAGES, t } from '../i18n/translations';

interface AccessibilityHeaderProps {
  currentLanguage: AppLanguageCode;
  onLanguageSelected: (lang: AppLanguageCode) => void;
  voiceEnabled: boolean;
  onToggleVoice: () => void;
  highContrast: boolean;
  onToggleHighContrast: () => void;
  fontSizeScale: FontSizeScale;
  onCycleFontSize: () => void;
  onSpeakCurrentContext?: () => void;
  title?: string;
}

export const AccessibilityHeader: React.FC<AccessibilityHeaderProps> = ({
  currentLanguage,
  onLanguageSelected,
  voiceEnabled,
  onToggleVoice,
  highContrast,
  onToggleHighContrast,
  fontSizeScale,
  onCycleFontSize,
  onSpeakCurrentContext,
  title
}) => {
  const currentLangObj = SUPPORTED_LANGUAGES.find((l) => l.code === currentLanguage) || SUPPORTED_LANGUAGES[0];

  const headerBg = highContrast ? 'bg-slate-900 border-b-2 border-yellow-400 text-yellow-400' : 'bg-white border-b border-slate-200 text-slate-800 shadow-sm';
  const buttonStyle = highContrast
    ? 'bg-slate-800 border-2 border-yellow-400 text-yellow-400 hover:bg-slate-700'
    : 'bg-slate-100 border border-slate-300 text-slate-700 hover:bg-slate-200';

  return (
    <header className={`${headerBg} sticky top-0 z-40 transition-colors duration-150`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 py-3 flex flex-wrap items-center justify-between gap-3">
        {/* Brand or Page Title */}
        <div className="flex items-center gap-3">
          <div className={`w-10 h-10 rounded-xl flex items-center justify-center font-bold text-lg shadow-inner ${
            highContrast ? 'bg-yellow-400 text-slate-950' : 'bg-teal-700 text-white'
          }`}>
            <span>স্ম</span>
          </div>
          <div>
            <h1 className={`font-extrabold tracking-tight text-lg sm:text-xl leading-tight ${highContrast ? 'text-white' : 'text-slate-900'}`}>
              {title || t('app_name', currentLanguage)}
            </h1>
            <p className={`text-xs ${highContrast ? 'text-yellow-300' : 'text-teal-700 font-medium'}`}>
              {t('app_tagline', currentLanguage)}
            </p>
          </div>
        </div>

        {/* Accessibility Toolbar */}
        <div className="flex items-center flex-wrap gap-2">
          {/* Language Selector Dropdown */}
          <div className="relative inline-flex items-center">
            <Globe className="w-4 h-4 mr-1.5 opacity-70" />
            <select
              id="language-selector"
              aria-label="Select application language"
              value={currentLanguage}
              onChange={(e) => onLanguageSelected(e.target.value as AppLanguageCode)}
              className={`text-xs sm:text-sm font-semibold py-1.5 px-3 rounded-lg appearance-none cursor-pointer pr-7 ${buttonStyle}`}
            >
              {SUPPORTED_LANGUAGES.map((lang) => (
                <option key={lang.code} value={lang.code}>
                  {lang.nativeName} ({lang.displayName})
                </option>
              ))}
            </select>
          </div>

          {/* Font Size Scaling */}
          <button
            id="btn-toggle-font-size"
            type="button"
            onClick={onCycleFontSize}
            className={`px-3 py-1.5 rounded-lg text-xs sm:text-sm font-bold flex items-center gap-1.5 transition-colors ${buttonStyle}`}
            title="Adjust Font Size"
            aria-label={`Current font size: ${fontSizeScale}. Tap to change.`}
          >
            <Type className="w-4 h-4" />
            <span>
              {fontSizeScale === 'STANDARD' ? 'A' : fontSizeScale === 'LARGE' ? 'A+' : 'A++'}
            </span>
          </button>

          {/* High Contrast Toggle */}
          <button
            id="btn-toggle-high-contrast"
            type="button"
            onClick={onToggleHighContrast}
            className={`px-3 py-1.5 rounded-lg text-xs sm:text-sm font-semibold flex items-center gap-1.5 transition-colors ${
              highContrast
                ? 'bg-yellow-400 text-slate-950 font-bold border-2 border-white'
                : buttonStyle
            }`}
            title="Toggle High Contrast for Visual Clarity"
            aria-label="Toggle High Contrast"
          >
            <Eye className="w-4 h-4" />
            <span className="hidden sm:inline">Contrast</span>
          </button>

          {/* Voice Read-Aloud Toggle */}
          <button
            id="btn-toggle-voice"
            type="button"
            onClick={onToggleVoice}
            className={`px-3 py-1.5 rounded-lg text-xs sm:text-sm font-semibold flex items-center gap-1.5 transition-colors ${
              voiceEnabled
                ? highContrast
                  ? 'bg-yellow-400 text-slate-950 font-bold'
                  : 'bg-teal-700 text-white font-medium hover:bg-teal-800'
                : buttonStyle
            }`}
            title="Toggle Voice Read Aloud"
            aria-label="Toggle Voice Assistance"
          >
            {voiceEnabled ? <Volume2 className="w-4 h-4" /> : <VolumeX className="w-4 h-4" />}
            <span className="hidden sm:inline">{voiceEnabled ? 'Voice On' : 'Voice Off'}</span>
          </button>

          {/* Context Read Button */}
          {voiceEnabled && onSpeakCurrentContext && (
            <button
              id="btn-speak-context"
              type="button"
              onClick={onSpeakCurrentContext}
              className={`p-2 rounded-lg text-xs sm:text-sm font-bold flex items-center gap-1 ${
                highContrast ? 'bg-amber-400 text-slate-950 hover:bg-amber-300' : 'bg-amber-100 text-amber-900 border border-amber-300 hover:bg-amber-200'
              }`}
              title="Read this screen aloud"
              aria-label="Read Screen Aloud"
            >
              <Sparkles className="w-4 h-4" />
            </button>
          )}
        </div>
      </div>
    </header>
  );
};
