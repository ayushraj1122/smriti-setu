import {
  UserEntity,
  GameAttemptEntity,
  ReminderEntity,
  RoutineItemEntity,
  SettingsEntity,
  AppLanguageCode,
  FontSizeScale
} from '../types';

const STORAGE_KEYS = {
  USERS: 'smriti_users',
  ATTEMPTS: 'smriti_attempts',
  REMINDERS: 'smriti_reminders',
  ROUTINES: 'smriti_routines',
  SETTINGS: 'smriti_settings',
  CURRENT_USER: 'smriti_current_user'
};

export const DEMO_PATIENT: UserEntity = {
  id: 'patient_arun',
  email: 'arun.sharma@example.com',
  fullName: 'Arun Sharma',
  role: 'PATIENT',
  age: 68,
  gender: 'Male',
  preferredLanguage: 'as',
  locationState: 'Assam',
  patientCode: 'NER-6842',
  createdAt: Date.now() - 30 * 86400000
};

export const DEMO_CAREGIVER: UserEntity = {
  id: 'caregiver_priya',
  email: 'priya.sharma@example.com',
  fullName: 'Priya Sharma',
  role: 'CAREGIVER',
  age: 36,
  gender: 'Female',
  preferredLanguage: 'en',
  locationState: 'Assam',
  linkedPatientId: 'patient_arun',
  patientCode: 'CRG-1102',
  createdAt: Date.now() - 30 * 86400000
};

const INITIAL_ATTEMPTS: GameAttemptEntity[] = [
  {
    id: 'att_1',
    userId: 'patient_arun',
    gameId: 'day_date',
    gameCategory: 'orientation',
    level: 1,
    timestamp: Date.now() - 6 * 86400000,
    totalQuestions: 3,
    correctAnswers: 3,
    incorrectAnswers: 0,
    accuracyPercent: 100,
    avgResponseTimeMs: 4200,
    totalScore: 360,
    completionStatus: 'COMPLETED',
    recommendationMessage: 'Excellent accuracy! You are ready to try the next level.'
  },
  {
    id: 'att_2',
    userId: 'patient_arun',
    gameId: 'place_time',
    gameCategory: 'orientation',
    level: 1,
    timestamp: Date.now() - 5 * 86400000,
    totalQuestions: 3,
    correctAnswers: 2,
    incorrectAnswers: 1,
    accuracyPercent: 67,
    avgResponseTimeMs: 5100,
    totalScore: 240,
    completionStatus: 'COMPLETED',
    recommendationMessage: 'Good effort! Continuing at this level will help build comfort.'
  },
  {
    id: 'att_3',
    userId: 'patient_arun',
    gameId: 'memory_match',
    gameCategory: 'memory',
    level: 1,
    timestamp: Date.now() - 4 * 86400000,
    totalQuestions: 3,
    correctAnswers: 3,
    incorrectAnswers: 0,
    accuracyPercent: 100,
    avgResponseTimeMs: 3800,
    totalScore: 375,
    completionStatus: 'COMPLETED',
    recommendationMessage: 'Excellent accuracy! You are ready to try the next level.'
  },
  {
    id: 'att_4',
    userId: 'patient_arun',
    gameId: 'find_target',
    gameCategory: 'attention',
    level: 1,
    timestamp: Date.now() - 3 * 86400000,
    totalQuestions: 3,
    correctAnswers: 3,
    incorrectAnswers: 0,
    accuracyPercent: 100,
    avgResponseTimeMs: 2900,
    totalScore: 390,
    completionStatus: 'COMPLETED',
    recommendationMessage: 'Outstanding quick focus! Try level 2.'
  },
  {
    id: 'att_5',
    userId: 'patient_arun',
    gameId: 'sequence_attention',
    gameCategory: 'attention',
    level: 2,
    timestamp: Date.now() - 2 * 86400000,
    totalQuestions: 4,
    correctAnswers: 3,
    incorrectAnswers: 1,
    accuracyPercent: 75,
    avgResponseTimeMs: 4400,
    totalScore: 420,
    completionStatus: 'COMPLETED',
    recommendationMessage: 'Very steady progress on sequence patterns.'
  },
  {
    id: 'att_6',
    userId: 'patient_arun',
    gameId: 'everyday_choice',
    gameCategory: 'reasoning',
    level: 1,
    timestamp: Date.now() - 1 * 86400000,
    totalQuestions: 3,
    correctAnswers: 3,
    incorrectAnswers: 0,
    accuracyPercent: 100,
    avgResponseTimeMs: 3100,
    totalScore: 380,
    completionStatus: 'COMPLETED',
    recommendationMessage: 'Clear and sound everyday decisions.'
  },
  {
    id: 'att_7',
    userId: 'patient_arun',
    gameId: 'day_date',
    gameCategory: 'orientation',
    level: 2,
    timestamp: Date.now() - 3600000 * 5,
    totalQuestions: 4,
    correctAnswers: 4,
    incorrectAnswers: 0,
    accuracyPercent: 100,
    avgResponseTimeMs: 3400,
    totalScore: 520,
    completionStatus: 'COMPLETED',
    recommendationMessage: 'Fantastic performance on level 2 day and date orientation!'
  }
];

const INITIAL_ROUTINES: RoutineItemEntity[] = [
  {
    id: 'rt_1',
    patientId: 'patient_arun',
    period: 'MORNING',
    timeStr: '07:30 AM',
    title: 'Morning Walk in Garden',
    description: '15 minutes of calm fresh air and light stretching.',
    isCompleted: true,
    orderIndex: 1
  },
  {
    id: 'rt_2',
    patientId: 'patient_arun',
    period: 'MORNING',
    timeStr: '08:30 AM',
    title: 'Warm Assam Tea & Breakfast',
    description: 'Poha with steamed vegetables and warm tea.',
    isCompleted: true,
    orderIndex: 2
  },
  {
    id: 'rt_3',
    patientId: 'patient_arun',
    period: 'MORNING',
    timeStr: '09:30 AM',
    title: 'Smriti Setu Brain Games',
    description: 'Play 2 orientation or memory games comfortably.',
    isCompleted: true,
    orderIndex: 3
  },
  {
    id: 'rt_4',
    patientId: 'patient_arun',
    period: 'AFTERNOON',
    timeStr: '01:00 PM',
    title: 'Healthy Lunch & Hydration',
    description: 'Rice, lentils, seasonal greens, and a glass of fresh water.',
    isCompleted: false,
    orderIndex: 4
  },
  {
    id: 'rt_5',
    patientId: 'patient_arun',
    period: 'AFTERNOON',
    timeStr: '02:30 PM',
    title: 'Quiet Rest / Nap',
    description: 'Restful recharge in a quiet, comfortable room.',
    isCompleted: false,
    orderIndex: 5
  },
  {
    id: 'rt_6',
    patientId: 'patient_arun',
    period: 'EVENING',
    timeStr: '05:30 PM',
    title: 'Family Call & Nostalgia Photos',
    description: 'Chat with grandchildren or look through family album.',
    isCompleted: false,
    orderIndex: 6
  },
  {
    id: 'rt_7',
    patientId: 'patient_arun',
    period: 'EVENING',
    timeStr: '08:30 PM',
    title: 'Night Medicine & Relaxing Music',
    description: 'Take nighttime drops with water and wind down peacefully.',
    isCompleted: false,
    orderIndex: 7
  }
];

const INITIAL_REMINDERS: ReminderEntity[] = [
  {
    id: 'rem_1',
    patientId: 'patient_arun',
    title: 'Blood Pressure Tablet',
    description: 'Take 1 tablet after morning breakfast with water.',
    category: 'MEDICATION',
    timeStr: '09:00 AM',
    dateStr: 'Daily',
    repeatFrequency: 'Daily',
    isActive: true,
    isCompleted: true,
    createdAt: Date.now() - 7 * 86400000
  },
  {
    id: 'rem_2',
    patientId: 'patient_arun',
    title: 'Midday Water Hydration',
    description: 'Drink a full tall glass of clean water.',
    category: 'HYDRATION',
    timeStr: '11:30 AM',
    dateStr: 'Daily',
    repeatFrequency: 'Daily',
    isActive: true,
    isCompleted: false,
    createdAt: Date.now() - 7 * 86400000
  },
  {
    id: 'rem_3',
    patientId: 'patient_arun',
    title: 'Evening Memory Practice',
    description: 'Spend 10 minutes on Smriti Setu cards.',
    category: 'GAME_PRACTICE',
    timeStr: '06:00 PM',
    dateStr: 'Daily',
    repeatFrequency: 'Daily',
    isActive: true,
    isCompleted: false,
    createdAt: Date.now() - 7 * 86400000
  },
  {
    id: 'rem_4',
    patientId: 'patient_arun',
    title: 'Dr. Barua Checkup Consultation',
    description: 'Routine wellness check at Guwahati Health Centre.',
    category: 'APPOINTMENT',
    timeStr: '11:00 AM',
    dateStr: 'Saturday',
    repeatFrequency: 'Once',
    isActive: true,
    isCompleted: false,
    createdAt: Date.now() - 3 * 86400000
  }
];

export const DEFAULT_SETTINGS: SettingsEntity = {
  userId: 'patient_arun',
  highContrast: false,
  fontSizeScale: 'STANDARD',
  largeButtons: true,
  isDarkMode: false,
  preferredLanguage: 'en',
  voiceEnabled: true,
  speechRate: 0.88
};

export class StorageService {
  private static isBrowser = typeof window !== 'undefined';

  public static initialize(): void {
    if (!this.isBrowser) return;

    if (!localStorage.getItem(STORAGE_KEYS.USERS)) {
      localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify([DEMO_PATIENT, DEMO_CAREGIVER]));
    }
    if (!localStorage.getItem(STORAGE_KEYS.ATTEMPTS)) {
      localStorage.setItem(STORAGE_KEYS.ATTEMPTS, JSON.stringify(INITIAL_ATTEMPTS));
    }
    if (!localStorage.getItem(STORAGE_KEYS.ROUTINES)) {
      localStorage.setItem(STORAGE_KEYS.ROUTINES, JSON.stringify(INITIAL_ROUTINES));
    }
    if (!localStorage.getItem(STORAGE_KEYS.REMINDERS)) {
      localStorage.setItem(STORAGE_KEYS.REMINDERS, JSON.stringify(INITIAL_REMINDERS));
    }
    if (!localStorage.getItem(STORAGE_KEYS.SETTINGS)) {
      localStorage.setItem(STORAGE_KEYS.SETTINGS, JSON.stringify(DEFAULT_SETTINGS));
    }
  }

  public static getUsers(): UserEntity[] {
    if (!this.isBrowser) return [DEMO_PATIENT, DEMO_CAREGIVER];
    const data = localStorage.getItem(STORAGE_KEYS.USERS);
    return data ? JSON.parse(data) : [DEMO_PATIENT, DEMO_CAREGIVER];
  }

  public static saveUser(user: UserEntity): void {
    if (!this.isBrowser) return;
    const users = this.getUsers().filter((u) => u.id !== user.id);
    users.push(user);
    localStorage.setItem(STORAGE_KEYS.USERS, JSON.stringify(users));
  }

  public static getCurrentUser(): UserEntity | null {
    if (!this.isBrowser) return null;
    const data = localStorage.getItem(STORAGE_KEYS.CURRENT_USER);
    return data ? JSON.parse(data) : null;
  }

  public static setCurrentUser(user: UserEntity | null): void {
    if (!this.isBrowser) return;
    if (user) {
      localStorage.setItem(STORAGE_KEYS.CURRENT_USER, JSON.stringify(user));
    } else {
      localStorage.removeItem(STORAGE_KEYS.CURRENT_USER);
    }
  }

  public static getAttempts(userId?: string): GameAttemptEntity[] {
    if (!this.isBrowser) return INITIAL_ATTEMPTS;
    const data = localStorage.getItem(STORAGE_KEYS.ATTEMPTS);
    const list: GameAttemptEntity[] = data ? JSON.parse(data) : INITIAL_ATTEMPTS;
    if (userId) {
      return list.filter((a) => a.userId === userId);
    }
    return list;
  }

  public static saveAttempt(attempt: GameAttemptEntity): void {
    if (!this.isBrowser) return;
    const attempts = this.getAttempts();
    attempts.push(attempt);
    localStorage.setItem(STORAGE_KEYS.ATTEMPTS, JSON.stringify(attempts));
  }

  public static getRoutines(patientId?: string): RoutineItemEntity[] {
    if (!this.isBrowser) return INITIAL_ROUTINES;
    const data = localStorage.getItem(STORAGE_KEYS.ROUTINES);
    const list: RoutineItemEntity[] = data ? JSON.parse(data) : INITIAL_ROUTINES;
    if (patientId) {
      return list.filter((r) => r.patientId === patientId);
    }
    return list;
  }

  public static toggleRoutine(routineId: string, isCompleted: boolean): void {
    if (!this.isBrowser) return;
    const routines = this.getRoutines().map((r) =>
      r.id === routineId ? { ...r, isCompleted } : r
    );
    localStorage.setItem(STORAGE_KEYS.ROUTINES, JSON.stringify(routines));
  }

  public static addRoutine(item: RoutineItemEntity): void {
    if (!this.isBrowser) return;
    const routines = this.getRoutines();
    routines.push(item);
    localStorage.setItem(STORAGE_KEYS.ROUTINES, JSON.stringify(routines));
  }

  public static deleteRoutine(routineId: string): void {
    if (!this.isBrowser) return;
    const routines = this.getRoutines().filter((r) => r.id !== routineId);
    localStorage.setItem(STORAGE_KEYS.ROUTINES, JSON.stringify(routines));
  }

  public static getReminders(patientId?: string): ReminderEntity[] {
    if (!this.isBrowser) return INITIAL_REMINDERS;
    const data = localStorage.getItem(STORAGE_KEYS.REMINDERS);
    const list: ReminderEntity[] = data ? JSON.parse(data) : INITIAL_REMINDERS;
    if (patientId) {
      return list.filter((r) => r.patientId === patientId);
    }
    return list;
  }

  public static toggleReminder(reminderId: string, isCompleted: boolean): void {
    if (!this.isBrowser) return;
    const reminders = this.getReminders().map((r) =>
      r.id === reminderId ? { ...r, isCompleted } : r
    );
    localStorage.setItem(STORAGE_KEYS.REMINDERS, JSON.stringify(reminders));
  }

  public static addReminder(reminder: ReminderEntity): void {
    if (!this.isBrowser) return;
    const reminders = this.getReminders();
    reminders.push(reminder);
    localStorage.setItem(STORAGE_KEYS.REMINDERS, JSON.stringify(reminders));
  }

  public static deleteReminder(reminderId: string): void {
    if (!this.isBrowser) return;
    const reminders = this.getReminders().filter((r) => r.id !== reminderId);
    localStorage.setItem(STORAGE_KEYS.REMINDERS, JSON.stringify(reminders));
  }

  public static getSettings(): SettingsEntity {
    if (!this.isBrowser) return DEFAULT_SETTINGS;
    const data = localStorage.getItem(STORAGE_KEYS.SETTINGS);
    return data ? JSON.parse(data) : DEFAULT_SETTINGS;
  }

  public static saveSettings(settings: SettingsEntity): void {
    if (!this.isBrowser) return;
    localStorage.setItem(STORAGE_KEYS.SETTINGS, JSON.stringify(settings));
  }
}
