import en from './locales/en.json';
import es from './locales/es.json';

const locales: Record<string, Record<string, unknown>> = { en, es };

export type TParams = Record<string, string | number>;

function resolvePath(obj: Record<string, unknown>, path: string): unknown {
  const keys = path.split('.');
  let current: unknown = obj;
  for (const key of keys) {
    if (current && typeof current === 'object' && key in current) {
      current = (current as Record<string, unknown>)[key];
    } else {
      return undefined;
    }
  }
  return current;
}

function interpolate(template: string, params?: TParams): string {
  if (!params) return template;
  return template.replace(/\{\{(\w+)\}\}/g, (match, key: string) =>
    params[key] !== undefined ? String(params[key]) : match,
  );
}

export function t(key: string, lang: string = 'en', params?: TParams): string {
  const locale = locales[lang] || locales.en;
  const value = resolvePath(locale, key);
  if (typeof value !== 'string') return key;
  return interpolate(value, params);
}
