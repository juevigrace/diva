import { apiRoute, saveSessionAndRespond } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';
import type { SessionResponse } from 'diva-types/auth/responses/session';

export const POST = apiRoute(async (ctx, session) => {
  const body = await ctx.request.json();
  const res = await apiFetch<SessionResponse>('/api/auth/refresh', { method: 'POST', body, token: session.access_token });
  return saveSessionAndRespond(ctx, res);
});
