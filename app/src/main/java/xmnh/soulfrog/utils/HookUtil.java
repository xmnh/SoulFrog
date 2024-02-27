package xmnh.soulfrog.utils;

import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;

public class HookUtil {
    /**
     * 通过反射类,找到方法返回类型为void和boolean的所有方法
     *
     * @param cls  类
     * @param bool true/false
     */
    public static void booleAndVoidReplace(Class<?> cls, boolean bool) {
        if (cls == null) return;
        for (Method method : cls.getDeclaredMethods()) {
            if (Modifier.isNative(method.getModifiers())) return;
            if (method.getReturnType() == boolean.class
                    || method.getReturnType() == Boolean.class) {
                XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(bool));
            } else if (method.getReturnType() == void.class) {
                XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(null));
            }
        }
    }


    /**
     * 通过反射类,找到方法返回类型为boolean的所有方法
     *
     * @param cls  类
     * @param bool true/false
     */
    public static void booleReplace(Class<?> cls, boolean bool) {
        if (cls == null) return;
        for (Method method : cls.getDeclaredMethods()) {
            if (method.getModifiers() != Modifier.NATIVE) {
                if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(bool));
                }
            }
        }
    }

    /**
     * 查找okhttp3
     */
    public static void findOkHttpResponseBody(ClassLoader classLoader, Consumer<XC_MethodHook.MethodHookParam> consumer) {
        Class<?> cls = XposedHelpers.findClass("okhttp3.Response", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "isSuccessful", XC_MethodReplacement.returnConstant(true));
        }
        Class<?> cls2 = XposedHelpers.findClass("okhttp3.ResponseBody", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls2, "string", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    consumer.accept(param);
                }
            });
        }
    }

    public static void findOkhttpRequest(ClassLoader classLoader, String url) {
        Class<?> cls = XposedHelpers.findClass("okhttp3.Request$Builder", classLoader);
        Class<?> httpUrl = XposedHelpers.findClass("okhttp3.HttpUrl", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "url", httpUrl, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (url != null && httpUrl != null) {
                        param.args[0] = url;
                        Object getUrl = XposedHelpers.callMethod(param.args[0], "get", String.class);

                    }

                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (url != null && httpUrl != null) {
//                        param.args[0] = url;
//                        if ()
//                        Object getUrl = XposedHelpers.callMethod(param.args[0], "get", String.class);

                    }
                }
            });
        }
        Class<?> cls2 = XposedHelpers.findClass("okhttp3.ResponseBody", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls2, "string", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.d(SoulFrog.TAG + " find okhttp3 ", param.getResult().toString());
                }
            });
        }
    }

    /**
     * 查找原生jsonObject
     */
    public static void findJsonObject(ClassLoader classLoader) {
        Class<?> cls = XposedHelpers.findClass("org.json.JSONObject", classLoader);
        if (null == cls) return;
        XposedHelpers.findAndHookConstructor(JSONObject.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.d(SoulFrog.TAG + " find constructor ", param.args[0].toString());
            }
        });
        XposedHelpers.findAndHookMethod(JSONObject.class, "toString", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (StringUtil.containsIgnoreCase(param.getResult().toString(), "vip")) {
                    Log.d(SoulFrog.TAG + " find vip json ", param.getResult().toString());
                }
            }
        });
    }

    /**
     * 查找gson
     */
    public static void findGson(ClassLoader classLoader) {
        Class<?> cls = XposedHelpers.findClass("com.google.gson.Gson", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "fromJson", String.class, Class.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.d(SoulFrog.TAG + " find gson ", param.args[0].toString());
                }
            });
        }
    }

    /**
     * 查找fastjson
     */
    public static void findFastjson(ClassLoader classLoader) {
        Class<?> cls = XposedHelpers.findClass("com.alibaba.fastjson.JSONObject", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "toJavaObject", Class.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.d(SoulFrog.TAG + " find fastjson ", param.args[0].toString());
                }
            });
        }
    }

    /**
     * param.args[0]为String.class , param.args[1]Class.class
     *
     * @param consumer MethodHookParam
     */
    public static void gsonFromJson(ClassLoader classLoader, Consumer<XC_MethodHook.MethodHookParam> consumer) {
        Class<?> cls = XposedHelpers.findClass("com.google.gson.Gson", classLoader);
        if (null == cls) return;
        try {
            XposedHelpers.findAndHookMethod(cls, "fromJson", String.class, Class.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    consumer.accept(param);
                }
            });
        } catch (Throwable e) {
            // public <T> T fromJson(String json, Class<T> classOfT)
            // 找到类了但方法名被混淆了，会抛出NoSuchMethodError则进行动态查找
            for (Method method : cls.getDeclaredMethods()) {
                if (method.getReturnType() == Object.class
                        && method.getParameterTypes().length == 2
                        && method.getParameterTypes()[0] == String.class
                        && method.getParameterTypes()[1] == Class.class) {
                    XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            consumer.accept(param);
                        }
                    });
                }
            }
        }
    }

    public static void gsonToJson(ClassLoader classLoader, Consumer<XC_MethodHook.MethodHookParam> consumer) {
        Class<?> cls = XposedHelpers.findClass("com.google.gson.Gson", classLoader);
        if (null == cls) return;
        try {
            XposedHelpers.findAndHookMethod(cls, "toJson", Object.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    consumer.accept(param);
                }
            });
        } catch (Throwable e) {
            // public String toJson(Object src)
            // 找到类了但方法名被混淆了，会抛出NoSuchMethodError则进行动态查找
            for (Method method : cls.getDeclaredMethods()) {
                if (method.getReturnType() == String.class
                        && method.getParameterTypes().length == 1
                        && method.getParameterTypes()[0] == Object.class) {
                    XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            consumer.accept(param);
                        }
                    });
                }
            }
        }
    }

    public static String printStackTrace() {
        StringBuilder sb = new StringBuilder();
        Throwable throwable = new Throwable();
        StackTraceElement[] stackElements = throwable.getStackTrace();
        for (StackTraceElement element : stackElements) {
            sb.append("at ")
                    .append(element.getClassName()).append(".").append(element.getMethodName())
                    .append("(")
                    .append(element.getFileName()).append(":").append(element.getLineNumber())
                    .append(")")
                    .append("\n");
        }
        return sb.toString();
    }

    public static void tapTap(ClassLoader classLoader) {
        Class<?> antiAddictionUIKit = XposedHelpers.findClass("com.tapsdk.antiaddictionui.AntiAddictionUIKit", classLoader);
        if (antiAddictionUIKit != null) {
            XposedBridge.hookAllMethods(antiAddictionUIKit, "init",XC_MethodReplacement.returnConstant(null));
        }
    }
}
