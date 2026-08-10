import { API_BASE_URL } from 'astro:env/server';
import type { APIResponse } from 'diva-types/common/responses/api-response';

interface FetchOptions {
  method?: string;
  body?: unknown;
  token?: string;
  formData?: FormData;
}

export async function apiFetch<T>(endpoint: string, options: FetchOptions = {}): Promise<{ status: number; ok: boolean; json: APIResponse<T> }> {
  const headers: Record<string, string> = {};
  if (!options.formData) {
    headers['Content-Type'] = 'application/json';
  }
  if (options.token) {
    headers['Authorization'] = `Bearer ${options.token}`;
  }
  const init: RequestInit = { method: options.method || 'GET', headers };
  if (options.body !== undefined) {
    init.body = JSON.stringify(options.body);
  }
  if (options.formData) {
    init.body = options.formData;
  }
  const res = await fetch(`${API_BASE_URL}${endpoint}`, init);
  const text = await res.text();
  let json: APIResponse<T> = {} as APIResponse<T>;
  if (text) {
    try {
      json = JSON.parse(text) as APIResponse<T>;
    } catch {
      json = { data: null as T, message: text, time: Date.now() };
    }
  }
  return { status: res.status, ok: res.ok, json };
}
