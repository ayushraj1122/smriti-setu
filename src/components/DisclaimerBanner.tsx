import React from 'react';
import { AlertCircle } from 'lucide-react';
import { AppLanguageCode } from '../types';
import { t } from '../i18n/translations';

interface DisclaimerBannerProps {
  currentLanguage: AppLanguageCode;
  highContrast?: boolean;
}

export const DisclaimerBanner: React.FC<DisclaimerBannerProps> = ({
  currentLanguage,
  highContrast
}) => {
  return (
    <div
      id="medical-disclaimer-banner"
      role="note"
      aria-label="Medical Disclaimer"
      className={`rounded-xl p-4 my-3 flex items-start gap-3 text-xs sm:text-sm leading-relaxed ${
        highContrast
          ? 'bg-slate-900 border-2 border-amber-400 text-yellow-300'
          : 'bg-amber-50 border border-amber-200 text-amber-900'
      }`}
    >
      <AlertCircle className={`w-5 h-5 flex-shrink-0 mt-0.5 ${highContrast ? 'text-amber-400' : 'text-amber-700'}`} />
      <div>
        <span className="font-bold block mb-0.5">Medical & Clinical Notice:</span>
        <p>{t('app_disclaimer', currentLanguage)}</p>
      </div>
    </div>
  );
};
