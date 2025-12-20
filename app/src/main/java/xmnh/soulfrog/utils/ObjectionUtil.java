package xmnh.soulfrog.utils;

import android.net.NetworkCapabilities;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

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
                            String className = (String) param.args[0];
                            if (StringUtil.containsIgnoreCase(className, "xposed")) {
                                param.args[0] = "null";
                            }
                        }
                    }
            );
//            XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
//                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
//                    String className = (String) param.args[0];
//                    if (StringUtil.containsIgnoreCase(className, "xposed")) {
//                        param.args[0] = "null";
//                    }
//                }
//            });
            XposedHelpers.findAndHookMethod(Throwable.class, "getOurStackTrace",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            StackTraceElement[] stackTrace = (StackTraceElement[]) param.getResult();
                            if (stackTrace != null) {
                                StackTraceElement[] stackTraceElements = Arrays.stream(stackTrace)
                                        .filter(i -> !StringUtil.containsIgnoreCase(i.toString(), "xposed")
                                                && !StringUtil.containsIgnoreCase(i.toString(), "Zygote")
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
                                    String className = element.toString();
                                    if (!StringUtil.containsIgnoreCase(className, "xposed")
                                            && !StringUtil.containsIgnoreCase(className, "Zygote")) {
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
                                if (StringUtil.containsIgnoreCase(result, "xposed")) {
                                    param.setResult("");
                                }
                            }
                        }
                    }
            );
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
                                boolean isRootCmd = "su".equals(cmd) ||
                                        cmd.endsWith("/su") ||
                                        cmd.contains("/su ") ||
                                        "busybox".equals(cmd) ||
                                        cmd.endsWith("/busybox");
                                boolean isSuspiciousPath = cmd.contains("xbin/") ||
                                        cmd.contains("sbin/") ||
                                        cmd.contains("magisk");

                                if (!isRootCmd && !isSuspiciousPath) {
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
                        }
                    }
            );
            XposedHelpers.findAndHookMethod(NetworkCapabilities.class, "hasTransport",
                    int.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            if ((int)param.args[0] == NetworkCapabilities.TRANSPORT_VPN){
                                param.setResult(false);
                            }
                        }
                    });
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
//        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getNetworkCountryIso",
//                XC_MethodReplacement.returnConstant("TW"));
        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getSimCountryIso",
                XC_MethodReplacement.returnConstant("TW"));
//        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getNetworkOperator",
////                int.class,
//                XC_MethodReplacement.returnConstant("310005"));
//        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getSimOperator",
////                int.class,
//                XC_MethodReplacement.returnConstant("310005"));
        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getSimOperatorName",
                XC_MethodReplacement.returnConstant("FarEasTone"));
//        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getNetworkOperatorName",
//                XC_MethodReplacement.returnConstant("FarEasTone"));
//        XposedHelpers.findAndHookMethod(TimeZone.class, "getDefaultRef",
//                XC_MethodReplacement.returnConstant(TimeZone.getTimeZone("America/Los_Angeles")));
//        XposedHelpers.findAndHookMethod(Locale.class, "getLanguage", XC_MethodReplacement.returnConstant("en"));
//        XposedHelpers.findAndHookMethod(Locale.class, "getCountry", XC_MethodReplacement.returnConstant("us"));


    }

}
