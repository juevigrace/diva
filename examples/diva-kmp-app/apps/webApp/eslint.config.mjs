// @ts-check

import eslint from '@eslint/js';
import tseslint from 'typescript-eslint';
import prettierEslint from 'eslint-plugin-prettier/recommended';

export default tseslint.config(
  eslint.configs.recommended,
  tseslint.configs.strictTypeChecked,
  tseslint.configs.stylisticTypeChecked,
  prettierEslint
);
