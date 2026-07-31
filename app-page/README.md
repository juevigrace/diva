# app-page

Astro application for the Diva platform: landing, marketing, documentation, and the authenticated dashboard (users, sessions, devices, permissions, audit log).

## Requirements

- Node >= 22.12 or Bun >= 1.2
- The backend (`diva-server`) running at `API_BASE_URL` (default `http://localhost:5001`, configurable via `astro:env/server`)

## Development

```sh
bun install
bun run dev
```

The dev server runs in background mode. Manage it with:

```sh
bun run stop    # astro dev stop
bun run status  # astro dev status
bun run logs    # astro dev logs
```

## Commands

| Command            | Action                                        |
| :----------------- | :-------------------------------------------- |
| `bun run build`    | Build the production site to `dist/`          |
| `bun run preview`  | Preview the production build locally          |
| `bun run lint`     | Run ESLint (from the repo root)               |
| `bun run format`   | Run Prettier (from the repo root)             |

## Structure

- `src/pages/` — routes: landing/docs (public), app pages (authenticated), `api/` proxy routes to the backend
- `src/components/` — `.astro` pages/components and React components (`client:*`)
- `src/layouts/` — base and app layouts
- `src/lib/` — i18n (`t.ts` server-side, `useT.ts` React), API helpers
- `src/middleware.ts` — session/auth refresh, route gating
- `src/actions/` — Astro server actions (session, auth)

Shared UI components and types live in the `lib/diva-ui` and `lib/diva-types` workspaces.
