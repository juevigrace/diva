export const SessionType = {
  NORMAL: 'NORMAL',
  TEMPORAL: 'TEMPORAL',
} as const;
export type SessionType = (typeof SessionType)[keyof typeof SessionType];
