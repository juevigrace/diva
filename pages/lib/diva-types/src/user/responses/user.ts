import type { UserStateResponse } from './user_state';

export type UserResponse = {
  id: string;
  username: string;
  email: string;
  phone_number: string | null;
  role: string;
  state: UserStateResponse | null;
  created_at: number;
  updated_at: number;
  deleted_at: number | null;
};
