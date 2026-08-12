import type { Role } from '../../common/enums/role_enum';

export type UpdatePermissionDto = {
  name: string;
  description: string;
};

export type UpdatePermissionRoleLevelDto = {
  level: Role;
};
