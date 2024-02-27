package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class AudioClips implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> cls = XposedHelpers.findClass("com.wm.common.user.UserManager", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "isPermanentVip",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod("com.wm.common.user.UserManager", classLoader, "isVip",
                    XC_MethodReplacement.returnConstant(true));
        }
        Class<?> cls2 = XposedHelpers.findClass("com.wm.common.user.UserInfoManager", classLoader);
        if (cls2 != null) {
            XposedHelpers.findAndHookMethod(cls2, "isPermanentVip",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(cls2, "isVip",
                    XC_MethodReplacement.returnConstant(true));
        }
        Class<?> cls3 = XposedHelpers.findClass("com.wm.common.ad.util.AdUtil", classLoader);
        if (cls3 != null) {
            XposedHelpers.findAndHookMethod(cls3, "isVip",
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

}
