package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.HookUtil;

public class APKPure implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader) {
        Class<?> bannerConfig = XposedHelpers.findClass("com.apkpure.aegon.ads.BannerConfig", classLoader);
        if (bannerConfig != null) {
            HookUtil.booleAndVoidReplace(bannerConfig, false);
        }
        Class<?> interstitialConfig = XposedHelpers.findClass("com.apkpure.aegon.ads.InterstitialConfig", classLoader);
        if (interstitialConfig != null) {
            HookUtil.booleAndVoidReplace(interstitialConfig, false);
        }
        Class<?> splashConfig = XposedHelpers.findClass("com.apkpure.aegon.ads.SplashConfig", classLoader);
        if (splashConfig != null) {
            HookUtil.booleAndVoidReplace(splashConfig, false);
        }
    }

}
