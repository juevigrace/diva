import { apiHandler, jsonResponse } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';

export const POST = apiHandler(async (ctx) => {
  const res = await apiFetch('/api/verification/request', { method: 'POST', body: await ctx.request.json() });
  return jsonResponse(res);
});
