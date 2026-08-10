import type { Theme } from '../enums/theme_enum';

export type UserPreferences = {
  id: string;
  theme: Theme;
  onboardingCompleted: boolean;
  language: string;
  userId?: string;
  lastSyncAt?: number;
  createdAt?: number;
  updatedAt?: number;
};
