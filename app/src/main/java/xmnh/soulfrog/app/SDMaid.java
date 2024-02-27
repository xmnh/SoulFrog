package xmnh.soulfrog.app;

import android.content.Context;

import java.lang.reflect.Method;
import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
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
            Class<?> state = XposedHelpers.findClass("eu.darken.sdmse.corpsefinder.ui.settings.CorpseFinderSettingsViewModel$State", classLoader);
            Class<?> corpseFinder = XposedHelpers.findClass("eu.darken.sdmse.corpsefinder.core.CorpseFinder$State", classLoader);
            if (state != null && corpseFinder != null) {
                XposedHelpers.findAndHookConstructor(state, corpseFinder, boolean.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[1] = true;
                    }
                });
            }
            Class<?> info = XposedHelpers.findClass("eu.darken.sdmse.common.upgrade.core.UpgradeRepoGplay$Info", classLoader);
            Class<?> billingData = XposedHelpers.findClass("eu.darken.sdmse.common.upgrade.core.billing.BillingData", classLoader);
            if (info != null && billingData != null) {
                XposedHelpers.findAndHookConstructor(info, boolean.class, billingData, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[0] = true;
                    }
                });
            }
        }
    }

}
