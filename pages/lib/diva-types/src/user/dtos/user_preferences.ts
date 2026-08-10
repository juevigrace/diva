import type { Theme } from '../enums/theme_enum';

export type CreateUserPreferencesDto = {
  theme: Theme;
  onboarding_completed: boolean;
  language: string;
};

export type UpdateUserPreferencesDto = {
  theme: Theme;
  language: string;
};
