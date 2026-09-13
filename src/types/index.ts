export type UserRole = 'PATIENT' | 'CAREGIVER';

export type NERState = 
  | 'Arunachal Pradesh'
  | 'Assam'
  | 'Manipur'
  | 'Meghalaya'
  | 'Mizoram'
  | 'Nagaland'
  | 'Sikkim'
  | 'Tripura';

export type AppLanguageCode = 'en' | 'hi' | 'as' | 'bn' | 'mni' | 'kha' | 'lus' | 'nag';

export interface AppLanguage {
  code: AppLanguageCode;
  displayName: string;
  nativeName: string;
}

export type GameCategory = 'all' | 'orientation' | 'memory' | 'attention' | 'reasoning';

export type ReminderCategory = 
  | 'MEDICATION'
  | 'HYDRATION'
  | 'APPOINTMENT'
  | 'DAILY_ROUTINE'
  | 'EXERCISE'
  | 'GAME_PRACTICE'
  | 'CUSTOM';

export type DayPeriod = 'MORNING' | 'AFTERNOON' | 'EVENING';

export type PerformanceTrend = 'IMPROVING' | 'STABLE' | 'NEEDS_ATTENTION';

export type FontSizeScale = 'STANDARD' | 'LARGE' | 'EXTRA_LARGE';

export interface UserEntity {
  id: string;
  email: string;
  fullName: string;
  role: UserRole;
  age?: number;
  gender?: string;
  preferredLanguage: AppLanguageCode;
  locationState: NERState;
  linkedPatientId?: string | null;
  patientCode: string;
  createdAt: number;
}

export interface GameAttemptEntity {
  id: string;
  userId: string;
  gameId: string;
  gameCategory: string;
  level: number;
  timestamp: number;
  totalQuestions: number;
  correctAnswers: number;
  incorrectAnswers: number;
  accuracyPercent: number;
  avgResponseTimeMs: number;
  totalScore: number;
  completionStatus: string;
  recommendationMessage: string;
}

export interface ReminderEntity {
  id: string;
  patientId: string;
  title: string;
  description: string;
  category: ReminderCategory;
  timeStr: string;
  dateStr: string;
  repeatFrequency: string;
  isActive: boolean;
  isCompleted: boolean;
  createdAt: number;
}

export interface RoutineItemEntity {
  id: string;
  patientId: string;
  period: DayPeriod;
  timeStr: string;
  title: string;
  description: string;
  isCompleted: boolean;
  orderIndex: number;
}

export interface SettingsEntity {
  userId: string;
  highContrast: boolean;
  fontSizeScale: FontSizeScale;
  largeButtons: boolean;
  isDarkMode: boolean;
  preferredLanguage: AppLanguageCode;
  voiceEnabled: boolean;
  speechRate: number;
}

export interface GameDefinition {
  id: string;
  titleKey: string;
  subtitleKey: string;
  category: GameCategory;
  iconEmoji: string;
  totalLevels: number;
}

export interface GameQuestion {
  id: string;
  promptKey: string;
  promptTextFallback: string;
  visualEmoji: string;
  options: string[];
  correctIndex: number;
  explanationKey?: string;
}

export interface MemoryCard {
  id: number;
  content: string;
  isFaceUp: boolean;
  isMatched: boolean;
}

export type PatientTab = 'home' | 'games' | 'progress' | 'profile';
export type CaregiverTab = 'dashboard' | 'progress' | 'reminders' | 'routine' | 'settings';

export type ScreenState = 
  | { type: 'welcome' }
  | { type: 'patient_login' }
  | { type: 'patient_signup' }
  | { type: 'caregiver_login' }
  | { type: 'caregiver_signup' }
  | { type: 'patient_main'; tab?: PatientTab }
  | { type: 'caregiver_main'; tab?: CaregiverTab }
  | { type: 'game_play'; gameId: string; level: number }
  | { 
      type: 'game_result'; 
      gameId: string; 
      level: number; 
      score: number; 
      accuracy: number; 
      avgTimeMs: number; 
      correct: number; 
      total: number; 
      recommendation: string; 
    };
