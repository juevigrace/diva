import type { Device } from './device';

export type UserDevice = {
  userId: string;
  device: Device;
  createdAt: number;
  updatedAt: number;
};
