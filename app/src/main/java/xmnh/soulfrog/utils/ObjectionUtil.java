package xmnh.soulfrog.utils;

import static de.robv.android.xposed.XposedHelpers.findConstructorExact;

import android.content.ContentResolver;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Debug;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Modifier;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.context.XpContext;

public class ObjectionUtil {

    public static void hideXp() {
        try {
            XposedHelpers.findAndHookMethod(Class.class, "forName",
                    String.class,
                    boolean.class,
                    ClassLoader.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            String result = (String) param.args[0];
                            if (result.contains("posed")) {
                                param.setThrowable(new ClassNotFoundException());
                            }
                        }
                    }
            );
            XposedHelpers.findAndHookMethod(Throwable.class, "getOurStackTrace",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            StackTraceElement[] stackTrace = (StackTraceElement[]) param.getResult();
                            if (stackTrace != null) {
                                StackTraceElement[] stackTraceElements = Arrays.stream(stackTrace)
                                        .filter(i -> !i.getClassName().contains("posed")
                                                && !i.getClassName().contains("Zygote")
                                        ).toArray(StackTraceElement[]::new);
                                param.setResult(stackTraceElements);
                            }
                        }
                    }
            );
            XposedHelpers.findAndHookMethod(Thread.class, "getStackTrace",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            StackTraceElement[] stackTrace = (StackTraceElement[]) param.getResult();
                            if (stackTrace != null) {
                                List<StackTraceElement> filteredStack = new ArrayList<>();
                                for (StackTraceElement element : stackTrace) {
                                    String className = element.getClassName();
                                    if (!className.contains("posed") && !className.contains("Zygote")) {
                                        filteredStack.add(element);
                                    }
                                }
                                param.setResult(filteredStack.toArray(new StackTraceElement[0]));
                            }

                        }
                    }
            );
            XposedHelpers.findAndHookMethod(BufferedReader.class, "readLine",
                    boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            String result = (String) param.getResult();
                            if (result != null) {
                                match(result, param);
                            }
                        }
                    }
            );
//            XposedHelpers.findAndHookMethod(StackTraceElement.class, "isNativeMethod",
//                    XC_MethodReplacement.returnConstant(false));
//            XposedHelpers.findAndHookMethod(Modifier.class, "isNative", int.class,
//                    XC_MethodReplacement.returnConstant(false));
//            XposedHelpers.findAndHookMethod(System.class, "getenv", String.class,
//                    new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            String property = (String) param.args[0];
//                            if (property.contains("CLASSPATH")) {
//                                param.setResult("");
//                            }
//                        }
//                    }
//            );
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

    public static void hideRoot() {
        try {
            XposedHelpers.findAndHookMethod(Runtime.class, "exec",
                    String[].class,
                    String[].class,
                    File.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            String[] cmdArray = (String[]) param.args[0];
                            List<String> filteredCmd = new ArrayList<>();
                            for (String cmd : cmdArray) {
                                if (cmd != null && !"su".equals(cmd) && !cmd.endsWith("su") && !cmd.contains("xbin")
                                        && !cmd.contains("busybox")) {
                                    filteredCmd.add(cmd);
                                }
                            }
                            param.args[0] = filteredCmd.toArray(new String[0]);
                        }
                    }
            );
            XposedHelpers.findAndHookConstructor(File.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String result = (String) param.args[0];
                    if (result != null && result.endsWith("/su")) {
                        param.args[0] = "";
                    }
                }
            });

//            XposedHelpers.findAndHookMethod(Debug.class, "isDebuggerConnected",
//                    XC_MethodReplacement.returnConstant(false));
            if (!"release-keys".equals(Build.TAGS)) {
                XposedHelpers.setStaticObjectField(android.os.Build.class, "TAGS", "release-keys");
            }
//            XposedHelpers.findAndHookMethod("android.os.SystemProperties",
//                    XpContext.classLoader,
//                    "get",
//                    String.class,
//                    new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            if ("ro.build.selinux".equals(param.args[0])) {
//                                param.setResult("1");
//                            }
//                        }
//                    }
//            );
//            XposedHelpers.findAndHookMethod(Settings.Global.class, "getInt",
//                    ContentResolver.class,
//                    String.class,
//                    int.class,
//                    new XC_MethodHook() {
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            String arg = (String) param.args[1];
//                            if (Settings.Global.ADB_ENABLED.equals(arg)) {
//                                param.setResult(0);
//                            }
//                        }
//                    }
//            );
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

    public static void hideProxy() {
        try {
            XposedHelpers.findAndHookMethod(NetworkInterface.class,
                    "getName", XC_MethodReplacement.returnConstant("rmnet_data1"));
            XposedHelpers.findAndHookMethod(NetworkCapabilities.class, "hasTransport",
                    int.class, XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod(System.class,
                    "getProperty",
                    String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            String result = (String) param.args[0];
                            if ("http.proxyHost".equals(result) || "http.proxyPort".equals(result)
                                    || "vxp".equals(result)) {
                                param.setResult("");
                            }
//                            if ("ro.secure".equals(result)
//                                    || "ro.debuggable".equals(result)
//                            ) {
//                                param.setResult("1");
//                            }
                        }
                    }
            );
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

    public static void hideAll() {
        try {
            hideXp();
            hideRoot();
            hideProxy();
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

    public static void ChangeRegion(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getNetworkCountryIso", new Object[]{new XC_MethodHook() { // from class: xmnh.soulfrog.utils.ObjectionUtil.9
            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                String result = param.getResult().toString();
                Log.d("SoulFrog", "getNetworkCountryIso: " + result);
                param.setResult("us");
                Log.d("SoulFrog", "getNetworkCountryIso replace: " + param.getResult());
            }
        }});
        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getSimCountryIso", new Object[]{new XC_MethodHook() { // from class: xmnh.soulfrog.utils.ObjectionUtil.10
            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                String result = param.getResult().toString();
                Log.d("SoulFrog", "getSimCountryIso: " + result);
                param.setResult("us");
                Log.d("SoulFrog", "getSimCountryIso replace: " + param.getResult());
            }
        }});
        XposedHelpers.findAndHookMethod(TimeZone.class, "getDefaultRef", new Object[]{XC_MethodReplacement.returnConstant(TimeZone.getTimeZone("America/Los_Angeles"))});
        XposedHelpers.findAndHookMethod(Locale.class, "getLanguage", new Object[]{XC_MethodReplacement.returnConstant("en")});
        XposedHelpers.findAndHookMethod(Locale.class, "getCountry", new Object[]{XC_MethodReplacement.returnConstant("us")});
    }


    private static void match(String str, XC_MethodHook.MethodHookParam param) {
        if (str != null) {
            if (str.contains("/su") || str.contains("posed")) {
                param.setResult("");
            }
        }
    }
}
