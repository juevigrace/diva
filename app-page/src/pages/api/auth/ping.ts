import { actions } from 'astro:actions';
import { apiRoute, json } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';

export const POST = apiRoute(async (ctx, session) => {
  const res = await apiFetch('/api/auth/ping', { method: 'POST', token: session.access_token });
  if (!res.ok) {
    await ctx.callAction(actions.session.deleteSession, {});
    return json(res.json, res.status);
  }
  return new Response(null, { status: res.status });
});
