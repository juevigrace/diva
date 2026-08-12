import { apiHandler, jsonResponse } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';

export const GET = apiHandler(async (ctx) => {
  const res = await apiFetch(`/api/user/check/email/${ctx.params.email}`);
  return jsonResponse(res);
});
