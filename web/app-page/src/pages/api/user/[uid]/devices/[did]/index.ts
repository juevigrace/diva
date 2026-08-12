import { apiRoute, jsonResponse, nullResponse } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';

export const GET = apiRoute(async (ctx, session) => {
  return jsonResponse(await apiFetch(`/api/user/${ctx.params.uid}/devices/${ctx.params.did}`, { token: session.access_token }));
});

export const DELETE = apiRoute(async (ctx, session) => {
  return nullResponse(await apiFetch(`/api/user/${ctx.params.uid}/devices/${ctx.params.did}`, { method: 'DELETE', token: session.access_token }));
});
