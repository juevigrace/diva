import type { Role } from '../../common/enums/role_enum';

export type CreateUserDto = {
  email: string;
  username: string;
  password: string;
};

export type UpdateUsernameDto = {
  username: string;
};

export type UpdatePasswordDto = {
  new_password: string;
};

export type UpdatePhoneNumberDto = {
  phone_number: string;
};

export type UpdateEmailDto = {
  email: string;
};

export type UpdateRole = {
  role: Role;
};
