import { registerPlugin } from '@capacitor/core';

import type { AppProcessPlugin } from './definitions';

const AppProcess = registerPlugin<AppProcessPlugin>('AppProcess', {
  web: () => import('./web').then((m) => new m.AppProcessWeb()),
});

export * from './definitions';
export { AppProcess };
