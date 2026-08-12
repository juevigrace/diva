import type { Permission } from '../../permission/models/permission';

export type UserPermission = {
  permission: Permission;
  userId: string;
  grantedBy: string | null;
  granted: boolean;
  grantedAt: number;
  expiresAt: number | null;
  updatedAt: number;
};
