import type { UserStatus } from '../enums/user_status_enum';

export type UserState = {
  verified: boolean;
  status: UserStatus;
  lastActiveAt: number;
  updatedAt: number;
};
