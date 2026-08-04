import { defineMiddleware } from 'astro:middleware';
import { actions } from 'astro:actions';
import { API_BASE_URL } from 'astro:env/server';
import type { SessionResponse } from 'diva-types/auth/responses';

declare global {
  namespace App {
    interface SessionData {
      auth?: SessionResponse;
      restoreEmail?: string;
      userLang?: string;
    }
    interface Locals {
      user: {
        userId: string;
        username: string;
        email: string;
        phoneNumber: string | null;
        role: string;
        createdAt: number;
        updatedAt: number;
        deletedAt: number | null;
      } | null;
      state: {
        verified: boolean;
        status: string;
        lastActiveAt: number;
        updatedAt: number;
      } | null;
      profile: {
        firstName: string | null;
        lastName: string | null;
        birthDate: number | null;
        alias: string | null;
        avatar: string | null;
        bio: string | null;
      } | null;
      preferences: {
        id: string;
        theme: string;
        onboarding_completed: boolean;
        language: string;
      } | null;
      lang: string;
    }
  }
}

const publicRoutes = [
  '/home',
  '/signIn',
  '/signUp',
  '/verify',
  '/restore',
  '/forgot-password',
  '/about',
  '/contact',
  '/pricing',
  '/docs',
  '/api',
  '/_astro',
  '/robots.txt',
  '/404',
];

function isPublicRoute(pathname: string): boolean {
  return publicRoutes.some((route) => pathname === route || pathname.startsWith(route + '/'));
}

const adminRoutes = ['/admin/permissions', '/admin/sessions', '/admin/health', '/admin/api', '/devices'];

function isAdminRoute(pathname: string): boolean {
  return adminRoutes.some((route) => pathname === route || pathname.startsWith(route + '/'));
}

async function fetchFromApi<T>(url: string, token: string): Promise<T | null> {
  try {
    const res = await fetch(url, { headers: { Authorization: `Bearer ${token}` } });
    if (!res.ok) return null;
    const json = await res.json();
    return json?.data ?? null;
  } catch {
    return null;
  }
}

export const onRequest = defineMiddleware(async (context, next) => {
  if (!context.session) return next();

  let auth = await context.session.get<SessionResponse | null>('auth');

  if (auth) {
    const now = Date.now();
    const buffer = 60_000;
    const expiresAt = auth.access_expires_at;

    if (expiresAt <= now) {
      await context.session?.set('auth', undefined);
      return next();
    }

    if (expiresAt <= now + buffer) {
      const { data: refreshed, error } = await context.callAction(actions.auth.refresh, {});
      if (error) {
        await context.session?.set('auth', undefined);
        return next();
      }
      if (refreshed) {
        auth = refreshed;
      }
    }

    const [userData, stateData, profileData, preferencesData] = await Promise.all([
      fetchFromApi<{
        id: string; username: string; email: string; phone_number: string | null;
        role: string; created_at: number; updated_at: number; deleted_at: number | null;
      }>(`${API_BASE_URL}/api/user/${auth.user_id}`, auth.access_token),
      fetchFromApi<{
        verified: boolean; status: string; last_active_at: number; updated_at: number;
      }>(`${API_BASE_URL}/api/user/${auth.user_id}/status`, auth.access_token),
      fetchFromApi<{
        first_name: string | null; last_name: string | null;
        birth_date: number | null; alias: string | null;
        avatar: string | null; bio: string | null;
      }>(`${API_BASE_URL}/api/user/${auth.user_id}/profile`, auth.access_token),
      fetchFromApi<{
        id: string; theme: string; onboarding_completed: boolean; language: string;
      }>(`${API_BASE_URL}/api/user/${auth.user_id}/preferences`, auth.access_token),
    ]);

    context.locals.user = userData
      ? {
          userId: userData.id,
          username: userData.username,
          email: userData.email,
          phoneNumber: userData.phone_number,
          role: userData.role,
          createdAt: userData.created_at,
          updatedAt: userData.updated_at,
          deletedAt: userData.deleted_at,
        }
      : null;

    context.locals.state = stateData
      ? {
          verified: stateData.verified,
          status: stateData.status,
          lastActiveAt: stateData.last_active_at,
          updatedAt: stateData.updated_at,
        }
      : null;

    context.locals.profile = profileData
      ? {
          firstName: profileData.first_name,
          lastName: profileData.last_name,
          birthDate: profileData.birth_date,
          alias: profileData.alias,
          avatar: profileData.avatar,
          bio: profileData.bio,
        }
      : null;

    context.locals.preferences = preferencesData
      ? {
          id: preferencesData.id,
          theme: preferencesData.theme,
          onboarding_completed: preferencesData.onboarding_completed,
          language: preferencesData.language,
        }
      : null;
  }

  const { pathname } = context.url;

  if (
    context.request.method === 'GET' &&
    !isPublicRoute(pathname) &&
    !context.locals.user
  ) {
    return context.redirect('/home');
  }

  if (
    context.locals.user &&
    isAdminRoute(pathname) &&
    context.locals.user.role !== 'ADMIN' &&
    context.locals.user.role !== 'MODERATOR'
  ) {
    return context.redirect('/home');
  }

  const acceptLang = context.request.headers.get('accept-language') || '';
  const preferredLang = acceptLang.split(',')[0]?.split('-')[0] || 'en';
  context.locals.lang = preferredLang === 'es' ? 'es' : 'en';

  return next();
});
