import { apiRoute, json } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';
import { batchOperate } from '@api/lib/batch';

export const POST = apiRoute(async (ctx, session) => {
  const { user_ids, role } = await ctx.request.json();
  if (!Array.isArray(user_ids) || user_ids.length === 0) {
    return json({ message: 'user_ids must be a non-empty array' }, 400);
  }
  if (!role) {
    return json({ message: 'role is required' }, 400);
  }

  const data = await batchOperate(user_ids, (uid) =>
    apiFetch(`/api/user/${uid}/role`, { method: 'PATCH', body: { role }, token: session.access_token }),
  );
  return json({ data });
});
