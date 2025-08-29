import { WebPlugin } from '@capacitor/core';

import type { AppProcessPlugin, PidResult, PssMiBResult } from './definitions';

export class AppProcessWeb extends WebPlugin implements AppProcessPlugin {
  async getPid(): Promise<PidResult> {
    throw this.unavailable('AppProcess API not available for web');
  }

  async getPssMiB(): Promise<PssMiBResult> {
    throw this.unavailable('AppProcess API not available for web');
  }

  async softKill(): Promise<void> {
    throw this.unavailable('AppProcess API not available for web');
  }
}
