package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class BearVCR implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> cls = XposedHelpers.findClass("com.duapps.recorder.kc0", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "a",
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

}
