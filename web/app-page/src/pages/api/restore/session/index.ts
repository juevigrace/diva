import { apiHandler, json } from '@api/lib/response';
import { actions } from 'astro:actions';

export const POST = apiHandler(async (ctx) => {
  const { email } = (await ctx.request.json()) as { email?: string };
  if (!email) {
    return json({ message: 'email is required' }, 400);
  }

  const { error } = await ctx.callAction(actions.restore.saveEmail, { email });
  if (error) {
    return json({ message: 'Failed to save restore email' }, 500);
  }

  return json({ ok: true });
});