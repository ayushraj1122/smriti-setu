import React, { useState, useEffect } from 'react';
import {
  UserEntity,
  GameAttemptEntity,
  ReminderEntity,
  RoutineItemEntity,
  ScreenState,
  PatientTab,
  CaregiverTab,
  AppLanguageCode,
  FontSizeScale,
  SettingsEntity
} from './types';
import {
  StorageService,
  DEMO_PATIENT,
  DEMO_CAREGIVER,
  DEFAULT_SETTINGS
} from './data/storage';
import { TextToSpeechService } from './engine/tts';
import { AccessibilityHeader } from './components/AccessibilityHeader';
import { WelcomeScreen } from './screens/WelcomeScreen';
import { PatientAuthScreen } from './screens/PatientAuthScreen';
import { CaregiverAuthScreen } from './screens/CaregiverAuthScreen';
import { PatientMainScreen } from './screens/PatientMainScreen';
import { CaregiverMainScreen } from './screens/CaregiverMainScreen';
import { GamePlayScreen } from './screens/GamePlayScreen';
import { GameResultScreen } from './screens/GameResultScreen';

export function App() {
  // Initialize storage once on mount
  useEffect(() => {
    StorageService.initialize();
  }, []);

  // Settings state
  const [settings, setSettings] = useState<SettingsEntity>(() => StorageService.getSettings());
  const [currentLanguage, setCurrentLanguage] = useState<AppLanguageCode>(settings.preferredLanguage);
  const [highContrast, setHighContrast] = useState<boolean>(settings.highContrast);
  const [fontSizeScale, setFontSizeScale] = useState<FontSizeScale>(settings.fontSizeScale);
  const [largeButtons, setLargeButtons] = useState<boolean>(settings.largeButtons);
  const [voiceEnabled, setVoiceEnabled] = useState<boolean>(settings.voiceEnabled);

  // App data state
  const [currentUser, setCurrentUser] = useState<UserEntity | null>(() => StorageService.getCurrentUser());
  const [allUsers, setAllUsers] = useState<UserEntity[]>(() => StorageService.getUsers());
  const [attempts, setAttempts] = useState<GameAttemptEntity[]>(() => StorageService.getAttempts());
  const [routines, setRoutines] = useState<RoutineItemEntity[]>(() => StorageService.getRoutines());
  const [reminders, setReminders] = useState<ReminderEntity[]>(() => StorageService.getReminders());

  // Screen routing state
  const [screen, setScreen] = useState<ScreenState>(() => {
    const user = StorageService.getCurrentUser();
    if (user) {
      return user.role === 'PATIENT' ? { type: 'patient_main', tab: 'home' } : { type: 'caregiver_main', tab: 'dashboard' };
    }
    return { type: 'welcome' };
  });

  // Keep settings updated in storage
  const updateSettings = (partial: Partial<SettingsEntity>) => {
    const updated = { ...settings, ...partial };
    setSettings(updated);
    StorageService.saveSettings(updated);
  };

  const handleToggleHighContrast = () => {
    const val = !highContrast;
    setHighContrast(val);
    updateSettings({ highContrast: val });
  };

  const handleCycleFontSize = () => {
    const next: FontSizeScale =
      fontSizeScale === 'STANDARD' ? 'LARGE' : fontSizeScale === 'LARGE' ? 'EXTRA_LARGE' : 'STANDARD';
    setFontSizeScale(next);
    updateSettings({ fontSizeScale: next });
  };

  const handleToggleVoice = () => {
    const val = !voiceEnabled;
    setVoiceEnabled(val);
    updateSettings({ voiceEnabled: val });
    if (!val) {
      TextToSpeechService.stop();
    }
  };

  const handleToggleLargeButtons = () => {
    const val = !largeButtons;
    setLargeButtons(val);
    updateSettings({ largeButtons: val });
  };

  const handleLanguageSelected = (lang: AppLanguageCode) => {
    setCurrentLanguage(lang);
    updateSettings({ preferredLanguage: lang });
  };

  const handleSpeak = (text: string) => {
    if (voiceEnabled) {
      TextToSpeechService.speak(text, currentLanguage);
    }
  };

  const handleSpeakCurrentContext = () => {
    if (screen.type === 'welcome') {
      handleSpeak('Welcome to Smriti Setu. Multilingual cognitive games and caregiver support platform.');
    } else if (screen.type === 'patient_main') {
      handleSpeak(`Patient Portal for ${currentUser?.fullName || 'User'}. Choose from Home, Games, Progress, or Profile tabs.`);
    } else if (screen.type === 'caregiver_main') {
      handleSpeak('Caregiver Dashboard. Monitor cognitive performance trends, daily routines, and medication reminders.');
    } else if (screen.type === 'game_play') {
      handleSpeak(`Playing ${screen.gameId} level ${screen.level}. Read each option and tap the best answer.`);
    }
  };

  // Auth actions
  const handleLoginSuccess = (user: UserEntity) => {
    setCurrentUser(user);
    setAllUsers(StorageService.getUsers());
    if (user.role === 'PATIENT') {
      setScreen({ type: 'patient_main', tab: 'home' });
    } else {
      setScreen({ type: 'caregiver_main', tab: 'dashboard' });
    }
  };

  const handleLogout = () => {
    StorageService.setCurrentUser(null);
    setCurrentUser(null);
    setScreen({ type: 'welcome' });
  };

  // Quick Demo shortcuts
  const handleQuickDemoPatient = () => {
    const patient = DEMO_PATIENT;
    StorageService.setCurrentUser(patient);
    setCurrentUser(patient);
    setScreen({ type: 'patient_main', tab: 'home' });
  };

  const handleQuickDemoCaregiver = () => {
    const caregiver = DEMO_CAREGIVER;
    StorageService.setCurrentUser(caregiver);
    setCurrentUser(caregiver);
    setScreen({ type: 'caregiver_main', tab: 'dashboard' });
  };

  // Routine & Reminder actions
  const handleToggleRoutine = (id: string, isCompleted: boolean) => {
    StorageService.toggleRoutine(id, isCompleted);
    setRoutines(StorageService.getRoutines());
  };

  const handleAddRoutine = (routine: RoutineItemEntity) => {
    StorageService.addRoutine(routine);
    setRoutines(StorageService.getRoutines());
  };

  const handleDeleteRoutine = (id: string) => {
    StorageService.deleteRoutine(id);
    setRoutines(StorageService.getRoutines());
  };

  const handleToggleReminder = (id: string, isCompleted: boolean) => {
    StorageService.toggleReminder(id, isCompleted);
    setReminders(StorageService.getReminders());
  };

  const handleAddReminder = (reminder: ReminderEntity) => {
    StorageService.addReminder(reminder);
    setReminders(StorageService.getReminders());
  };

  const handleDeleteReminder = (id: string) => {
    StorageService.deleteReminder(id);
    setReminders(StorageService.getReminders());
  };

  const handleLinkPatientByCode = (code: string) => {
    const users = StorageService.getUsers();
    const found = users.find((u) => u.patientCode.trim().toUpperCase() === code.trim().toUpperCase());
    if (found && currentUser) {
      const updatedCaregiver: UserEntity = {
        ...currentUser,
        linkedPatientId: found.id
      };
      StorageService.saveUser(updatedCaregiver);
      StorageService.setCurrentUser(updatedCaregiver);
      setCurrentUser(updatedCaregiver);
      return { success: true, message: `Successfully linked with ${found.fullName} (${found.patientCode})!` };
    }
    return { success: false, message: `No patient found with code "${code}". Try NER-6842.` };
  };

  // Active linked patient for caregiver
  const linkedPatient: UserEntity = (() => {
    if (currentUser && currentUser.role === 'CAREGIVER' && currentUser.linkedPatientId) {
      const found = allUsers.find((u) => u.id === currentUser.linkedPatientId);
      if (found) return found;
    }
    const anyPatient = allUsers.find((u) => u.role === 'PATIENT');
    return anyPatient || DEMO_PATIENT;
  })();

  // Game completion handler
  const handleFinishGame = (result: {
    score: number;
    accuracy: number;
    avgTimeMs: number;
    correct: number;
    total: number;
    recommendation: string;
  }) => {
    if (screen.type !== 'game_play') return;
    const currentGameId = screen.gameId;
    const currentLevel = screen.level;

    const newAttempt: GameAttemptEntity = {
      id: `att_${Date.now()}`,
      userId: currentUser?.id || DEMO_PATIENT.id,
      gameId: currentGameId,
      gameCategory:
        currentGameId === 'day_date' || currentGameId === 'place_time'
          ? 'orientation'
          : currentGameId === 'memory_match' || currentGameId === 'remember_objects'
          ? 'memory'
          : currentGameId === 'find_target' || currentGameId === 'sequence_attention'
          ? 'attention'
          : 'reasoning',
      level: currentLevel,
      timestamp: Date.now(),
      totalQuestions: result.total,
      correctAnswers: result.correct,
      incorrectAnswers: result.total - result.correct,
      accuracyPercent: result.accuracy,
      avgResponseTimeMs: result.avgTimeMs,
      totalScore: result.score,
      completionStatus: 'COMPLETED',
      recommendationMessage: result.recommendation
    };

    StorageService.saveAttempt(newAttempt);
    setAttempts(StorageService.getAttempts());

    setScreen({
      type: 'game_result',
      gameId: currentGameId,
      level: currentLevel,
      score: result.score,
      accuracy: result.accuracy,
      avgTimeMs: result.avgTimeMs,
      correct: result.correct,
      total: result.total,
      recommendation: result.recommendation
    });
  };

  return (
    <div className={`min-h-screen ${highContrast ? 'high-contrast bg-slate-950 text-white' : 'bg-slate-50 text-slate-900'}`}>
      {/* Top Accessibility Header */}
      <AccessibilityHeader
        currentLanguage={currentLanguage}
        onLanguageSelected={handleLanguageSelected}
        voiceEnabled={voiceEnabled}
        onToggleVoice={handleToggleVoice}
        highContrast={highContrast}
        onToggleHighContrast={handleToggleHighContrast}
        fontSizeScale={fontSizeScale}
        onCycleFontSize={handleCycleFontSize}
        onSpeakCurrentContext={handleSpeakCurrentContext}
      />

      {/* Main View Router */}
      <main>
        {screen.type === 'welcome' && (
          <WelcomeScreen
            currentLanguage={currentLanguage}
            highContrast={highContrast}
            fontSizeScale={fontSizeScale}
            onPlayGames={() => {
              // Set demo patient as guest if playing immediately
              if (!currentUser) {
                StorageService.setCurrentUser(DEMO_PATIENT);
                setCurrentUser(DEMO_PATIENT);
              }
              setScreen({ type: 'patient_main', tab: 'games' });
            }}
            onPatientLogin={() => setScreen({ type: 'patient_login' })}
            onCaregiverLogin={() => setScreen({ type: 'caregiver_login' })}
            onQuickDemoPatient={handleQuickDemoPatient}
            onQuickDemoCaregiver={handleQuickDemoCaregiver}
            onSpeak={handleSpeak}
          />
        )}

        {screen.type === 'patient_login' && (
          <PatientAuthScreen
            mode="login"
            currentLanguage={currentLanguage}
            highContrast={highContrast}
            fontSizeScale={fontSizeScale}
            onSuccess={handleLoginSuccess}
            onBack={() => setScreen({ type: 'welcome' })}
            onSwitchMode={() => setScreen({ type: 'patient_signup' })}
          />
        )}

        {screen.type === 'patient_signup' && (
          <PatientAuthScreen
            mode="signup"
            currentLanguage={currentLanguage}
            highContrast={highContrast}
            fontSizeScale={fontSizeScale}
            onSuccess={handleLoginSuccess}
            onBack={() => setScreen({ type: 'welcome' })}
            onSwitchMode={() => setScreen({ type: 'patient_login' })}
          />
        )}

        {screen.type === 'caregiver_login' && (
          <CaregiverAuthScreen
            mode="login"
            currentLanguage={currentLanguage}
            highContrast={highContrast}
            fontSizeScale={fontSizeScale}
            onSuccess={handleLoginSuccess}
            onBack={() => setScreen({ type: 'welcome' })}
            onSwitchMode={() => setScreen({ type: 'caregiver_signup' })}
          />
        )}

        {screen.type === 'caregiver_signup' && (
          <CaregiverAuthScreen
            mode="signup"
            currentLanguage={currentLanguage}
            highContrast={highContrast}
            fontSizeScale={fontSizeScale}
            onSuccess={handleLoginSuccess}
            onBack={() => setScreen({ type: 'welcome' })}
            onSwitchMode={() => setScreen({ type: 'caregiver_login' })}
          />
        )}

        {screen.type === 'patient_main' && (
          <PatientMainScreen
            currentTab={screen.tab || 'home'}
            onSelectTab={(tab: PatientTab) => setScreen({ type: 'patient_main', tab })}
            user={currentUser || DEMO_PATIENT}
            attempts={attempts}
            routines={routines}
            onToggleRoutine={handleToggleRoutine}
            currentLanguage={currentLanguage}
            onLanguageSelected={handleLanguageSelected}
            highContrast={highContrast}
            onToggleHighContrast={handleToggleHighContrast}
            fontSizeScale={fontSizeScale}
            onCycleFontSize={handleCycleFontSize}
            largeButtons={largeButtons}
            onToggleLargeButtons={handleToggleLargeButtons}
            voiceEnabled={voiceEnabled}
            onToggleVoice={handleToggleVoice}
            onSpeak={handleSpeak}
            onLaunchGame={(gameId, level) => setScreen({ type: 'game_play', gameId, level })}
            onLogout={handleLogout}
          />
        )}

        {screen.type === 'caregiver_main' && (
          <CaregiverMainScreen
            currentTab={screen.tab || 'dashboard'}
            onSelectTab={(tab: CaregiverTab) => setScreen({ type: 'caregiver_main', tab })}
            caregiverUser={currentUser || DEMO_CAREGIVER}
            linkedPatient={linkedPatient}
            allPatients={allUsers.filter((u) => u.role === 'PATIENT')}
            onSelectPatient={(patient) => {
              if (currentUser) {
                const updated = { ...currentUser, linkedPatientId: patient.id };
                StorageService.saveUser(updated);
                StorageService.setCurrentUser(updated);
                setCurrentUser(updated);
              }
            }}
            attempts={attempts}
            reminders={reminders}
            onToggleReminder={handleToggleReminder}
            onAddReminder={handleAddReminder}
            onDeleteReminder={handleDeleteReminder}
            routines={routines}
            onToggleRoutine={handleToggleRoutine}
            onAddRoutine={handleAddRoutine}
            onDeleteRoutine={handleDeleteRoutine}
            onLinkPatientByCode={handleLinkPatientByCode}
            currentLanguage={currentLanguage}
            onLanguageSelected={handleLanguageSelected}
            highContrast={highContrast}
            onToggleHighContrast={handleToggleHighContrast}
            fontSizeScale={fontSizeScale}
            onCycleFontSize={handleCycleFontSize}
            voiceEnabled={voiceEnabled}
            onToggleVoice={handleToggleVoice}
            onSpeak={handleSpeak}
            onLogout={handleLogout}
          />
        )}

        {screen.type === 'game_play' && (
          <GamePlayScreen
            gameId={screen.gameId}
            level={screen.level}
            currentLanguage={currentLanguage}
            highContrast={highContrast}
            fontSizeScale={fontSizeScale}
            largeButtons={largeButtons}
            voiceEnabled={voiceEnabled}
            onFinishGame={handleFinishGame}
            onQuit={() => setScreen({ type: 'patient_main', tab: 'games' })}
          />
        )}

        {screen.type === 'game_result' && (
          <GameResultScreen
            gameId={screen.gameId}
            level={screen.level}
            score={screen.score}
            accuracy={screen.accuracy}
            avgTimeMs={screen.avgTimeMs}
            correct={screen.correct}
            total={screen.total}
            recommendation={screen.recommendation}
            currentLanguage={currentLanguage}
            highContrast={highContrast}
            fontSizeScale={fontSizeScale}
            voiceEnabled={voiceEnabled}
            onReplay={() => setScreen({ type: 'game_play', gameId: screen.gameId, level: screen.level })}
            onBackToGames={() => setScreen({ type: 'patient_main', tab: 'games' })}
            onGoHome={() => setScreen({ type: 'patient_main', tab: 'home' })}
            onSpeak={handleSpeak}
          />
        )}
      </main>
    </div>
  );
}

export default App;
