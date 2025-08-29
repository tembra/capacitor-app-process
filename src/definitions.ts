export interface AppProcessPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
