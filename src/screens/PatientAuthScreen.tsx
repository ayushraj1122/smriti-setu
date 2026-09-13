import React, { useState } from 'react';
import { ArrowLeft, User, MapPin, Globe } from 'lucide-react';
import { AppLanguageCode, NERState, UserEntity, FontSizeScale } from '../types';
import { SUPPORTED_LANGUAGES, t } from '../i18n/translations';
import { StorageService } from '../data/storage';

interface PatientAuthScreenProps {
  mode: 'login' | 'signup';
  currentLanguage: AppLanguageCode;
  highContrast: boolean;
  fontSizeScale: FontSizeScale;
  onSuccess: (user: UserEntity) => void;
  onBack: () => void;
  onSwitchMode: () => void;
}

const NER_STATES: NERState[] = [
  'Assam',
  'Arunachal Pradesh',
  'Manipur',
  'Meghalaya',
  'Mizoram',
  'Nagaland',
  'Sikkim',
  'Tripura'
];

export const PatientAuthScreen: React.FC<PatientAuthScreenProps> = ({
  mode,
  currentLanguage,
  highContrast,
  fontSizeScale,
  onSuccess,
  onBack,
  onSwitchMode
}) => {
  const [email, setEmail] = useState('');
  const [fullName, setFullName] = useState('');
  const [age, setAge] = useState<number>(68);
  const [locationState, setLocationState] = useState<NERState>('Assam');
  const [error, setError] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    const users = StorageService.getUsers();

    if (mode === 'login') {
      const found = users.find((u) => u.role === 'PATIENT' && (u.email.toLowerCase() === email.toLowerCase() || u.patientCode === email));
      if (found) {
        StorageService.setCurrentUser(found);
        onSuccess(found);
      } else {
        // Offer fallback to demo patient if user just clicks submit or types Arun
        const demo = users.find((u) => u.id === 'patient_arun') || users[0];
        if (demo) {
          StorageService.setCurrentUser(demo);
          onSuccess(demo);
        } else {
          setError('Patient record not found. Use Demo account or Sign up below.');
        }
      }
    } else {
      if (!fullName.trim()) {
        setError('Please enter your full name.');
        return;
      }
      const code = `NER-${Math.floor(1000 + Math.random() * 9000)}`;
      const newUser: UserEntity = {
        id: `patient_${Date.now()}`,
        email: email || `user_${Date.now()}@smriti.local`,
        fullName: fullName.trim(),
        role: 'PATIENT',
        age: Number(age) || 68,
        preferredLanguage: currentLanguage,
        locationState,
        patientCode: code,
        createdAt: Date.now()
      };
      StorageService.saveUser(newUser);
      StorageService.setCurrentUser(newUser);
      onSuccess(newUser);
    }
  };

  const cardStyle = highContrast
    ? 'bg-slate-900 border-2 border-yellow-400 text-white'
    : 'bg-white border border-slate-200 text-slate-900 shadow-sm';

  const inputStyle = highContrast
    ? 'bg-slate-800 border-2 border-yellow-400 text-yellow-300 placeholder-slate-400'
    : 'bg-slate-50 border border-slate-300 text-slate-900 focus:bg-white focus:border-teal-600';

  return (
    <div className={`min-h-[calc(100vh-60px)] px-4 py-8 flex flex-col items-center justify-center ${
      highContrast ? 'bg-slate-950 text-white' : 'bg-slate-50 text-slate-900'
    }`}>
      <div className={`w-full max-w-md p-6 sm:p-8 rounded-3xl ${cardStyle}`}>
        <button
          type="button"
          onClick={onBack}
          className="mb-4 inline-flex items-center gap-1.5 text-xs font-bold text-slate-500 hover:text-slate-900 transition-colors"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>Back to Home</span>
        </button>

        <div className="flex items-center gap-3 mb-6">
          <div className="w-12 h-12 rounded-2xl bg-teal-100 text-teal-800 flex items-center justify-center font-bold text-xl">
            <User className="w-6 h-6" />
          </div>
          <div>
            <h2 className="text-xl font-extrabold">
              {mode === 'login' ? 'Patient Sign In' : 'Create Patient Profile'}
            </h2>
            <p className="text-xs text-slate-500">
              {mode === 'login' ? 'Enter email or unique Patient Code' : 'Set up personalized cognitive games'}
            </p>
          </div>
        </div>

        {error && (
          <div className="p-3 mb-4 rounded-xl bg-rose-50 border border-rose-200 text-rose-800 text-xs font-semibold">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          {mode === 'signup' && (
            <>
              <div>
                <label className="block text-xs font-bold uppercase tracking-wider mb-1.5">
                  Full Name
                </label>
                <input
                  type="text"
                  required
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  placeholder="e.g. Arun Sharma"
                  className={`w-full p-3 rounded-xl text-sm font-medium outline-none ${inputStyle}`}
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider mb-1.5">
                    Age
                  </label>
                  <input
                    type="number"
                    min={18}
                    max={120}
                    value={age}
                    onChange={(e) => setAge(Number(e.target.value))}
                    className={`w-full p-3 rounded-xl text-sm font-medium outline-none ${inputStyle}`}
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold uppercase tracking-wider mb-1.5">
                    North East State
                  </label>
                  <select
                    value={locationState}
                    onChange={(e) => setLocationState(e.target.value as NERState)}
                    className={`w-full p-3 rounded-xl text-sm font-medium outline-none ${inputStyle}`}
                  >
                    {NER_STATES.map((s) => (
                      <option key={s} value={s}>
                        {s}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            </>
          )}

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider mb-1.5">
              {mode === 'login' ? 'Email or Patient Code' : 'Email (Optional)'}
            </label>
            <input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder={mode === 'login' ? 'arun.sharma@example.com or NER-6842' : 'name@example.com'}
              className={`w-full p-3 rounded-xl text-sm font-medium outline-none ${inputStyle}`}
            />
          </div>

          <button
            type="submit"
            className={`w-full py-3.5 rounded-xl font-extrabold text-sm sm:text-base mt-2 shadow transition-all ${
              highContrast
                ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                : 'bg-teal-700 text-white hover:bg-teal-800'
            }`}
          >
            {mode === 'login' ? 'Sign In as Patient' : 'Create Patient Profile'}
          </button>
        </form>

        <div className="mt-6 pt-4 border-t border-slate-200 text-center">
          <button
            type="button"
            onClick={onSwitchMode}
            className="text-xs font-semibold text-teal-700 dark:text-yellow-400 hover:underline"
          >
            {mode === 'login' ? "Don't have a profile yet? Sign up" : 'Already have a code? Sign In'}
          </button>
        </div>
      </div>
    </div>
  );
};
