import type { Json } from '../../common/json';
import type { Permission } from '../models/permission';

export type PermissionResponse = Json<Omit<Permission, 'deletedAt'>>;
