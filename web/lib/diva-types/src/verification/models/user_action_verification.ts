import type { UserAction } from '../../user/models/user_action';

export type UserActionVerification = {
  action: UserAction;
  token: string;
  expiresAt: number;
  usedAt: number | null;
  verified: boolean;
};
