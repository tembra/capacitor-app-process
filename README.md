# capacitor-app-process

Gives information about app process and the possibility to kill it and relaunch the app

## Install

```bash
npm install capacitor-app-process
npx cap sync
```

## API

<docgen-index>

* [`getPid()`](#getpid)
* [`getPssMiB()`](#getpssmib)
* [`softKill(...)`](#softkill)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getPid()

```typescript
getPid() => Promise<PidResult>
```

**Returns:** <code>Promise&lt;<a href="#pidresult">PidResult</a>&gt;</code>

--------------------


### getPssMiB()

```typescript
getPssMiB() => Promise<PssMiBResult>
```

**Returns:** <code>Promise&lt;<a href="#pssmibresult">PssMiBResult</a>&gt;</code>

--------------------


### softKill(...)

```typescript
softKill(options?: SoftKillOptions | undefined) => Promise<void>
```

| Param         | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`options`** | <code><a href="#softkilloptions">SoftKillOptions</a></code> |

--------------------


### Interfaces


#### PidResult

| Prop      | Type                |
| --------- | ------------------- |
| **`pid`** | <code>number</code> |


#### PssMiBResult

| Prop         | Type                |
| ------------ | ------------------- |
| **`pssMiB`** | <code>number</code> |


#### SoftKillOptions

| Prop           | Type                                                              |
| -------------- | ----------------------------------------------------------------- |
| **`relaunch`** | <code>boolean \| { cooldown?: number; waitTime?: number; }</code> |

</docgen-api>
