package xmnh.soulfrog.app;

import android.content.Context;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class AiQiYi implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> playerAlbumInfo = XposedHelpers.findClassIfExists("com.iqiyi.video.qyplayersdk.model.PlayerAlbumInfo", classLoader);
        if (playerAlbumInfo != null) {
            XposedHelpers.findAndHookMethod(playerAlbumInfo, "isShowWaterMark",
                    XC_MethodReplacement.returnConstant(false));
        }
        Class<?> PlayerAlbumInfo = XposedHelpers.findClassIfExists("com.qiyi.shortplayer.player.model.PlayerAlbumInfo", classLoader);
        if (PlayerAlbumInfo != null) {
            XposedHelpers.findAndHookMethod(PlayerAlbumInfo, "isShowWaterMark",
                    XC_MethodReplacement.returnConstant(false));
        }
        Class<?> QYPlayerControlConfig = XposedHelpers.findClassIfExists("com.iqiyi.video.qyplayersdk.model.QYPlayerControlConfig", classLoader);
        if (QYPlayerControlConfig != null) {
            XposedHelpers.findAndHookMethod(QYPlayerControlConfig, "isShowWaterMark",
                    XC_MethodReplacement.returnConstant(false));
        }
        Class<?> QYPlayerADConfig = XposedHelpers.findClassIfExists("com.iqiyi.video.qyplayersdk.model.QYPlayerADConfig", classLoader);
        if (QYPlayerADConfig != null) {
            XposedHelpers.findAndHookMethod(QYPlayerADConfig, "isShowPause",
                    XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod(QYPlayerADConfig, "needCheckHalfPauseAdShow",
                    XC_MethodReplacement.returnConstant(false));
        }
        Class<?> adDefaultListener = XposedHelpers.findClassIfExists("com.iqiyi.video.qyplayersdk.cupid.listener.AdDefaultListener", classLoader);
        if (adDefaultListener != null) {
            XposedHelpers.findAndHookMethod(adDefaultListener, "isNeedRequestPauseAds",
                    XC_MethodReplacement.returnConstant(false));
        }
        Class<?> AdsController = XposedHelpers.findClassIfExists("com.iqiyi.video.qyplayersdk.cupid.AdsController", classLoader);
        if (AdsController != null) {
            XposedHelpers.findAndHookMethod(AdsController, "isNeedRequestPauseAds",
                    XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod(AdsController, "showOrHideAdView",
                    int.class, boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.args[0] = 17;
                            param.args[1] = false;
                        }
                    });
        }
        Class<?> d = XposedHelpers.findClassIfExists("com.qiyi.video.qysplashscreen.d", classLoader);
        if (d != null) {
            XposedHelpers.findAndHookMethod(d, "showHotLaunchSplashAd",
                    String.class,
                    XC_MethodReplacement.returnConstant(false));
        }
        Class<?> sdkAdapterInitor = XposedHelpers.findClassIfExists("org.iqiyi.video.adapter.sdk.SdkAdapterInitor", classLoader);
        if (sdkAdapterInitor != null) {
            XposedBridge.hookAllMethods(sdkAdapterInitor, "initAd", XC_MethodReplacement.returnConstant(null));
        }

        Class<?> adPlayerCondition = XposedHelpers.findClassIfExists("org.qiyi.cast.ui.ad.AdPlayerCondition", classLoader);
        if (adPlayerCondition != null) {
            XposedHelpers.findAndHookMethod(adPlayerCondition, "isAllowPlay", XC_MethodReplacement.returnConstant(false));
        }
        Class<?> mainActivity = XposedHelpers.findClassIfExists("org.qiyi.android.video.MainActivity", classLoader);
        if (mainActivity != null) {
            for (Method method : mainActivity.getDeclaredMethods()) {
                if (method.getModifiers() == Modifier.PRIVATE && method.getReturnType() == boolean.class
                        && method.getParameterTypes().length == 1) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(false));
                }
            }
        }
        AppUtil.finish(context);
    }

}
