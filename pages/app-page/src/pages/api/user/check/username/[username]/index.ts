import { apiHandler, jsonResponse } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';

export const GET = apiHandler(async (ctx) => {
  const res = await apiFetch(`/api/user/check/username/${ctx.params.username}`);
  return jsonResponse(res);
});
