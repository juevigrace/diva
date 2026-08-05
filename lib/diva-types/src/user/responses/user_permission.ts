export type UserPermissionResponse = {
  permission_id: string;
  granted_by: string | null;
  granted: boolean;
  granted_at: number;
  expires_at: number | null;
  updated_at: number;
};
