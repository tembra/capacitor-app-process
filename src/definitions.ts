interface PidResult {
  pid: number;
}

interface PssMiBResult {
  pssMiB: number;
}

interface SoftKillOptions {
  relaunch?:
    | boolean
    | {
        cooldown?: number; // ms, default 60_000
        waitTime?: number; // ms, default 500
      };
}

interface AppProcessPlugin {
  getPid(): Promise<PidResult>;
  getPssMiB(): Promise<PssMiBResult>;
  softKill(options?: SoftKillOptions): Promise<void>;
}

export { PidResult, PssMiBResult, SoftKillOptions, AppProcessPlugin };
