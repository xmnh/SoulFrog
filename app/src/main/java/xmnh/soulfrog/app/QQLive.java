package xmnh.soulfrog.app;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;

public class QQLive implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader) {
        Class<?> homeActivity = XposedHelpers.findClass("com.tencent.qqlive.ona.activity.HomeActivity", classLoader);
        if (homeActivity != null) {
            XposedHelpers.findAndHookMethod(homeActivity, "shouldDelayShowTeenGuardDialog",
                    XC_MethodReplacement.returnConstant(true));
        }
        Class<?> feedAdConfigUtils = XposedHelpers.findClass("com.tencent.qqlive.ad.FeedAdConfigUtils", classLoader);
        if (feedAdConfigUtils != null) {
            HookUtil.booleAndVoidReplace(feedAdConfigUtils, false);
        }
        Class<?> adPlayerData = XposedHelpers.findClass("com.tencent.qqlive.qadcore.data.AdPlayerData", classLoader);
        if (adPlayerData != null) {
            for (Method method : adPlayerData.getDeclaredMethods()) {
                if (method.getReturnType() == void.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(null));
                }
            }
        }
        Class<?> watermarkManager = XposedHelpers.findClass("com.tencent.qqlive.ona.player.plugin.watermark.WatermarkManager", classLoader);
        if (watermarkManager != null) {
            for (Method method : watermarkManager.getDeclaredMethods()) {
                if (method.getReturnType() == boolean.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(false));
                }
            }
        }
        Class<?> tVKLogoInfo = XposedHelpers.findClass("com.tencent.qqlive.tvkplayer.api.vinfo.TVKLogoInfo", classLoader);
        if (tVKLogoInfo != null) {
            XposedHelpers.findAndHookMethod(tVKLogoInfo, "getShow",
                    XC_MethodReplacement.returnConstant(false));
        }
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
        AppUtil.finish(context);
    }

}
