import { useCallback, useState } from 'react';
import type { ZodError } from 'zod';

export function useFieldErrors() {
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});

  const clearFieldError = useCallback((field: string) => {
    setFieldErrors((prev) => {
      const next = { ...prev };
      delete next[field];
      return next;
    });
  }, []);

  const setFromZod = useCallback((error: ZodError) => {
    const fields = error.flatten().fieldErrors;
    const errors: Record<string, string> = {};
    for (const [key, msgs] of Object.entries(fields)) {
      if (msgs && msgs.length > 0) errors[key] = msgs[0];
    }
    setFieldErrors(errors);
  }, []);

  const setFromApi = useCallback((fields: Record<string, unknown>) => {
    const errors: Record<string, string> = {};
    for (const [key, msgs] of Object.entries(fields)) {
      if (Array.isArray(msgs) && msgs.length > 0) errors[key] = msgs[0];
    }
    setFieldErrors(errors);
  }, []);

  return { fieldErrors, setFieldErrors, clearFieldError, setFromZod, setFromApi };
}
