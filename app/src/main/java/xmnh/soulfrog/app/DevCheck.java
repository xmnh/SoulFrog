package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class DevCheck implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> cls = XposedHelpers.findClass("a7.q", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "b", String.class,
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

}
