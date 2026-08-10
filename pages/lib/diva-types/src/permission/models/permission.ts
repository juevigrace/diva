import type { Role } from '../../common/enums/role_enum';
import type { PermissionAction } from '../enums/permission_action_enum';

export type Permission = {
  id: string;
  name: string;
  description: string;
  action: PermissionAction;
  roleLevel: Role;
  createdAt: number;
  updatedAt: number;
  deletedAt: number | null;
};
