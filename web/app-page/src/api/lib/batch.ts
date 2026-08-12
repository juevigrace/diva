export type BatchResult = {
  succeeded: string[];
  failed: { id: string; error: string }[];
};

type BatchResponse = { ok: boolean; json: { message?: string } };

export async function batchOperate(
  ids: string[],
  op: (id: string) => Promise<BatchResponse>,
): Promise<BatchResult> {
  const results = await Promise.allSettled(ids.map((id) => op(id)));

  const succeeded: string[] = [];
  const failed: { id: string; error: string }[] = [];
  for (let i = 0; i < results.length; i++) {
    const r = results[i];
    if (r.status === 'fulfilled' && r.value.ok) {
      succeeded.push(ids[i]);
    } else {
      const msg = r.status === 'fulfilled' ? r.value.json.message || 'request failed' : (r.reason as Error)?.message || 'unknown error';
      failed.push({ id: ids[i], error: msg });
    }
  }
  return { succeeded, failed };
}
