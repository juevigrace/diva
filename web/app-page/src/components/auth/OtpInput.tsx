import { useImperativeHandle, useRef, forwardRef } from 'react';

export interface OtpInputHandle {
  focusFirst: () => void;
}

interface OtpInputProps {
  value: string[];
  onChange: (next: string[]) => void;
  length?: number;
  disabled?: boolean;
}

export default forwardRef<OtpInputHandle, OtpInputProps>(function OtpInput(
  { value, onChange, length = 6, disabled = false },
  ref,
) {
  const inputs = useRef<(HTMLInputElement | null)[]>([]);

  useImperativeHandle(ref, () => ({
    focusFirst: () => inputs.current[0]?.focus(),
  }));

  const handleChange = (index: number, raw: string) => {
    if (raw.length > 1) {
      const digits = raw.replace(/\D/g, '').split('').slice(0, length);
      const next = [...value];
      digits.forEach((d, i) => {
        if (index + i < length) next[index + i] = d;
      });
      onChange(next);
      inputs.current[Math.min(index + digits.length, length - 1)]?.focus();
      return;
    }

    const digit = raw.replace(/\D/g, '');
    const next = [...value];
    next[index] = digit;
    onChange(next);
    if (digit && index < length - 1) {
      inputs.current[index + 1]?.focus();
    }
  };

  const handleKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace' && !value[index] && index > 0) {
      inputs.current[index - 1]?.focus();
    }
  };

  return (
    <div className="flex justify-center gap-2">
      {value.map((digit, i) => (
        <input
          key={i}
          ref={(el) => { inputs.current[i] = el; }}
          type="text"
          inputMode="numeric"
          maxLength={length}
          className="border-input bg-background focus-visible:ring-ring h-12 w-10 rounded-md border text-center text-lg font-bold shadow-sm focus-visible:ring-1 focus-visible:outline-none"
          value={digit}
          disabled={disabled}
          onChange={(e) => handleChange(i, e.target.value)}
          onKeyDown={(e) => handleKeyDown(i, e)}
          autoComplete="one-time-code"
        />
      ))}
    </div>
  );
});
