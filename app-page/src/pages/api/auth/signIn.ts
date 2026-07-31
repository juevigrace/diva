import { apiHandler, json, saveSessionAndRespond } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';
import { signInInputSchema } from '@lib/schemas/auth';
import { getDeviceLabel } from '@lib/device';
import type { SessionResponse } from 'diva-types/auth/responses';

export const POST = apiHandler(async (ctx) => {
  const body = await ctx.request.json();
  const parsed = signInInputSchema.safeParse(body);
  if (!parsed.success) {
    const fields = parsed.error.flatten().fieldErrors;
    const message = Object.values(fields).flat().join('. ');
    return json({ message: message || 'Validation failed', fields }, 400);
  }

  const res = await apiFetch<SessionResponse>('/api/auth/signIn', {
    method: 'POST',
    body: {
      username: parsed.data.username,
      password: parsed.data.password,
      session_data: {
        device: parsed.data.device || getDeviceLabel(ctx.request.headers.get('User-Agent') || 'web'),
        user_agent: ctx.request.headers.get('User-Agent') || 'web',
      },
    },
  });

  return saveSessionAndRespond(ctx, res);
});
