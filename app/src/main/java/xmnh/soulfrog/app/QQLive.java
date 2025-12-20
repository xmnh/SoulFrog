package xmnh.soulfrog.app;

import android.content.Context;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class QQLive implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader) {
/*        Class<?> homeActivity = XposedHelpers.findClass("com.tencent.qqlive.ona.activity.HomeActivity", classLoader);
        if (homeActivity != null) {
            XposedHelpers.findAndHookMethod(homeActivity, "shouldDelayShowTeenGuardDialog",
                    XC_MethodReplacement.returnConstant(true));
        }*/
/*        Class<?> watermarkManager = XposedHelpers.findClass("com.tencent.qqlive.ona.player.plugin.watermark.WatermarkManager", classLoader);
        if (watermarkManager != null) {
            for (Method method : watermarkManager.getDeclaredMethods()) {
                if (method.getReturnType() == boolean.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(false));
                }
            }
        }*/
        Class<?> tVideoNetInfo = XposedHelpers.findClass("com.tencent.tavcut.tvkplayer.manager.TVideoNetInfo", classLoader);
        if (tVideoNetInfo != null) {
            XposedHelpers.findAndHookMethod(tVideoNetInfo, "isHasWatermark",
                    XC_MethodReplacement.returnConstant(false));
        }
        // 暂停广告
        Class<?> TVKPlayerManager = XposedHelpers.findClass("com.tencent.qqlive.tvkplayer.logic.TVKPlayerManager", classLoader);
        if (TVKPlayerManager != null) {
            XposedHelpers.findAndHookMethod(TVKPlayerManager, "pauseWithIsAllowShowPauseAd",
                    boolean.class, ViewGroup.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.args[0] = false;
                        }
                    });
        }
        Class<?> tVKPlayerConfig = XposedHelpers.findClass("com.tencent.qqlive.tvkplayer.kmm.utils.TVKPlayerConfig", classLoader);
        if (tVKPlayerConfig != null) {
            XposedHelpers.findAndHookMethod(tVKPlayerConfig, "EnableAnimationWatermark", XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod(tVKPlayerConfig, "EnableDarkWatermark", XC_MethodReplacement.returnConstant(false));
        }
        Class<?> vMTPlayerInfoUtils = XposedHelpers.findClass("com.tencent.qqlive.modules.vb.playerplugin.impl.utils.VMTPlayerInfoUtils", classLoader);
        if (vMTPlayerInfoUtils != null) {
            Class<?> vMTVideoInfo = XposedHelpers.findClass("com.tencent.qqlive.modules.vb.playerplugin.impl.VMTVideoInfo", classLoader);
            XposedHelpers.findAndHookMethod(vMTPlayerInfoUtils, "isNoAdPlayMode", vMTVideoInfo,
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(vMTPlayerInfoUtils, "isCanPlayAD", vMTVideoInfo,
                    XC_MethodReplacement.returnConstant(false));
        }

        AppUtil.finish(context);
    }

}
