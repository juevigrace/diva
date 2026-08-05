import { apiHandler, json, saveSessionAndRespond } from '@api/lib/response';
import { apiFetch } from '@api/lib/fetch';
import { forgotPasswordConfirmSchema } from '@lib/schemas/auth';
import { getDeviceLabel } from '@lib/device';
import type { SessionResponse } from 'diva-types/auth/responses/session';

export const POST = apiHandler(async (ctx) => {
  const body = await ctx.request.json();
  const parsed = forgotPasswordConfirmSchema.safeParse(body);
  if (!parsed.success) {
    return json({ message: 'Validation failed', fields: parsed.error.flatten().fieldErrors }, 400);
  }

  const res = await apiFetch<SessionResponse>('/api/auth/forgot/password/confirm', {
    method: 'POST',
    body: {
      id: parsed.data.id,
      session_data: {
        device: parsed.data.device || getDeviceLabel(ctx.request.headers.get('User-Agent') || 'web'),
        user_agent: ctx.request.headers.get('User-Agent') || 'web',
      },
    },
  });

  return saveSessionAndRespond(ctx, res);
});
