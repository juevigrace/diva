import { apiRoute, json } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';
import { batchOperate } from '@api/lib/batch';

export const POST = apiRoute(async (ctx, session) => {
  const { session_ids } = await ctx.request.json();
  if (!Array.isArray(session_ids) || session_ids.length === 0) {
    return json({ message: 'session_ids must be a non-empty array' }, 400);
  }

  const data = await batchOperate(session_ids, (sid) =>
    apiFetch(`/api/sessions/${sid}`, { method: 'DELETE', token: session.access_token }),
  );
  return json({ data });
});
