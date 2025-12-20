package xmnh.soulfrog.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
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

    private static String argsFormat(XC_MethodHook.MethodHookParam param) {
        Member method = param.method;
        Class<?>[] parameterTypes = new Class<?>[0];
        if (method instanceof Method) {
            parameterTypes = ((Method) method).getParameterTypes();
        } else if (method instanceof Constructor) {
            parameterTypes = ((Constructor<?>) method).getParameterTypes();
        }
        if (parameterTypes.length == 0) {
            return "no args";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            sb.append(parameterTypes[i].getName()).append(" : ").append(param.args[i]).append(",\n");
        }
        return sb.toString();
    }

    public static String getMethodSignatureBefore(XC_MethodHook.MethodHookParam param) {
        if (param == null) return "";
        Object[] args = param.args;
        Object thisObject = param.thisObject;
        StringBuilder sb = new StringBuilder();
        sb.append("\n")
                .append("================================\n")
                .append("[before hook]\n")
                .append("--------------------------------\n")
                .append("method: ").append(param.method.getName()).append("\n")
                .append("args: ").append(argsFormat(param)).append("\n")
                .append("thisObject: ").append(thisObject != null ? thisObject.getClass().getName() : "null")
                .append("\n================================\n")
        ;
        return sb.toString();
    }

    public static String getMethodSignatureAfter(XC_MethodHook.MethodHookParam param) {
        if (param == null) return "";
        Object[] args = param.args;
        Object thisObject = param.thisObject;
        Object result = param.getResult();
        StringBuilder sb = new StringBuilder();
        sb.append("\n================================\n")
                .append("[after hook]\n")
                .append("--------------------------------\n")
                .append("method: ").append(param.method.getName()).append("\n")
                .append("args: ").append(argsFormat(param)).append("\n")
                .append("thisObject: ").append(thisObject != null ? thisObject.getClass().getName() : "null").append("\n")
                .append("result: ").append(result)
                .append("\n================================\n")
        ;
        return sb.toString();
    }

    public static Set<Class<?>> httpURLConnectionImpl = new HashSet<>();

    public static void httpUrlConnection(BiConsumer<Class<?>, XC_MethodHook.MethodHookParam> consumer) {
        XposedHelpers.findAndHookMethod(URL.class, "openConnection", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object result = param.getResult();
                if (result instanceof HttpURLConnection connection) {
                    Class<?> implClass = connection.getClass();
                    if (!httpURLConnectionImpl.contains(implClass)) {
                        Log.d(SoulFrog.TAG, "Found and hooked httpURLConnectionImpl: " + implClass.getName());
                        consumer.accept(implClass, param);
                        httpURLConnectionImpl.add(implClass);
                    }
                }
            }
        });
    }

    public static void hookHttpURLConnectionImpl(Class<?> implClass, String targetUrl,
                                                 Consumer<XC_MethodHook.MethodHookParam> consumer) {
        XposedHelpers.findAndHookMethod(implClass, "getResponseCode", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Object result = param.getResult();
                HttpURLConnection conn = (HttpURLConnection) param.thisObject;
//                Log.d(SoulFrog.TAG, conn.getURL() + " origin getResponseCode: " + result);
                if (isTargetUrl(conn.getURL().toString(), targetUrl)) {
                    Log.d(SoulFrog.TAG, "Status Code modified to 200 for: " + conn.getURL());
                    param.setResult(200);
                }
            }
        });
        XposedHelpers.findAndHookMethod(implClass, "getInputStream", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                HttpURLConnection conn = (HttpURLConnection) param.thisObject;
//                InputStream result = (InputStream) param.getResult();
//                Log.d(SoulFrog.TAG, " ====== start ====== ");
//                Log.d(SoulFrog.TAG, conn.getURL() + " getInputStream ");
//                Log.d(SoulFrog.TAG, "InputStream response: " + readStreamToString(result));
//                Log.d(SoulFrog.TAG, " ====== end ====== ");
                if (isTargetUrl(conn.getURL().toString(), targetUrl)) {
                    Log.d(SoulFrog.TAG, "Intercepting InputStream for: " + conn.getURL());
                    consumer.accept(param);
                }
            }
        });
        XposedHelpers.findAndHookMethod(implClass, "getErrorStream", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                HttpURLConnection conn = (HttpURLConnection) param.thisObject;
//                InputStream result = (InputStream) param.getResult();
//                Log.d(SoulFrog.TAG, " ====== start ====== ");
//                Log.d(SoulFrog.TAG, conn.getURL() + " getErrorStream ");
//                Log.d(SoulFrog.TAG, "getErrorStream response: " + readStreamToString(result));
//                Log.d(SoulFrog.TAG, " ====== end ====== ");
                if (isTargetUrl(conn.getURL().toString(), targetUrl)) {
                    Log.d(SoulFrog.TAG, "Intercepting getErrorStream for: " + conn.getURL());
                    consumer.accept(param);
                }
            }
        });
    }

    private static boolean isTargetUrl(String url, String targetUrl) {
        if (url == null) return false;
        return url.contains(targetUrl);
    }

    private static String readStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    public static void taptapOkhttp(ClassLoader classLoader) {
    }
}