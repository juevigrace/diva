import { useState } from 'react';
import { useT } from '@lib/i18n/useT';

interface AdminSessionActionsProps {
  lang?: string;
}

export default function AdminSessionActions({ lang = 'en' }: AdminSessionActionsProps) {
  const t = useT(lang);
  const [status, setStatus] = useState<{ type: 'success' | 'error'; message: string } | null>(null);
  const [loading, setLoading] = useState<string | null>(null);

  const run = async (path: string, successKey: string, failKey: string) => {
    setLoading(path);
    setStatus(null);
    try {
      const res = await fetch(path, { method: 'DELETE' });
      if (res.ok) {
        setStatus({ type: 'success', message: t(successKey) });
      } else {
        const j = await res.json();
        setStatus({ type: 'error', message: j.message || t(failKey) });
      }
    } catch {
      setStatus({ type: 'error', message: t(failKey) });
    }
    setLoading(null);
  };

  const buttonClass = 'inline-flex items-center justify-center rounded-md px-4 py-2 text-sm font-medium transition-colors disabled:opacity-50';

  return (
    <>
      <div className="grid gap-6 md:grid-cols-2">
        <div className="border-border bg-card rounded-xl border p-6 shadow-sm">
          <h3 className="text-lg font-semibold">{t('admin.closeExpiredTitle')}</h3>
          <p className="text-muted-foreground mt-1 text-sm">{t('admin.closeExpiredDesc')}</p>
          <div className="mt-4">
            <button
              type="button"
              className={`${buttonClass} bg-primary text-primary-foreground hover:bg-primary/90`}
              disabled={loading !== null}
              onClick={() => run('/api/sessions/close', 'admin.expiredClosed', 'admin.failedCloseExpired')}
            >
              {t('admin.closeExpired')}
            </button>
          </div>
        </div>

        <div className="border-border bg-card rounded-xl border p-6 shadow-sm">
          <h3 className="text-lg font-semibold">{t('admin.purgeSessionsTitle')}</h3>
          <p className="text-muted-foreground mt-1 text-sm">{t('admin.purgeSessionsDesc')}</p>
          <div className="mt-4">
            <button
              type="button"
              className={`${buttonClass} bg-destructive text-destructive-foreground hover:bg-destructive/90`}
              disabled={loading !== null}
              onClick={() => run('/api/sessions', 'admin.sessionsPurged', 'admin.failedPurgeSessions')}
            >
              {t('admin.purgeSessions')}
            </button>
          </div>
        </div>
      </div>

      {status && (
        <p className={`mt-4 text-sm ${status.type === 'success' ? 'text-green-600' : 'text-destructive'}`}>
          {status.message}
        </p>
      )}
    </>
  );
}
