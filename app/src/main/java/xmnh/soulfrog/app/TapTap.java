package xmnh.soulfrog.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;

public class TapTap implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader) {
        AppUtil.finish(context);
    }

    public static void tapTap(Context context, ClassLoader classLoader) {
        try {
            tapTapLicense(context, classLoader);
        } catch (Throwable e) {
            Log.d(SoulFrog.TAG, "tapTapLicense => ", e);
        }
//        try {
//            tapTapAntiAddict(context, classLoader);
//        } catch (Throwable e) {
//            Log.d(SoulFrog.TAG, "tapTapAntiAddict => ", e);
//        }
        try {
            xdTapTap(context, classLoader);
        } catch (Throwable e) {
            Log.d(SoulFrog.TAG, "xdTapTap => ", e);
        }
        AppUtil.finish(context);
    }

    private static void tapTapLicense(Context context, ClassLoader classLoader) {
        Class<?> tapTapLicense = XposedHelpers.findClassIfExists("com.taptap.pay.sdk.library.TapTapLicense", classLoader);
        Class<?> tapLicense = XposedHelpers.findClassIfExists("com.taptap.sdk.license.TapTapLicense", classLoader);
        if (tapTapLicense == null && tapLicense == null) {
            return;
        }
        long licenseDate = System.currentTimeMillis() - 430000000L;
        context.getSharedPreferences("tap_license", 0)
                .edit()
                .putLong("last_license_date", licenseDate)
                .putLong("last_license_date_second", licenseDate / 1000)
                .putLong("last_purchased_date", licenseDate)
                .apply();
        context.getSharedPreferences("tap_sdk_sp", 0)
                .edit()
                .putLong("last_licensed_date", licenseDate)
                .putLong("last_licensed_date_second", licenseDate / 1000)
                .putLong("last_purchased_date", licenseDate)
                .putLong("last_licensed_date_five_days", licenseDate)
                .apply();
        Class<?> tapPurchase = XposedHelpers.findClassIfExists("com.taptap.pay.sdk.library.TapPurchase", classLoader);
        if (tapPurchase != null) {
            XposedBridge.hookAllConstructors(tapPurchase, new XC_MethodHook() {
                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                    XposedHelpers.setBooleanField(param.thisObject, "isBought", true);
                }
            });
        }
        Class<?> purchaseInfo = XposedHelpers.findClassIfExists("com.taptap.sdk.license.internal.PurchaseInfo", classLoader);
        if (purchaseInfo != null) {
            XposedBridge.hookAllConstructors(purchaseInfo, new XC_MethodHook() {
                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                    XposedHelpers.setBooleanField(param.thisObject, "isBought", true);
                }
            });
        }
        if (tapTapLicense != null) {
            try {
                XposedHelpers.findAndHookMethod(tapTapLicense, "check",
                        Activity.class, Fragment.class, boolean.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                param.args[2] = false;
                            }
                        });
            } catch (Throwable e) {
                Log.d(SoulFrog.TAG, "tapTapLicense check => ", e);
            }
        }
        if (tapLicense != null) {
            XposedHelpers.findAndHookMethod(tapLicense, "checkLicense",
                    Activity.class, boolean.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            param.args[1] = false;
                        }
                    });
        }
        Class<?> tapLicenseInternal = XposedHelpers.findClassIfExists("com.taptap.sdk.license.internal.TapLicenseInternal", classLoader);
        if (tapLicenseInternal != null) {
            XposedHelpers.findAndHookMethod(tapLicenseInternal, "checkLicense",
                    Activity.class, boolean.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            param.args[1] = false;
                        }
                    });
        }
        Class<?> tapLicenseHelper = XposedHelpers.findClassIfExists("com.taptap.pay.sdk.library.TapLicenseHelper", classLoader);
        if (tapLicenseHelper != null) {
            try {
                XposedHelpers.findAndHookMethod(tapLicenseHelper, "setDLCCallback",
                        boolean.class, String.class,
                        classLoader.loadClass("com.taptap.pay.sdk.library.DLCManager$InventoryCallback"),
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                Log.d(SoulFrog.TAG, "methodSignatureBefore =>" + methodSignatureBefore);
                            }
                        });
                XposedHelpers.findAndHookMethod(tapLicenseHelper, "purchaseDLC",
                        android.app.Activity.class, String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                Log.d(SoulFrog.TAG, "methodSignatureBefore =>" + methodSignatureBefore);
                            }

                        });
            } catch (Throwable e) {
                Log.d(SoulFrog.TAG, "tapLicenseHelper setDLCCallback => ", e);
            }
        }
    }

    private static String tapTapAntiAddictSp;

    private static void tapTapAntiAddict(Context context, ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> antiAddictionSettings = XposedHelpers.findClassIfExists("com.tapsdk.antiaddiction.settings.AntiAddictionSettings", classLoader);
        if (antiAddictionSettings != null) {
            try {
                XposedHelpers.findAndHookMethod(antiAddictionSettings, "getSPNameByUserId", String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String result = (String) param.getResult();
                        Log.d(SoulFrog.TAG, "getSPNameByUserId => " + result);
                        if (result != null) {
                            tapTapAntiAddictSp = result;
                        }
                    }
                });
            } catch (NoSuchMethodError e) {
                Log.d(SoulFrog.TAG, "getSPNameByUserId => ", e);
            }
            if (tapTapAntiAddictSp != null) {
                Log.d(SoulFrog.TAG, "tapTapAntiAddictSp => " + tapTapAntiAddictSp);
                SharedPreferences tapAntiSp = context.getSharedPreferences(tapTapAntiAddictSp, Context.MODE_PRIVATE);
//                String userAntiConfigKey = tapAntiSp.getString("userAntiConfigKey", null);
                try {
                    JSONObject jsonObject = new JSONObject("{\"real_name\":{\"age_limit\":18,\"is_adult\":true},\"anti_addiction\":{\"policy_active\":\"time_range\",\"policy_model\":\"server\",\"policy_heartbeat_interval\":120},\"content_rating_check\":{\"allow\":true},\"local\":{\"time_range\":{\"time_start\":\"20:00\",\"time_end\":\"21:00\",\"text\":{\"allow\":{\"title\":\"健康游戏提示\",\"description\":\"你当前为<font color=\\\"#FF8156\\\"><b>未成年账号</b></font>，已被纳入防沉迷系统。根据国家新闻出版署《关于防止未成年人沉迷网络游戏的通知》《关于进一步严格管理 切实防止未成年人沉迷网络游戏的通知》，网络游戏仅可在周五、周六、周日和法定节假日每日 20 时至 21 时向未成年人提供 60 分钟网络游戏服务。今日游戏时间还剩余<font color=\\\"#FF8156\\\"> <b># ${remaining} #</b> </font>分钟。\"},\"reject\":{\"title\":\"健康游戏提示\",\"description\":\"<span color=\\\"#888888\\\">您当前为未成年账号，已被纳入防沉迷系统。根据国家新闻出版署《关于防止未成年人沉迷网络游戏的通知》《关于进一步严格管理 切实防止未成年人沉迷网络游戏的通知》，周五、周六、周日及法定节假日 20 点 -  21 点之外为<font color=\\\"#FF8156\\\"><b>健康保护时段</b></font>。当前时间段无法游玩，请合理安排时间。</span>\"}},\"holidays\":[\"2025-01-01\",\"2025-01-03\",\"2025-01-04\",\"2025-01-05\",\"2026-09-27\",\"2026-10-01\",\"2026-10-02\",\"2026-10-03\",\"2026-10-04\",\"2026-10-05\",\"2026-10-06\",\"2026-10-07\"]}}}");
                    SharedPreferences.Editor editor = tapAntiSp.edit().
                            putString("userAntiConfigKey", jsonObject.toString());
                    editor.apply();
                } catch (JSONException e) {
                    Log.d(SoulFrog.TAG, "userAntiConfigKey => ", e);
                }
            }
        }
        Class<?> userInfo = XposedHelpers.findClassIfExists("com.tapsdk.antiaddiction.entities.UserInfo", classLoader);
        if (userInfo != null) {
            XposedBridge.hookAllConstructors(userInfo, new XC_MethodHook() {
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                    XposedHelpers.setIntField(param.thisObject, "ageLimit", 18);
                    XposedHelpers.setIntField(param.thisObject, "remainTime", 999999999);
                }
            });
        }
        Class<?> antiAddictionUIImpl = XposedHelpers.findClassIfExists("com.tapsdk.antiaddictionui.AntiAddictionUIImpl", classLoader);
        if (antiAddictionUIImpl != null) {
            XposedHelpers.findAndHookMethod(antiAddictionUIImpl,
                    "setAntiAddictionCallback",
                    classLoader.loadClass("com.tapsdk.antiaddictionui.AntiAddictionUICallback"),
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Object arg = param.args[0];
                            if (arg == null) return;
                            String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                            Log.d(SoulFrog.TAG, "methodSignatureBefore =>" + methodSignatureBefore);
                            XposedHelpers.findAndHookMethod(arg.getClass(), "onCallback",
                                    int.class, Map.class,
                                    new XC_MethodHook() {
                                        @Override
                                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                            param.args[0] = 500;
                                        }
                                    });
                        }
                    });
        }
        Class<?> tapAntiAddictionServiceImpl = XposedHelpers.findClassIfExists("com.tapsdk.antiaddictionui.wrapper.TapAntiAddictionServiceImpl", classLoader);
        if (tapAntiAddictionServiceImpl != null) {
            XposedHelpers.findAndHookMethod(tapAntiAddictionServiceImpl, "sendCallbackDataToEngine",
                    int.class, java.util.Map.class, classLoader.loadClass("com.tds.common.bridge.BridgeCallback"),
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.args[0] = 500;
                            Object arg2 = param.args[2];
                            if (arg2 != null) {
                                Log.d(SoulFrog.TAG, "BridgeCallback impl => " + arg2.getClass().getName());
                            }

                        }
                    });
        }
//        Class<?> antiAddictionUIImpl = XposedHelpers.findClassIfExists("com.tapsdk.antiaddictionui.AntiAddictionUIImpl", classLoader);
//        if (antiAddictionUIImpl != null) {
//            XposedHelpers.findAndHookMethod(antiAddictionUIImpl, "startup",
//                    android.app.Activity.class, String.class, boolean.class,
//                    new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            Object arg0 = param.args[0];
//                            Object arg1 = param.args[1];
//                            Object arg2 = param.args[2];
//                            Log.d(SoulFrog.TAG, "startup => " + arg0.getClass().getName() + " " + arg1 + " " + arg2);
//                        }
//
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//                        }
//                    });
//        }
        Log.d(SoulFrog.TAG, "tapTapAntiAddict => end");
    }

    private static void xdTapTap(Context context, ClassLoader classLoader) {
        Class<?> xDSDK = XposedHelpers.findClassIfExists("com.xd.xdsdk.XDSDK", classLoader);
        if (xDSDK == null) return;
        HookUtil.httpUrlConnection((cls, param) -> {
            HookUtil.hookHttpURLConnectionImpl(cls, "authorizations/taptap", hookParam -> {
                String json = "{\"id\":88888888,\"user_id\":888888888,\"access_token\":\"root\",\"scopes\":\"user,sdk\"}";
                InputStream newStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
                hookParam.setResult(newStream);
            });
            HookUtil.hookHttpURLConnectionImpl(cls, "user?access_token=", hookParam -> {
                String json = "{\"id\":\"888888888\",\"name\":\"root\",\"nickname\":null,\"friendly_name\":\"root\",\"created\":1666666666,\"last_login\":0,\"site\":\"9\",\"client_id\":\"root\",\"authoriz_state\":1,\"is_upload_play_log\":1,\"id_card\":\"root\",\"adult_type\":4,\"tmp_to_xd\":true,\"safety\":true,\"privacy_agreement\":0,\"fcm\":0,\"anti_addiction_token\":\"root\",\"phone\":\"18888888888\",\"did\":\"a6b6c6d6e6f6-6666-abcd-6666-a6b6c6d6e6f6\",\"ip\":\"114.114.114.114\"}";
                InputStream newStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
                hookParam.setResult(newStream);
            });
        });
    }

    private static void tapTapAntiAddictionCallback(Class<?> cls, String methodName) {
        if (cls == null) return;
        XposedHelpers.findAndHookMethod(cls, methodName != null ? methodName : "onCallback",
                int.class, Map.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        param.args[0] = 500;
                        Log.d(SoulFrog.TAG, param.thisObject.getClass().getName() + " tapTapAntiAddictionCallback param => "
                                + param.args[0] + " " + param.args[1]);
                    }
                });

    }

    private static void tapAd(ClassLoader classLoader) {
        try {

        } catch (Throwable e) {
            Log.e(SoulFrog.TAG, "tapAd error", e);
        }
    }
}
