import { defineConfig, envField } from 'astro/config';

import tailwindcss from '@tailwindcss/vite';

import node from '@astrojs/node';

import react from '@astrojs/react';

export default defineConfig({
  output: 'server',

  vite: {
    plugins: [tailwindcss()],
  },

  adapter: node({
    mode: 'standalone',
  }),

  env: {
    schema: {
      BASE_URL: envField.string({
        context: 'server',
        access: 'secret',
        optional: false,
      }),
      SERVER_URL: envField.string({
        context: 'server',
        access: 'secret',
        optional: false,
      }),
    },
    validateSecrets: true,
  },

  integrations: [react()],
});
