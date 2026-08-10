export type UserProfile = {
  userId?: string;
  firstName: string | null;
  lastName: string | null;
  birthDate: number | null;
  alias: string | null;
  avatar: string | null;
  bio: string | null;
  updatedAt?: number;
};
