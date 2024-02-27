package xmnh.soulfrog.utils;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;

public class KillAdsUtil {
    private static final Map<String, Class<?>> CLASS_CACHE = new HashMap<>();
    private static final Map<Class<?>, Boolean> CACHE_FLAG = new HashMap<>();
    private static final List<String> ADS = new ArrayList<>();
    private static boolean finish;

    static {
        // 优量汇
        ADS.add("com.qq.e.comm.managers.GDTADManager");
        // 广点通
        ADS.add("com.qq.e.comm.managers.b");
        // 穿山甲
        ADS.add("com.bytedance.sdk.openadsdk.TTAdSdk");
        ADS.add("com.bytedance.sdk.openadsdk.TTAdManager");
        // 游可赢
        ADS.add("com.tencent.klevin.KlevinManager");
//        ADS.add("com.bytedance.sdk.openadsdk.TTAdBridge");
//        ADS.add("com.bytedance.sdk.openadsdk.AdSlot$Builder");
//        ADS.add("com.bytedance.pangle.Zeus");
        // 友盟
        ADS.add("com.umeng.commonsdk.UMConfigure");
        // bugly
        ADS.add("com.tencent.bugly.crashreport.CrashReport");
//        ADS.add("com.tencent.bugly.Bugly");
        ADS.add("com.tencent.ams.xsad.rewarded.RewardedAd");
        // google
        ADS.add("com.google.android.gms.ads.MobileAds");
//        ADS.add("com.google.ads.mediation.admob.AdMobAdapter");
        // 百青藤
        ADS.add("com.baidu.mobads.sdk.api.BDAdConfig");
//        ADS.add("com.baidu.mobads.sdk.api.AdView");
//        ADS.add("com.baidu.mobads.sdk.api.NovelSDKConfig");
//        ADS.add("com.baidu.mobads.sdk.internal.bo");
        // bigo
        ADS.add("sg.bigo.ads.BigoAdSdk");
        // sigmob
        ADS.add("com.sigmob.windad.WindAds");
        ADS.add("com.sigmob.sdk.Sigmob");
        // 有米
        ADS.add("net.youmi.overseas.android.YoumiOffersWallSdk");
        // 快手
        ADS.add("com.kwad.sdk.api.KsAdSDK");
//        ADS.add("com.kwad.sdk.core.network.BaseResultData");
//        ADS.add("com.kwad.components.offline.api.core.network.model.BaseOfflineCompoResultData");
//        ADS.add("com.kwad.sdk.KsAdSDKImpl");
//        ADS.add("com.kuaishou.pushad.PushAdManager");
//        ADS.add("com.kuaishou.pushad.KsAdGlobalWatcher");
//        ADS.add("com.tencentmusic.ad.TMEAds");
        // unity
        ADS.add("com.unity3d.ads.UnityAds");
        // Vungle
        ADS.add("com.vungle.warren.Vungle");
        // yomob
        ADS.add("com.yomob.tgsdklib.TGADSDK");
        ADS.add("com.mbridge.msdk.MBridgeSDK");
//        ADS.add("com.jd.ad.sdk.JadAdNative");
        // 京准通
        ADS.add("com.jd.ad.sdk.JadYunSdk");
        // 倍孜
        ADS.add("com.beizi.ad.BeiZi");
        ADS.add("com.beizi.fusion.BeiZis");
        // 小米
        ADS.add("com.xiaomi.ad.AdSdk");
//        ADS.add("com.tianmu.TianmuSDK");
//        ADS.add("com.tencent.qqmini.sdk.utils.AdUtil");
//        ADS.add("com.tencent.qqmini.sdk.plugins.BannerAdPlugin");
//        ADS.add("com.tencent.qqmini.minigame.plugins.BlockAdPlugin");
//        ADS.add("com.tencent.ads.AdManager");
//        ADS.add("com.tencent.ad.tangram.AdManagerForQQ");
//        ADS.add("com.tencent.ad.tangram.settings.AdSettingsManager");
//        ADS.add("com.tencent.gdtad.aditem.GdtAd");
//        ADS.add("com.tencent.gdtad.api.GdtAd");
//        ADS.add("com.anythink.splashad.api.ATSplashAd");
        // 米盟
        ADS.add("com.miui.zeus.mimo.sdk.MimoSdk");
        // 极光推送
        ADS.add("cn.jpush.android.api.JPushInterface");
//        ADS.add("cn.jiguang.union.ads.base.api.JAdApi");
//        ADS.add("cn.jiguang.ads.nativ.api.JNativeAdApi");
//        ADS.add("cn.jiguang.ads.notify.api.JNotifyAdApi");
        // inmobi
        ADS.add("com.inmobi.sdk.InMobiSdk");
        // oppo联盟
        ADS.add("com.heytap.msp.mobad.api.MobAdManager");
        // 脸书
        ADS.add("com.facebook.ads.AudienceNetworkAds");
        // smaato
        ADS.add("com.smaato.sdk.core.SmaatoSdk");
        // applovin
        ADS.add("com.applovin.sdk.AppLovinSdk");
        // mopub
        ADS.add("com.mopub.common.MoPub");
        // 华为
        ADS.add("com.huawei.hms.ads.HwAds");
        // verve
        ADS.add("net.pubnative.lite.sdk.HyBid");
        // yandex
        ADS.add("com.yandex.mobile.ads.common.MobileAds");
        // VerizonAds
        ADS.add("com.yahoo.ads.YASAds");
        // inmobi
        ADS.add("com.inmobi.sdk.InMobiSdk");
        // maio
        ADS.add("jp.maio.sdk.android.MaioAds");
        // verve
        ADS.add("net.pubnative.lite.sdk.HyBid");
    }

    public static void killAds(ClassLoader classLoader) {
        if (finish) return;
        for (String ad : ADS) {
            try {
                Class<?> cls = CLASS_CACHE.get(ad);
                if (cls == null) {
                    cls = XposedHelpers.findClassIfExists(ad, classLoader);
                    if (cls != null) {
                        CLASS_CACHE.put(ad, cls);
                    }
                }
                replace(cls);
            } catch (Throwable e) {
                XposedBridge.log(e);
            }
        }
        Runtime.getRuntime().gc();
        finish = true;
    }

    private static void replace(Class<?> cls) {
        if (cls == null) return;
        if (CACHE_FLAG.containsKey(cls)) return;
        for (Method method : cls.getDeclaredMethods()) {
            if (Modifier.isNative(method.getModifiers())) continue;
            if (method.getReturnType() == boolean.class
                    || method.getReturnType() == Boolean.class) {
                XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(false));
            } else if (method.getReturnType() == void.class
                    || method.getReturnType() == cls) {
                XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(null));
            } else if (method.getReturnType() == String.class) {
                XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(""));
            }
        }
        CACHE_FLAG.put(cls, Boolean.TRUE);
        Log.d(SoulFrog.TAG,"killAd => " + cls.getName());
    }

    public static void assets() {
        XposedHelpers.findAndHookMethod(AssetManager.class, "open",
                String.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        // 获取要打开的文件名
                        String fileName = (String) param.args[0];
                        // 判断是否是广告插件
                        if (fileName.contains("xadsdk")
                                || StringUtil.isMD5(fileName)
                                || (StringUtil.isNumeric(fileName) && fileName.length() >= 9)
                                || fileName.contains("gdtadv2")) {
                            XposedBridge.log("assets ad plugin => " + fileName);
                            // 如果是广告插件，则返回一个空的 InputStream，从而阻止文件的加载
                            param.args[0] = "";
                            // 更改为抛FileNotFoundException异常
//                            param.setThrowable(new Exception());
                        }
                    }
                });
    }

    public static void killAdPrecise(ClassLoader classLoader) {
        try {
            byteDanceAd(classLoader);
            tencentAd(classLoader);
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

    private static void byteDanceAd(ClassLoader classLoader) {
        try {
            Class<?> ttAdSdk = XposedHelpers.findClass("com.bytedance.sdk.openadsdk.TTAdSdk", classLoader);
            if (ttAdSdk != null) {
                XposedHelpers.findAndHookMethod(ttAdSdk, "getAdManager",
                        XC_MethodReplacement.returnConstant(null));
            }
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

    private static void tencentAd(ClassLoader classLoader) {
        try {
            Class<?> splashAD = XposedHelpers.findClass("com.qq.e.ads.splash.SplashAD", classLoader);
            if (splashAD != null) {
                XposedBridge.hookAllConstructors(splashAD, XC_MethodReplacement.returnConstant(null));
            }
            Class<?> rewardVideoAD = XposedHelpers.findClass("com.qq.e.ads.rewardvideo.RewardVideoAD", classLoader);
            if (rewardVideoAD != null) {
                XposedHelpers.findAndHookMethod(rewardVideoAD, "loadAD", XC_MethodReplacement.returnConstant(null));
            }
            Class<?> unifiedInterstitialAD = XposedHelpers.findClass("com.qq.e.ads.interstitial2.UnifiedInterstitialAD", classLoader);
            if (unifiedInterstitialAD != null) {
                XposedHelpers.findAndHookMethod(unifiedInterstitialAD, "loadAD", XC_MethodReplacement.returnConstant(null));
            }
            Class<?> unifiedBannerView = XposedHelpers.findClass("com.qq.e.ads.banner2.UnifiedBannerView", classLoader);
            if (unifiedBannerView != null) {
                XposedHelpers.findAndHookMethod(unifiedBannerView, "loadAD", XC_MethodReplacement.returnConstant(null));
            }
            Class<?> nativeExpressAD = XposedHelpers.findClass("com.qq.e.ads.nativ.NativeExpressAD", classLoader);
            if (nativeExpressAD != null) {
                XposedBridge.hookAllMethods(nativeExpressAD, "loadAD", XC_MethodReplacement.returnConstant(null));
            }
            Class<?> nativeUnifiedAD = XposedHelpers.findClass("com.qq.e.ads.nativ.NativeUnifiedAD", classLoader);
            if (nativeUnifiedAD != null) {
                XposedBridge.hookAllMethods(nativeUnifiedAD, "loadData", XC_MethodReplacement.returnConstant(null));
            }
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

}
