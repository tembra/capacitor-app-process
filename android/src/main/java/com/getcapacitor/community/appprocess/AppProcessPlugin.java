package com.getcapacitor.community.appprocess;

import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import org.json.JSONException;

@CapacitorPlugin(name = "AppProcess")
public class AppProcessPlugin extends Plugin {

  private AppProcess implementation;

  private static class RelaunchOptions {
    boolean shouldRelaunch = false;
    Long cooldown = null;
    Long waitTime = null;
  }

  private RelaunchOptions parseRelaunchOptions(PluginCall call) {
    RelaunchOptions options = new RelaunchOptions();

    JSObject relaunchObj = call.getObject("relaunch");

    if (relaunchObj != null) {
      options.shouldRelaunch = true;

      try {
        options.cooldown = relaunchObj.getLong("cooldown");
      } catch (JSONException exception) {
      }

      try {
        options.waitTime = relaunchObj.getLong("waitTime");
      } catch (JSONException exception) {
      }

      return options;
    }

    boolean relaunchBool = call.getBoolean("relaunch", false);
    options.shouldRelaunch = relaunchBool;

    return options;
  }

  @Override
  public void load() {
    implementation = new AppProcess(getContext(), getActivity());
  }

  @PluginMethod
  public void getPid(PluginCall call) {
    try {
      int pid = implementation.getPid();

      JSObject ret = new JSObject();
      ret.put("pid", pid);

      call.resolve(ret);

    } catch (Exception exception) {
      call.reject("[AppProcess] getPid failed: " + exception.getMessage(), null, exception);
    }
  }

  @PluginMethod
  public void getPssMiB(PluginCall call) {
    try {
      float pssMiB = implementation.getPssMiB();

      JSObject ret = new JSObject();
      ret.put("pssMiB", pssMiB);

      call.resolve(ret);

    } catch (Exception exception) {
      call.reject("[AppProcess] getPssMiB failed: " + exception.getMessage(), null, exception);
    }
  }

  @PluginMethod
  public void softKill(PluginCall call) {
    getActivity().runOnUiThread(() -> {
      try {
        RelaunchOptions options = this.parseRelaunchOptions(call);

        if (options.shouldRelaunch) {
          try {
            implementation.scheduleRelaunch(options.cooldown, options.waitTime);
          } catch (Exception exception) {
            call.reject(
                "[AppProcess] softKill failed. Can not schedule relaunch: "
                    + exception.getMessage(),
                null,
                exception);

            return;
          }
        }

        call.resolve();
        implementation.softKill();

      } catch (Exception exception) {
        call.reject("[AppProcess] softKill failed: " + exception.getMessage(), null, exception);
      }
    });
  }
}
