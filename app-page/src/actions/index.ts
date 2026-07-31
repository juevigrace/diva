import { defineAction, ActionError } from 'astro:actions';
import { z } from 'astro/zod';
import { apiFetch } from '@api/lib/fetch';
import { getDeviceLabel } from '@lib/device';
import type { SessionResponse } from 'diva-types/auth/responses';

export const server = {
  session: {
    saveSession: defineAction({
      accept: 'json',
      input: z.object({
        session_id: z.string(),
        user_id: z.string(),
        access_token: z.string(),
        refresh_token: z.string(),
        status: z.string(),
        type: z.string(),
        device_id: z.string(),
        ip: z.string(),
        agent: z.string(),
        access_expires_at: z.number(),
        refresh_expires_at: z.number(),
        created_at: z.number(),
        updated_at: z.number(),
      }),
      handler: async (input, ctx) => {
        await ctx.session?.set('auth', input);
      },
    }),

    getSession: defineAction({
      accept: 'json',
      handler: async (_, ctx) => {
        const session = await ctx.session?.get<SessionResponse>('auth');
        if (!session) {
          throw new ActionError({ code: 'NOT_FOUND', message: 'Session not found' });
        }
        return session;
      },
    }),

    deleteSession: defineAction({
      accept: 'json',
      handler: async (_, ctx) => {
        await ctx.session?.set('auth', undefined);
      },
    }),
  },

  auth: {
    refresh: defineAction({
      accept: 'json',
      handler: async (_, ctx) => {
        const session = await ctx.session?.get<SessionResponse>('auth');
        if (!session) {
          throw new ActionError({ code: 'NOT_FOUND', message: 'Session not found' });
        }
        const res = await apiFetch<SessionResponse>('/api/auth/refresh', {
          method: 'POST',
          body: { device: getDeviceLabel(session.agent), user_agent: session.agent },
          token: session.refresh_token,
        });
        if (!res.ok) {
          throw new ActionError({ code: 'BAD_REQUEST', message: res.json.message || 'Refresh failed' });
        }
        await ctx.session?.set('auth', res.json.data);
        return res.json.data;
      },
    }),
  },
};
