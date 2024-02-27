package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class FakeLocation implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        String appVersionName = AppUtil.getAppVersionName(context);
        if ("1.3.1.9".equals(appVersionName)) {
            XposedHelpers.findAndHookMethod("ށ.ރ.ؠ.ؠ.֏", classLoader, "ޅ",
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

}
