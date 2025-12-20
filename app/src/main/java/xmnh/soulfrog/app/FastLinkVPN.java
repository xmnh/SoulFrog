package xmnh.soulfrog.app;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
import xmnh.soulfrog.enums.AppEnum;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.StringUtil;

public class FastLinkVPN implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader) {
        AppUtil.sp = context.getSharedPreferences("vip", MODE_PRIVATE);
        if (AppUtil.checkSp(StringUtil.randomText(16, false), 0)) {
            AppUtil.resetDataDir(context, AppEnum.FAST_LINK_VPN.getPackageName());
            AppUtil.sp.edit().putString("inviter", "201271814").apply();
        }
        getDeviceId();

        AppUtil.finish(context);
    }

    private void getDeviceId() {
        XposedHelpers.findAndHookMethod(Settings.System.class, "getString",
                ContentResolver.class, String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String arg = (String) param.args[1];
                        if ("android_id".equals(arg)) {
                            Log.d(SoulFrog.TAG, "device_id: " + AppUtil.sp.getString("device_id", ""));
                            param.setResult(AppUtil.sp.getString("device_id", ""));
                        }
                    }
                });
    }
}
