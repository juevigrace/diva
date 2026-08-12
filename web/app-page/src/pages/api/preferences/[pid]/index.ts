import { apiRoute, jsonResponse } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';

export const GET = apiRoute(async (ctx, session) => {
  return jsonResponse(await apiFetch(`/api/user/preferences/${ctx.params.pid}/`, { token: session.access_token }));
});

export const PUT = apiRoute(async (ctx, session) => {
  const res = await apiFetch(`/api/user/preferences/${ctx.params.pid}/`, { method: 'PUT', body: await ctx.request.json(), token: session.access_token });
  if (res.ok && ctx.session) {
    await ctx.session.set('userLang', undefined);
  }
  return jsonResponse(res);
});
