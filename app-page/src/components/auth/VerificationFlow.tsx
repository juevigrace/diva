import { useState, useRef, useEffect, type FormEvent } from 'react';
import { Button } from 'diva-ui/components/button';
import { Input } from 'diva-ui/components/input';
import { toast } from 'diva-ui/components/sonner';
import { Loader2 } from 'lucide-react';
import { useT } from '@lib/i18n/useT';
import { getDeviceLabel } from '@lib/device';
import { ActionType } from 'diva-types/verification/enums';
import OtpInput, { type OtpInputHandle } from './OtpInput';

interface VerificationFlowProps {
  action: string;
  email?: string;
  lang?: string;
}

export default function VerificationFlow({ action, email: initialEmail = '', lang = 'en' }: VerificationFlowProps) {
  const t = useT(lang);
  const [step, setStep] = useState<'request' | 'verify' | 'verified' | 'restored' | 'confirming' | 'new_password' | 'complete'>(initialEmail ? 'verify' : 'request');
  const [email, setEmail] = useState(initialEmail);
  const [actionId, setActionId] = useState('');
  const [token, setToken] = useState(['', '', '', '', '', '']);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const otpRef = useRef<OtpInputHandle>(null);

  const isPasswordReset = action === ActionType.PASSWORD_RESET;
  const isUserRestore = action === ActionType.USER_RESTORE;

  const requestCode = async (emailToUse: string) => {
    setError('');
    try {
      const res = await fetch('/api/verification/request', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: emailToUse, action }),
      });

      if (res.ok) {
        const json = await res.json();
        setActionId(json.id || json.data?.id || '');
        setStep('verify');
        toast.success(t('verification.codeSent'));
        setTimeout(() => otpRef.current?.focusFirst(), 100);
        return true;
      } else {
        const json = await res.json();
        setError(json.message || t('verification.failedToSendCode'));
        return false;
      }
    } catch {
      setError(t('auth.networkError'));
      return false;
    }
  };

  const handleRequestCode = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    await requestCode(email);
    setLoading(false);
  };

  useEffect(() => {
    if (action === ActionType.USER_RESTORE) {
      if (email) requestCode(email);
      return;
    }

    if (action !== ActionType.USER_VERIFICATION || email) return;

    const autoRequest = async () => {
      setLoading(true);
      try {
        const userRes = await fetch('/api/user/me');
        if (!userRes.ok) throw new Error();
        const userData = await userRes.json();
        const userEmail = userData.email || '';
        if (!userEmail) throw new Error();
        setEmail(userEmail);
        await requestCode(userEmail);
      } catch {
        setError(t('verification.failedToSendCode'));
      }
      setLoading(false);
    };

    autoRequest();
  }, []);

  const tokenComplete = token.every((d) => d !== '');

  const handleVerifyCode = async () => {
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
        if (isPasswordReset) {
          setStep('confirming');
          await handleForgotPasswordConfirm();
        } else if (isUserRestore) {
          setStep('restored');
        } else {
          setStep('verified');
        }
      } else {
        const json = await res.json();
        setError(json.message || t('verification.verificationFailed'));
      }
    } catch {
      setError(t('auth.networkError'));
    }
    setLoading(false);
  };

  const handleForgotPasswordConfirm = async () => {
    try {
      const res = await fetch('/api/auth/forgot/password/confirm', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: actionId, device: getDeviceLabel(navigator.userAgent) }),
      });

      if (res.ok) {
        setStep('new_password');
        toast.success(t('verification.identityVerified'));
      } else {
        const json = await res.json();
        setError(json.message || t('verification.failedToSendCode'));
        setStep('verify');
      }
    } catch {
      setError(t('auth.networkError'));
      setStep('verify');
    }
    setLoading(false);
  };

  const handlePasswordSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (newPassword.length < 4) {
      setError(t('profile.passwordMin'));
      return;
    }
    setError('');
    setLoading(true);

    try {
      const res = await fetch('/api/user/me/password', {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ new_password: newPassword }),
      });

      if (res.ok) {
        await fetch('/api/auth/signOut', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ device: getDeviceLabel(navigator.userAgent), user_agent: navigator.userAgent }),
        }).catch(() => {});
        setStep('complete');
      } else {
        const json = await res.json();
        setError(json.message || t('profile.failedChangePassword'));
      }
    } catch {
      setError(t('auth.networkError'));
    }
    setLoading(false);
  };

  if (step === 'verified') {
    return (
      <div className="mx-auto w-full max-w-md text-center">
        <div className="bg-primary/10 text-primary mx-auto flex h-16 w-16 items-center justify-center rounded-2xl">
          <svg className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
          </svg>
        </div>
        <h1 className="mt-6 text-2xl font-bold">{t('verification.emailVerified')}</h1>
        <p className="text-muted-foreground mt-2 text-sm">{t('verification.emailVerifiedDesc')}</p>
        <Button asChild className="mt-8">
          <a href="/">{t('verification.goToDashboard')}</a>
        </Button>
      </div>
    );
  }

  if (step === 'restored') {
    return (
      <div className="mx-auto w-full max-w-md text-center">
        <div className="bg-primary/10 text-primary mx-auto flex h-16 w-16 items-center justify-center rounded-2xl">
          <svg className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
          </svg>
        </div>
        <h1 className="mt-6 text-2xl font-bold">{t('verification.accountRestored')}</h1>
        <p className="text-muted-foreground mt-2 text-sm">{t('verification.accountRestoredDesc')}</p>
        <Button asChild className="mt-8">
          <a href="/signIn">{t('verification.signInWithRestoredAccount')}</a>
        </Button>
      </div>
    );
  }

  if (step === 'complete') {
    return (
      <div className="mx-auto w-full max-w-md text-center">
        <div className="bg-primary/10 text-primary mx-auto flex h-16 w-16 items-center justify-center rounded-2xl">
          <svg className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
          </svg>
        </div>
        <h1 className="mt-6 text-2xl font-bold">{t('verification.passwordChanged')}</h1>
        <p className="text-muted-foreground mt-2 text-sm">{t('verification.passwordChangedDesc')}</p>
        <Button asChild className="mt-8">
          <a href="/signIn">{t('verification.signInWithNewPassword')}</a>
        </Button>
      </div>
    );
  }

  if (step === 'confirming') {
    return (
      <div className="mx-auto w-full max-w-md text-center">
        <Loader2 className="text-primary mx-auto h-8 w-8 animate-spin" />
        <h1 className="mt-6 text-2xl font-bold">{t('verification.confirming')}</h1>
        <p className="text-muted-foreground mt-2 text-sm">{t('verification.pleaseWait')}</p>
      </div>
    );
  }

  if (step === 'new_password') {
    return (
      <div className="mx-auto w-full max-w-md">
        <div className="text-center">
          <div className="bg-primary mx-auto flex h-12 w-12 items-center justify-center rounded-xl">
            <span className="text-primary-foreground text-xl font-bold">D</span>
          </div>
          <h1 className="mt-4 text-2xl font-bold tracking-tight">{t('verification.setNewPassword')}</h1>
          <p className="text-muted-foreground mt-2 text-sm">{t('verification.enterNewPassword')}</p>
        </div>

        <div className="border-border bg-card mt-8 rounded-xl border p-8 shadow-sm">
          <form onSubmit={handlePasswordSubmit} className="space-y-5">
            <div className="space-y-2">
              <label className="text-sm leading-none font-medium" htmlFor="new-password">{t('verification.newPassword')}</label>
              <Input
                id="new-password"
                type="password"
                placeholder={t('auth.passwordPlaceholder')}
                value={newPassword}
                onChange={(e) => { setNewPassword(e.target.value); setError(''); }}
              />
            </div>
            {error && <p className="text-destructive text-sm">{error}</p>}
            <Button type="submit" className="w-full" disabled={loading || newPassword.length < 4}>
              {loading ? t('verification.changingPassword') : t('verification.changePassword')}
            </Button>
          </form>
        </div>
      </div>
    );
  }

  if (step === 'request') {
    return (
      <div className="mx-auto w-full max-w-md">
        <div className="text-center">
          <div className="bg-primary mx-auto flex h-12 w-12 items-center justify-center rounded-xl">
            <span className="text-primary-foreground text-xl font-bold">D</span>
          </div>
          <h1 className="mt-4 text-2xl font-bold tracking-tight">
            {isPasswordReset
              ? t('verification.resetPassword')
              : isUserRestore
                ? t('verification.restoreAccount')
                : t('verification.verifyEmailTitle')}
          </h1>
          <p className="text-muted-foreground mt-2 text-sm">
            {t('verification.enterCode')}
          </p>
        </div>

        <div className="border-border bg-card mt-8 rounded-xl border p-8 shadow-sm">
          <form onSubmit={handleRequestCode} className="space-y-5">
            <div className="space-y-2">
              <label className="text-sm leading-none font-medium" htmlFor="email">{t('auth.email')}</label>
              <Input
                id="email"
                type="email"
                placeholder={t('auth.emailPlaceholder')}
                value={email}
                onChange={(e) => { setEmail(e.target.value); setError(''); }}
                required
              />
            </div>
            {error && <p className="text-destructive text-sm">{error}</p>}
            <Button type="submit" className="w-full" disabled={loading || !email}>
              {loading ? t('verification.sending') : t('verification.sendCode')}
            </Button>
          </form>
        </div>

        <p className="text-muted-foreground mt-6 text-center text-sm">
          {t('verification.rememberPassword')}{' '}
          <a href="/signIn" className="text-primary hover:underline">{t('auth.signIn')}</a>
        </p>
      </div>
    );
  }

  return (
    <div className="mx-auto w-full max-w-md">
      <div className="text-center">
        <div className="bg-primary mx-auto flex h-12 w-12 items-center justify-center rounded-xl">
          <span className="text-primary-foreground text-xl font-bold">D</span>
        </div>
        <h1 className="mt-4 text-2xl font-bold tracking-tight">{t('verification.enterCode')}</h1>
        <p className="text-muted-foreground mt-2 text-sm">
          {t('verification.enterCode')} <span className="font-medium text-foreground">{email}</span>
        </p>
      </div>

      <div className="border-border bg-card mt-8 rounded-xl border p-8 shadow-sm">
        <div className="space-y-5">
          <div>
            <label className="text-sm leading-none font-medium">{t('verification.codePlaceholder')}</label>
            <div className="mt-3">
              <OtpInput ref={otpRef} value={token} onChange={setToken} />
            </div>
          </div>
          {error && <p className="text-destructive text-sm text-center">{error}</p>}
          <Button
            type="button"
            className="w-full"
            disabled={loading || !tokenComplete}
            onClick={handleVerifyCode}
          >
            {loading ? t('verification.verifying') : t('verification.verifyCode')}
          </Button>
        </div>
      </div>

        <p className="text-muted-foreground mt-6 text-center text-sm">
          {t('verification.resendCode')}{' '}
          <button
            type="button"
            className="text-primary hover:underline cursor-pointer"
            onClick={handleRequestCode}
          >
            {t('verification.resendCode')}
          </button>
      </p>
    </div>
  );
}
