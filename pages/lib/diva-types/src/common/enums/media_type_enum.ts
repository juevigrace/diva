export const MediaType = {
  AUDIO: 'AUDIO',
  IMAGE: 'IMAGE',
  VIDEO: 'VIDEO',
  UNSPECIFIED: 'UNSPECIFIED',
} as const;
export type MediaType = (typeof MediaType)[keyof typeof MediaType];
