package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class SDMaid implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader) {
        Class<?> cls = XposedHelpers.findClass("x7.a", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "c", XC_MethodReplacement.returnConstant(true));
        }
        Class<?> cls2 = XposedHelpers.findClass("u7.c", classLoader);
        if (cls2 != null) {
            XposedHelpers.findAndHookMethod("u7.i", classLoader, "b",
                    cls2,
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

    public static class SDMaidSe implements BaseHook {

        @Override
        public void hook(Context context, ClassLoader classLoader) {

            Class<?> info = XposedHelpers.findClass("eu.darken.sdmse.common.upgrade.core.UpgradeRepoGplay$Info", classLoader);
            if (info != null) {
                XposedBridge.hookAllConstructors(info, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedHelpers.setBooleanField(param.thisObject, "isPro", true);
                    }
                });
            }
        }
    }

}
