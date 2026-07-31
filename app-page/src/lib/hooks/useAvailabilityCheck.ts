import { useEffect, useRef, useState } from 'react';

export interface AvailabilityState {
  status: 'idle' | 'checking' | 'available' | 'taken';
  message: string;
}

export function useAvailabilityCheck(
  value: string,
  shouldRun: (value: string) => boolean,
  buildUrl: (value: string) => string,
  takenMessage: string,
  debounceMs = 500,
): AvailabilityState {
  const [state, setState] = useState<AvailabilityState>({ status: 'idle', message: '' });

  const shouldRunRef = useRef(shouldRun);
  const buildUrlRef = useRef(buildUrl);
  const takenMessageRef = useRef(takenMessage);
  shouldRunRef.current = shouldRun;
  buildUrlRef.current = buildUrl;
  takenMessageRef.current = takenMessage;

  useEffect(() => {
    if (!shouldRunRef.current(value)) {
      setState({ status: 'idle', message: '' });
      return;
    }

    const timer = setTimeout(async () => {
      setState({ status: 'checking', message: '' });
      try {
        const res = await fetch(buildUrlRef.current(value));
        const json = await res.json();
        if (res.ok) {
          setState({ status: 'available', message: '' });
        } else {
          setState({ status: 'taken', message: json.message || takenMessageRef.current });
        }
      } catch {
        setState({ status: 'idle', message: '' });
      }
    }, debounceMs);

    return () => clearTimeout(timer);
  }, [value, debounceMs]);

  return state;
}
