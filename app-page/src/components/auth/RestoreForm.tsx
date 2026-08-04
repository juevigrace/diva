import { useState, type FormEvent } from 'react';
import { Button } from 'diva-ui/components/button';
import { Input } from 'diva-ui/components/input';
import { useT } from '@lib/i18n/useT';
import { ActionType } from 'diva-types/verification/enums';

interface RestoreFormProps {
  email?: string;
  lang?: string;
}

export default function RestoreForm({ email: initialEmail = '', lang = 'en' }: RestoreFormProps) {
  const t = useT(lang);
  const [email, setEmail] = useState(initialEmail);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const res = await fetch('/api/verification/request', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, action: ActionType.USER_RESTORE }),
      });

      if (res.ok) {
        let saved = false;
        try {
          const saveRes = await fetch('/api/restore/session', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email }),
          });
          saved = saveRes.ok;
        } catch {
          saved = false;
        }

        let url = `/verify?action=${encodeURIComponent(ActionType.USER_RESTORE)}`;
        if (!saved) url += `&email=${encodeURIComponent(email)}`;
        window.location.href = url;
        return;
      }

      const json = await res.json();
      setError(json.message || t('verification.failedToSendCode'));
    } catch {
      setError(t('auth.networkError'));
    }
    setLoading(false);
  };

  return (
    <div className="mx-auto w-full max-w-md">
      <div className="text-center">
        <div className="bg-primary mx-auto flex h-12 w-12 items-center justify-center rounded-xl">
          <span className="text-primary-foreground text-xl font-bold">D</span>
        </div>
        <h1 className="mt-4 text-2xl font-bold tracking-tight">{t('verification.restoreAccount')}</h1>
        <p className="text-muted-foreground mt-2 text-sm">{t('verification.aCodeWillBeSent')}</p>
      </div>

      <div className="border-border bg-card mt-8 rounded-xl border p-8 shadow-sm">
        <form onSubmit={handleSubmit} className="space-y-5">
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
