package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class BrainWave implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> cls = XposedHelpers.findClass("kotlin.coroutines.jvm.internal.Boxing", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "boxBoolean",
                    boolean.class,
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

}
