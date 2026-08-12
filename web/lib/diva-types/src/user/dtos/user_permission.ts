export type CreateUserPermissionDto = {
  permission_action: string;
  granted: boolean;
  expires_at?: number | null;
};

export type UpdateUserPermissionDto = {
  granted: boolean;
  expires_at?: number | null;
};
