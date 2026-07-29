import { actions } from 'astro:actions';
import { requireSession, json } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';
import type { SessionResponse } from 'diva-types/auth/responses';

export async function POST(context: import('astro').APIContext): Promise<Response> {
  try {
    const session = await requireSession(context);
    const body = await context.request.json();
    const res = await apiFetch<SessionResponse>('/api/auth/refresh', { method: 'POST', body, token: session.access_token });
    if (!res.ok) return json(res.json, res.status);
    const { error: saveError } = await context.callAction(actions.session.saveSession, res.json.data);
    if (saveError) {
      return json({ message: 'Failed to save session' }, 500);
    }
    return json(res.json.data, res.status);
  } catch (e) {
    if (e instanceof Response) return e;
    return json({ message: `${e}` }, 500);
  }
}
