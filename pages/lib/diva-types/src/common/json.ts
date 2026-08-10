export type CamelToSnake<S extends string> = S extends `${infer T}${infer Rest}`
  ? `${T extends Uppercase<T> ? `_${Lowercase<T>}` : T}${CamelToSnake<Rest>}`
  : S;

export type Json<T> = {
  [K in keyof T as CamelToSnake<K & string>]: T[K];
};
