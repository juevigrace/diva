import type { Action } from '../../verification/enums/action_enum';

export type UserAction = {
  id: string;
  name: Action;
  userId: string;
};
