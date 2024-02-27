package xmnh.soulfrog.app;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
import xmnh.soulfrog.enums.AppEnum;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;
import xmnh.soulfrog.utils.StringUtil;

public class FastLinkVPN implements BaseHook {
    //    private SharedPreferences sp;
    @Override
    public void hook(Context context, ClassLoader classLoader) {
//        String appVersionName = AppUtil.getAppVersionName(context);
        AppUtil.sp = context.getSharedPreferences("vip", MODE_PRIVATE);
        if (AppUtil.checkSp(StringUtil.randomText(16, false), 0)) {
            AppUtil.resetDataDir(context, AppEnum.FAST_LINK_VPN.getPackageName());
            AppUtil.sp.edit().putString("inviter", "201271814").apply();
            getDeviceId();
        }
        // DeviceInfoUtils.kt
/*        switch (appVersionName) {
            case "2.18.6":
                getDeviceId(classLoader, "u3.a.a.a.a1.b2", "d");
                return;
            case "2.18.10":
            case "2.18.12":
                getDeviceId(classLoader, "cs.m", "f");
                return;
            case "2.18.16":
                getDeviceId(classLoader, "ds.m", "f");
                return;
            case "2.18.18":
                getDeviceId(classLoader, "fs.l", "d");
                return;
            case "2.18.20":
                getDeviceId(classLoader, "gs.l", "d");
                return;
            default:
                getDeviceId();
        }*/
        AppUtil.finish(context);
    }

/*    private void getDeviceId(ClassLoader classLoader, String className, String methodName) {
        Class<?> cls2 = XposedHelpers.findClass(className, classLoader);
        if (cls2 != null) {
            XposedHelpers.findAndHookMethod(cls2, methodName,
                    XC_MethodReplacement.returnConstant(AppUtil.sp.getString("device_id", "")));
        }
    }*/

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

    private void referrer(ClassLoader classLoader) {
        HookUtil.gsonToJson(classLoader, param -> {
            String result = (String) param.getResult();
            if (!StringUtil.isEmpty(result)) {
                Log.d(SoulFrog.TAG , "referrer: " + result);
            }
        });
        Class<?> homeActivity = XposedHelpers.findClass("world.letsgo.booster.android.pages.home.HomeActivity", classLoader);
        if (homeActivity == null) return;
/*        XposedHelpers.findAndHookConstructor(homeActivity, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
*//*                HookUtil.gsonToJson(classLoader, param -> {
                    String result = (String)param.getResult();
                    if (!StringUtil.isEmpty(result)){

                    }
                });*//*
                try {
                    Activity activity = (Activity) methodHookParam.thisObject;
                    if (activity == null || !"world.letsgo.booster.android.pages.home.HomeActivity".equals(activity.getClass().getName())) {
                        return;
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Class<?> onClickClass = XposedHelpers.findClass("gr.c", classLoader);
                        Object clazzObj = XposedHelpers.newInstance(onClickClass);
                        XposedHelpers.callMethod(clazzObj, "P", AppUtil.sp.getString("inviter",""));

                    }, 3000L);
                } catch (Throwable e) {
                    XposedBridge.log(SoulFrog.TAG + " ==> " + e.getMessage());
                }
            }
        });*/
    }


/*    private boolean checkNeedUpdate() {
        long currentTime = System.currentTimeMillis();
        if (!sp.contains("expire_time") || currentTime >= sp.getLong("expire_time", 0)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("device_id", StringUtil.randomText(16, false));
            editor.putLong("expire_time", currentTime + 2_400_000);
//            editor.putLong("expire_time", System.currentTimeMillis() + 2_400_000 + 259_200_000);
            editor.apply();
            return true;
        }
        return false;
    }*/
}
