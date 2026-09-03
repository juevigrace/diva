import { defineConfig } from 'astro/config';
import tailwindcss from '@tailwindcss/vite';
import react from '@astrojs/react';
import sitemap from '@astrojs/sitemap';
import node from '@astrojs/node';

export default defineConfig({
  site: 'https://example.com',
  output: 'server',
  server: { port: 4321, host: true },
  adapter: node({ mode: 'standalone' }),
  integrations: [react(), sitemap()],
  vite: {
    ssr: { noExternal: ['diva-ui'] },
    plugins: [tailwindcss()],
  },
});
