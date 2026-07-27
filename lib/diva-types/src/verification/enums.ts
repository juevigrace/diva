export const ActionType = {
  USER_VERIFICATION: 'USER_VERIFICATION',
  PASSWORD_RESET: 'PASSWORD_RESET',
  EMAIL_UPDATE: 'EMAIL_UPDATE',
  USERNAME_UPDATE: 'USERNAME_UPDATE',
  PHONE_UPDATE: 'PHONE_UPDATE',
  USER_RESTORE: 'USER_RESTORE',
} as const;
export type ActionType = (typeof ActionType)[keyof typeof ActionType];
