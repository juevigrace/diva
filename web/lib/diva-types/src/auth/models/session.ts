import type { SessionStatus } from '../enums/session_status_enum';
import type { SessionType } from '../enums/session_type_enum';
import type { Device } from '../../device/models/device';
import type { User } from '../../user/models/user';

export type Session = {
  id: string;
  user: User;
  accessToken: string;
  refreshToken: string;
  device: Device;
  ipAddress: string;
  userAgent: string;
  status: SessionStatus;
  type: SessionType;
  accessExpiresAt: number;
  refreshExpiresAt: number;
  createdAt: number;
  updatedAt: number;
};
