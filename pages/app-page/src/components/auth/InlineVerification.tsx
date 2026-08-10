import { useState, useRef, useEffect } from 'react';
import { Button } from 'diva-ui/components/button';
import { useT } from '@lib/i18n/useT';
import OtpInput, { type OtpInputHandle } from './OtpInput';

interface InlineVerificationProps {
  action: string;
  email: string;
  onVerified: () => void;
  onCancel: () => void;
  autoRequest?: boolean;
  lang?: string;
}

export default function InlineVerification({ action, email, onVerified, onCancel, autoRequest = false, lang = 'en' }: InlineVerificationProps) {
  const t = useT(lang);
  const [step, setStep] = useState<'request' | 'verify'>('request');
  const [actionId, setActionId] = useState('');
  const [token, setToken] = useState(['', '', '', '', '', '']);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const otpRef = useRef<OtpInputHandle>(null);
  const autoRequested = useRef(false);

  useEffect(() => {
    if (!autoRequest || autoRequested.current) return;
    autoRequested.current = true;
    handleRequestCode();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleRequestCode = async () => {
    setError('');
    setLoading(true);
    try {
      const res = await fetch('/api/verification/request', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, action }),
      });
      if (res.ok) {
        const json = await res.json();
        setActionId(json.id || json.data?.id || '');
        setStep('verify');
        setTimeout(() => otpRef.current?.focusFirst(), 100);
      } else {
        const json = await res.json();
        setError(json.message || t('verification.failedToSendCode'));
      }
    } catch {
      setError(t('auth.networkError'));
    }
    setLoading(false);
  };

  const tokenComplete = token.every((d) => d !== '');

  const handleVerify = async () => {
    if (!tokenComplete || !actionId) return;
    setError('');
    setLoading(true);
    try {
      const res = await fetch('/api/verification/', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ action_id: actionId, token: token.join('') }),
      });
      if (res.ok) {
        onVerified();
      } else {
        const json = await res.json();
        setError(json.message || t('verification.verificationFailed'));
      }
    } catch {
      setError(t('auth.networkError'));
    }
    setLoading(false);
  };

  if (step === 'request') {
    return (
      <div className="space-y-3">
        <p className="text-sm text-muted-foreground">
          {t('verification.aCodeWillBeSent')} <strong>{email}</strong>.
        </p>
        {error && <p className="text-destructive text-xs">{error}</p>}
        <div className="flex gap-2">
          <Button type="button" size="sm" onClick={handleRequestCode} disabled={loading}>
            {loading ? t('verification.sending') : t('verification.sendCode')}
          </Button>
          <Button type="button" size="sm" variant="ghost" onClick={onCancel}>{t('common.cancel')}</Button>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-3">
        <p className="text-sm text-muted-foreground">
          {t('verification.enterCode')} <strong>{email}</strong>.
        </p>
      <OtpInput ref={otpRef} value={token} onChange={setToken} />
      {error && <p className="text-destructive text-xs text-center">{error}</p>}
      <div className="flex gap-2">
        <Button type="button" size="sm" disabled={loading || !tokenComplete} onClick={handleVerify}>
          {loading ? t('verification.verifying') : t('verification.verifyCode')}
        </Button>
        <Button type="button" size="sm" variant="ghost" onClick={onCancel}>{t('common.cancel')}</Button>
      </div>
      <button type="button" className="text-primary hover:underline text-xs cursor-pointer" onClick={handleRequestCode}>
        {t('verification.resendCode')}
      </button>
    </div>
  );
}
