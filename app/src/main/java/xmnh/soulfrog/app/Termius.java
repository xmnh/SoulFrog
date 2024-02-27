package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class Termius implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> cls = XposedHelpers.findClass("com.server.auditor.ssh.client.app.w", classLoader);
        if (cls != null) {
            XposedHelpers.findAndHookMethod(cls, "m0", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(cls, "r0", XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }
}
