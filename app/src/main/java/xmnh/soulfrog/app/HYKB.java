package xmnh.soulfrog.app;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;

public class HYKB implements BaseHook {
    static String getGameId;

    @Override
    public void hook(Context context, ClassLoader classLoader) {

    }

    public static void hykb(Context context, ClassLoader classLoader) {
        Class<?> hykbPaidChecker = XposedHelpers.findClassIfExists("com.m3839.sdk.paid.HykbPaidChecker", classLoader);
        Class<?> hykbDLC = XposedHelpers.findClassIfExists("com.m3839.sdk.dlc.HykbDLC", classLoader);
        if (hykbPaidChecker == null && hykbDLC == null) {
            return;
        }

        Class<?> commonMananger = XposedHelpers.findClassIfExists("com.m3839.sdk.common.CommonMananger", classLoader);
        if (commonMananger != null) {
            XposedHelpers.findAndHookMethod(commonMananger, "getGameId", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (getGameId != null) return;
                    String result = (String) param.getResult();
                    if (result != null) {
                        getGameId = result;
                        Log.d(SoulFrog.TAG, "getGameId => " + result);
                    }
                }
            });
        }

        if (hykbPaidChecker != null) {
//            HookUtil.httpUrlConnection((cls, param) -> {
//                Log.d(SoulFrog.TAG, "Intercepting HttpURLConnection for: " + cls.getName());
//                HookUtil.hookHttpURLConnectionImpl(cls, "https://api.hykb.com/api/v1/license/check", hookParam -> {
//
//                });
//            });
            context.getSharedPreferences("hykb_paid_check", Context.MODE_PRIVATE)
                    .edit()
                    .putString("expire_time", Base64.encodeToString("2099-12-31 23:59".getBytes(), Base64.DEFAULT))
                    .apply();
            Class<?> hykbCheckListener = XposedHelpers.findClassIfExists("com.m3839.sdk.paid.HykbCheckListener", classLoader);
            Class<?> httpLoader = XposedHelpers.findClassIfExists("com.m3839.sdk.common.http.loader.HttpLoader", classLoader);
            if (httpLoader != null) {
//                try {
//                    XposedHelpers.findAndHookMethod(httpLoader, "requestGet",
//                            String.class, java.util.Map.class, java.util.Map.class,
//                            classLoader.loadClass("com.m3839.sdk.common.http.listener.OnHttpRequestListener"),
//                            new XC_MethodHook() {
//                                @Override
//                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                    String arg0 = (String) param.args[0];
//                                    Object arg3 = param.args[3];
//                                    if (arg3 != null) {
//                                        String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
//                                            XposedHelpers.findAndHookMethod(arg3.getClass(), "onResponseSuccess",
//                                                    String.class,
//                                                    new XC_MethodHook() {
//                                                        @Override
//                                                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                                            Object thisObject = param.thisObject;
//                                                            String arg0 = (String) param.args[0];
//                                                            Log.d(SoulFrog.TAG, "onResponseSuccess arg0 => " + arg0);
//                                                        }
//                                                    }
//                                            );
//                                        } catch (Throwable e) {
//                                            Log.d(SoulFrog.TAG, "requestGet onResponseSuccess error => " + e.getMessage());
//                                        }
//                                        try {
//                                            XposedHelpers.findAndHookMethod(arg3.getClass(), "onResponseError",
//                                                    int.class, String.class,
//                                                    new XC_MethodHook() {
//                                                        @Override
//                                                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                                            Object thisObject = param.thisObject;
//                                                            int arg0 = (int) param.args[0];
//                                                            String arg1 = (String) param.args[1];
//                                                            Log.d(SoulFrog.TAG, "onResponseError arg0 => " + arg0 + " arg1 => " + arg1);
//                                                        }
//                                                    }
//                                            );
//                                        } catch (Throwable e) {
//                                            Log.d(SoulFrog.TAG, "requestGet onResponseError error => " + e.getMessage());
//                                        }
//                                    }
//                                }
//                            });
//                } catch (Throwable e) {
//                    Log.d(SoulFrog.TAG, "requestGet error => " + e.getMessage());
//                }
            }
            try {
                XposedHelpers.findAndHookMethod(hykbPaidChecker, "checkLicense",
                        android.app.Activity.class, String.class, String.class, int.class, hykbCheckListener,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                Object thisObject = param.thisObject;
                                String arg1 = (String) param.args[1];
                                String arg2 = (String) param.args[2];
                                int arg3 = (int) param.args[3];
                                Object listener = param.args[4];
                                String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                Log.d(SoulFrog.TAG, "methodSignatureBefore => " + methodSignatureBefore);
                                if (listener != null) {
                                    XposedHelpers.findAndHookMethod(listener.getClass(), "onReject",
                                            int.class, String.class,
                                            new XC_MethodReplacement() {
                                                @Override
                                                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                                    Object thisObject = param.thisObject;
                                                    String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                                    Log.d(SoulFrog.TAG, "methodSignatureBefore => " + methodSignatureBefore);
                                                    XposedHelpers.callMethod(thisObject, "onAllowEnter");
                                                    return null;
                                                }
                                            });
                                }
                            }
                        });
            } catch (Throwable e) {
                Log.d(SoulFrog.TAG, "checkLicense error => " + e.getMessage());
            }
            Class<?> preferencesUtils = XposedHelpers.findClassIfExists("com.m3839.sdk.common.util.PreferencesUtils", classLoader);
            if (preferencesUtils != null) {
                XposedHelpers.findAndHookMethod(preferencesUtils, "savePaidType",
                        "android.content.Context", int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                param.args[1] = 1;
                            }

                        });
            }
            try {
                XposedHelpers.findAndHookMethod(hykbPaidChecker, "checkLicense",
                        android.app.Activity.class, String.class, String.class, hykbCheckListener,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                Object arg0 = param.args[0];
                                String arg1 = (String) param.args[1];
                                String arg2 = (String) param.args[2];
                                Object listener = param.args[3];
                                Object thisObject = param.thisObject;
                                String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                Log.d(SoulFrog.TAG, "methodSignatureBefore => " + methodSignatureBefore);
                                XposedHelpers.findAndHookMethod(listener.getClass(), "onReject",
                                        int.class, String.class,
                                        new XC_MethodReplacement() {
                                            @Override
                                            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                                Object listenerThis = param.thisObject;
                                                Object arg0 = param.args[0];
                                                Object arg1 = param.args[1];
                                                String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                                Log.d(SoulFrog.TAG, "methodSignatureBefore => " + methodSignatureBefore);
                                                return XposedHelpers.callMethod(listenerThis, "onAllowEnter");
                                            }
                                        });

                            }
                        });
            } catch (Throwable e) {
                Log.d(SoulFrog.TAG, "checkLicense2 error => " + e.getMessage());
            }
            try {

            } catch (Throwable e) {
                Log.d(SoulFrog.TAG, " error => " + e.getMessage());
            }
        }
        if (hykbDLC != null) {
            Class<?> hykbDLCSkuStatusInfo = XposedHelpers.findClassIfExists("com.m3839.sdk.dlc.bean.HykbDLCSkuStatusInfo", classLoader);
            if (hykbDLCSkuStatusInfo != null) {
                XposedBridge.hookAllConstructors(hykbDLCSkuStatusInfo, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedHelpers.setIntField(param.thisObject, "status", 1);
                    }
                });
            }
            try {
                XposedHelpers.findAndHookMethod(hykbDLC, "query",
                        android.app.Activity.class, int.class, classLoader.loadClass("com.m3839.sdk.dlc.listener.HykbDLCQueryListener"),
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                int arg1 = (int) param.args[1];
                                Object arg2 = param.args[2];
                                String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                Log.d(SoulFrog.TAG, "methodSignatureBefore => " + methodSignatureBefore);
                                XposedHelpers.findAndHookMethod(arg2.getClass(), "onSucceed",
                                        int.class,
                                        new XC_MethodHook() {
                                            @Override
                                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                                param.args[0] = 1;
                                            }
                                        }
                                );
                            }
                        });
                XposedHelpers.findAndHookMethod(hykbDLC, "queryAll",
                        android.app.Activity.class, classLoader.loadClass("com.m3839.sdk.dlc.listener.HykbDLCQueryAllListener"),
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                Object arg1 = param.args[1];
                                String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                Log.d(SoulFrog.TAG, "methodSignatureBefore => " + methodSignatureBefore);
                                XposedHelpers.findAndHookMethod(arg1.getClass(), "onQueryAll", "onSucceed",
                                        classLoader.loadClass("com.m3839.sdk.dlc.bean.HykbDLCSkuStatusData"),
                                        new XC_MethodHook() {
                                            @Override
                                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                                Object args = param.args[0];
                                                Log.d(SoulFrog.TAG, "queryAll listener args => " + args);
                                            }
                                        });
                            }
                        });
                Log.d(SoulFrog.TAG, "queryAll end");
            } catch (Throwable e) {
                Log.d(SoulFrog.TAG, "queryAll error => " + e.getMessage());
            }
        }
        if (getGameId != null) {
            context.getSharedPreferences("hykb_login_info" + getGameId, Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("from_paid", true)
                    .putInt("paid-type", 1)
                    .apply();
        }


        AppUtil.finish(context);
    }

    private static void hykbPay(ClassLoader classLoader){
        Class<?> hykbPay = XposedHelpers.findClassIfExists("com.m3839.sdk.pay.HykbPay", classLoader);
        if (hykbPay != null) {
            try {
                XposedHelpers.findAndHookMethod(hykbPay, "pay",
                        android.app.Activity.class,
                        classLoader.loadClass("com.m3839.sdk.pay.bean.HykbPayInfo"),
                        classLoader.loadClass("com.m3839.sdk.pay.listener.HykbPayListener"),
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                Object arg1 = param.args[1];
                                Object arg2 = param.args[2];
                                String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                Log.d(SoulFrog.TAG, "pay methodSignatureBefore " + methodSignatureBefore);
                                XposedHelpers.findAndHookMethod(arg2.getClass(), "onPayResult",
                                        int.class, String.class,
                                        classLoader.loadClass("com.m3839.sdk.pay.bean.HykbPayResult"),
                                        new XC_MethodHook() {
                                            @Override
                                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                                param.args[0] = 9000;
                                            }
                                        }
                                );
                            }

                        });
            } catch (Throwable e) {
                Log.d(SoulFrog.TAG, "pay v1 error => " + e.getMessage());
            }

            try {
                XposedHelpers.findAndHookMethod(hykbPay, "pay",
                        android.app.Activity.class,
                        classLoader.loadClass("com.m3839.sdk.pay.bean.HykbPayInfo"),
                        classLoader.loadClass("com.m3839.sdk.pay.listener.HykbV2PayListener"),
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                Object arg1 = param.args[1];
                                Object arg2 = param.args[2];
                                String methodSignatureBefore = HookUtil.getMethodSignatureBefore(param);
                                Log.d(SoulFrog.TAG, "pay methodSignatureBefore " + methodSignatureBefore);
                                XposedHelpers.findAndHookMethod(arg2.getClass(), "onFailed",
                                        classLoader.loadClass("com.m3839.sdk.pay.bean.HykbPayInfo"),
                                        int.class, String.class,
                                        new XC_MethodReplacement() {
                                            @Override
                                            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                                Object arg0 = param.args[0];
                                                int arg1 = (int) param.args[1];
                                                String arg2 = (String) param.args[2];
                                                String beforeLog = HookUtil.getMethodSignatureBefore(param);
                                                Log.d(SoulFrog.TAG, "onFailed => " + beforeLog);
                                                if (arg1 == 3001 || arg1 == 6001) {
                                                    Object onSucceed = XposedHelpers.callMethod(param.thisObject, "onSucceed", arg0);
                                                    Log.d(SoulFrog.TAG, "onSucceed result => " + onSucceed);
                                                }
                                                return returnConstant(null);
                                            }
                                        }
                                );
                            }
                        });
            } catch (Throwable e) {
                Log.d(SoulFrog.TAG, "pay error => " + e.getMessage());
            }
        }
    }
}
