import React, { useState } from 'react';
import { ArrowLeft, ShieldCheck, HeartHandshake, Link as LinkIcon } from 'lucide-react';
import { AppLanguageCode, NERState, UserEntity, FontSizeScale } from '../types';
import { StorageService } from '../data/storage';

interface CaregiverAuthScreenProps {
  mode: 'login' | 'signup';
  currentLanguage: AppLanguageCode;
  highContrast: boolean;
  fontSizeScale: FontSizeScale;
  onSuccess: (user: UserEntity) => void;
  onBack: () => void;
  onSwitchMode: () => void;
}

export const CaregiverAuthScreen: React.FC<CaregiverAuthScreenProps> = ({
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
  const [patientCode, setPatientCode] = useState('NER-6842');
  const [error, setError] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    const users = StorageService.getUsers();

    if (mode === 'login') {
      const found = users.find((u) => u.role === 'CAREGIVER' && u.email.toLowerCase() === email.toLowerCase());
      if (found) {
        StorageService.setCurrentUser(found);
        onSuccess(found);
      } else {
        // Fallback to demo caregiver if user clicks without typing or uses demo
        const demo = users.find((u) => u.id === 'caregiver_priya') || users.find((u) => u.role === 'CAREGIVER');
        if (demo) {
          StorageService.setCurrentUser(demo);
          onSuccess(demo);
        } else {
          setError('Caregiver account not found. Try Demo account or Sign Up.');
        }
      }
    } else {
      if (!fullName.trim()) {
        setError('Please enter your full name.');
        return;
      }
      // Look up linked patient by code
      const linkedPatient = users.find((u) => u.patientCode.trim().toUpperCase() === patientCode.trim().toUpperCase());
      const newCaregiver: UserEntity = {
        id: `caregiver_${Date.now()}`,
        email: email || `caregiver_${Date.now()}@smriti.local`,
        fullName: fullName.trim(),
        role: 'CAREGIVER',
        preferredLanguage: currentLanguage,
        locationState: 'Assam',
        linkedPatientId: linkedPatient ? linkedPatient.id : 'patient_arun',
        patientCode: `CRG-${Math.floor(1000 + Math.random() * 9000)}`,
        createdAt: Date.now()
      };
      StorageService.saveUser(newCaregiver);
      StorageService.setCurrentUser(newCaregiver);
      onSuccess(newCaregiver);
    }
  };

  const cardStyle = highContrast
    ? 'bg-slate-900 border-2 border-yellow-400 text-white'
    : 'bg-white border border-slate-200 text-slate-900 shadow-sm';

  const inputStyle = highContrast
    ? 'bg-slate-800 border-2 border-yellow-400 text-yellow-300 placeholder-slate-400'
    : 'bg-slate-50 border border-slate-300 text-slate-900 focus:bg-white focus:border-indigo-600';

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
          <div className="w-12 h-12 rounded-2xl bg-indigo-100 text-indigo-800 flex items-center justify-center font-bold text-xl">
            <ShieldCheck className="w-6 h-6" />
          </div>
          <div>
            <h2 className="text-xl font-extrabold">
              {mode === 'login' ? 'Caregiver Sign In' : 'Register Caregiver Account'}
            </h2>
            <p className="text-xs text-slate-500">
              Monitor patient trends, routines, and reminders
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
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider mb-1.5">
                Full Name
              </label>
              <input
                type="text"
                required
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                placeholder="e.g. Priya Sharma"
                className={`w-full p-3 rounded-xl text-sm font-medium outline-none ${inputStyle}`}
              />
            </div>
          )}

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider mb-1.5">
              Caregiver Email
            </label>
            <input
              type="text"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="priya.sharma@example.com"
              className={`w-full p-3 rounded-xl text-sm font-medium outline-none ${inputStyle}`}
            />
          </div>

          {mode === 'signup' && (
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider mb-1.5 flex items-center gap-1">
                <LinkIcon className="w-3.5 h-3.5 text-indigo-600" />
                <span>Link Patient Code</span>
              </label>
              <input
                type="text"
                value={patientCode}
                onChange={(e) => setPatientCode(e.target.value)}
                placeholder="e.g. NER-6842"
                className={`w-full p-3 rounded-xl text-sm font-medium outline-none ${inputStyle}`}
              />
              <span className="text-[11px] text-slate-400 mt-1 block">
                Default demo patient code is <strong>NER-6842</strong> (Arun Sharma)
              </span>
            </div>
          )}

          <button
            type="submit"
            className={`w-full py-3.5 rounded-xl font-extrabold text-sm sm:text-base mt-2 shadow transition-all ${
              highContrast
                ? 'bg-yellow-400 text-slate-950 hover:bg-yellow-300'
                : 'bg-indigo-700 text-white hover:bg-indigo-800'
            }`}
          >
            {mode === 'login' ? 'Sign In as Caregiver' : 'Create Caregiver Account'}
          </button>
        </form>

        <div className="mt-6 pt-4 border-t border-slate-200 text-center">
          <button
            type="button"
            onClick={onSwitchMode}
            className="text-xs font-semibold text-indigo-700 dark:text-yellow-400 hover:underline"
          >
            {mode === 'login' ? 'Need a caregiver account? Sign up' : 'Already registered? Sign In'}
          </button>
        </div>
      </div>
    </div>
  );
};
