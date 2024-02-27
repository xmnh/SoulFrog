package xmnh.soulfrog.app;

import android.content.Context;
import android.preference.Preference;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class JuiceSSH implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> cls = XposedHelpers.findClass("com.sonelli.ej0", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "z", Preference.class,
                    XC_MethodReplacement.returnConstant(null));
        }
        Class<?> cls2 = XposedHelpers.findClass("com.sonelli.oi0", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls2, "d", Object.class,
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

}
