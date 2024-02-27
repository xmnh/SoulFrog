package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;


public class Launcher3 implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> cls = XposedHelpers.findClass("com.ldmnq.AdSdkInterface", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "initOnce", Context.class,
                    XC_MethodReplacement.returnConstant(null));
            XposedHelpers.findAndHookMethod(cls, "isOpenCleanMode", Context.class,
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

}
