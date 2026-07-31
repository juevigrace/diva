import { useMemo } from 'react';
import { t, type TParams } from './t';

export function useT(lang: string) {
  return useMemo(
    () => (key: string, params?: TParams) => t(key, lang, params),
    [lang],
  );
}
