import type { UserStatus } from '../enums/user_status_enum';

export type UpdateVerified = {
  verified: boolean;
};

export type UpdateUserStatus = {
  status: UserStatus;
};
