package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class TomatoBook implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader) {
        context.getSharedPreferences("kv_new_user_free_privilege", 0)
                .edit()
                .putInt("key_free_download_count", 666)
                .apply();
        Class<?> novelRewardAdDependImpl = XposedHelpers.findClass("com.dragon.read.ad.exciting.video.inspire.impl.NovelRewardAdDependImpl", classLoader);
        if (novelRewardAdDependImpl != null) {
            XposedHelpers.findAndHookMethod(novelRewardAdDependImpl, "hasNoAdFollAllScene",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(novelRewardAdDependImpl, "isNoAd", String.class,
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }
}
