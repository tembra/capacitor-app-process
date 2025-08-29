import { WebPlugin } from '@capacitor/core';

import type { AppProcessPlugin } from './definitions';

export class AppProcessWeb extends WebPlugin implements AppProcessPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
