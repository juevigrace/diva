import { useState } from 'react';
import { Button } from 'diva-ui/components/button';
import { toast } from 'diva-ui/components/sonner';
import { Input } from 'diva-ui/components/input';
import { Loader2, CheckCircle2, AlertCircle } from 'lucide-react';
import { signUpInputSchema } from '@lib/schemas/auth';
import { useT } from '@lib/i18n/useT';
import { useFieldErrors } from '@lib/hooks/useFieldErrors';
import { useAvailabilityCheck, type AvailabilityState } from '@lib/hooks/useAvailabilityCheck';
import { getDeviceLabel } from '@lib/device';

interface SignUpFormProps {
  lang?: string;
}

export default function SignUpForm({ lang = 'en' }: SignUpFormProps) {
  const t = useT(lang);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const { fieldErrors, setFieldErrors, clearFieldError, setFromZod, setFromApi } = useFieldErrors();

  const usernameAvail: AvailabilityState = useAvailabilityCheck(
    username,
    (v) => v.length >= 3,
    (v) => `/api/user/check/username/${encodeURIComponent(v)}`,
    t('auth.usernameTaken'),
  );
  const emailAvail: AvailabilityState = useAvailabilityCheck(
    email,
    (v) => v.includes('@'),
    (v) => `/api/user/check/email/${encodeURIComponent(v)}`,
    t('auth.emailTaken'),
  );

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFieldErrors({});

    const parsed = signUpInputSchema.safeParse({ username, email, password });
    if (!parsed.success) {
      setFromZod(parsed.error);
      return;
    }

    setLoading(true);

    try {
      const res = await fetch('/api/auth/signUp', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, password, device: getDeviceLabel(navigator.userAgent) }),
      });

      if (res.ok) {
        window.location.href = '/';
        return;
      }

      const json = await res.json();

      if (res.status === 400 && json.fields) {
        setFromApi(json.fields);
        return;
      }

      const msg = (json.message || '').toLowerCase();
      if (res.status === 409) {
        if (msg.includes('username')) {
          setFieldErrors({ username: json.message });
          return;
        }
        if (msg.includes('email')) {
          setFieldErrors({ email: json.message });
          return;
        }
      }

      toast.error(json.message || t('auth.anErrorOccurred'));
    } catch {
      toast.error(t('auth.networkError'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mx-auto w-full max-w-md space-y-6">
      <div className="text-center">
        <div className="bg-primary mx-auto flex h-12 w-12 items-center justify-center rounded-xl">
          <span className="text-primary-foreground text-xl font-bold">D</span>
        </div>
        <h1 className="mt-4 text-2xl font-bold tracking-tight">{t('auth.createAccount')}</h1>
        <p className="text-muted-foreground mt-2 text-sm">{t('auth.getStarted')}</p>
      </div>

      <div className="border-border bg-card rounded-xl border p-8 shadow-sm">
        <form onSubmit={handleSubmit} className="space-y-5">
          <div className="space-y-2">
            <label className="text-sm leading-none font-medium" htmlFor="username">{t('auth.username')}</label>
            <div className="relative">
              <Input
                id="username"
                type="text"
                placeholder={t('auth.usernamePlaceholder')}
                className="pr-8"
                value={username}
                onChange={(e) => { setUsername(e.target.value); clearFieldError('username'); }}
              />
              {!fieldErrors.username && usernameAvail.status !== 'idle' && (
                usernameAvail.status === 'checking' ? (
                  <Loader2 className="text-muted-foreground absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2 animate-spin" />
                ) : usernameAvail.status === 'available' ? (
                  <CheckCircle2 className="text-emerald-500 absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2" />
                ) : (
                  <AlertCircle className="text-destructive absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2" />
                )
              )}
            </div>
            {fieldErrors.username ? (
              <p className="text-destructive text-sm">{fieldErrors.username}</p>
            ) : usernameAvail.status === 'taken' ? (
              <p className="text-destructive text-sm">{usernameAvail.message}</p>
            ) : null}
          </div>
          <div className="space-y-2">
            <label className="text-sm leading-none font-medium" htmlFor="email">{t('auth.email')}</label>
            <div className="relative">
              <Input
                id="email"
                type="email"
                placeholder={t('auth.emailPlaceholder')}
                className="pr-8"
                value={email}
                onChange={(e) => { setEmail(e.target.value); clearFieldError('email'); }}
              />
              {!fieldErrors.email && emailAvail.status !== 'idle' && (
                emailAvail.status === 'checking' ? (
                  <Loader2 className="text-muted-foreground absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2 animate-spin" />
                ) : emailAvail.status === 'available' ? (
                  <CheckCircle2 className="text-emerald-500 absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2" />
                ) : (
                  <AlertCircle className="text-destructive absolute right-3 top-1/2 h-4 w-4 -translate-y-1/2" />
                )
              )}
            </div>
            {fieldErrors.email ? (
              <p className="text-destructive text-sm">{fieldErrors.email}</p>
            ) : emailAvail.status === 'taken' ? (
              <p className="text-destructive text-sm">{emailAvail.message}</p>
            ) : null}
          </div>
          <div className="space-y-2">
            <label className="text-sm leading-none font-medium" htmlFor="password">{t('auth.password')}</label>
            <Input
              id="password"
              type="password"
              placeholder={t('auth.strongPasswordPlaceholder')}
              value={password}
              onChange={(e) => { setPassword(e.target.value); clearFieldError('password'); }}
            />
            {fieldErrors.password && (
              <p className="text-destructive text-sm">{fieldErrors.password}</p>
            )}
          </div>
          <Button type="submit" className="w-full" disabled={loading}>
            {loading ? t('auth.creatingAccount') : t('auth.createAccountBtn')}
          </Button>
        </form>
      </div>

      <p className="text-muted-foreground text-center text-sm">
        {t('auth.haveAccount')}{' '}
        <a href="/signIn" className="text-primary hover:underline">{t('auth.signIn')}</a>
      </p>
    </div>
  );
}
