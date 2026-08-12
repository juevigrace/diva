export type CreateProfileDto = {
  first_name: string;
  last_name: string;
  alias: string;
  bio?: string;
  birth_date: number;
};

export type UpdateProfileDto = {
  first_name: string;
  last_name: string;
  alias: string;
  bio?: string;
  birth_date: number;
};
