package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class BrainWave implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader) {
        String defaultSpName = AppUtil.getDefaultSpName(context);
        context.getSharedPreferences(defaultSpName, Context.MODE_PRIVATE)
                .edit()
                .putBoolean("is_svip", true)
                .apply();
        Class<?> account = XposedHelpers.findClass("com.imoblife.brainwave.storge.Account", classLoader);
        if (account != null) {
            Class<?> continuation = XposedHelpers.findClass("kotlin.coroutines.Continuation", classLoader);
            XposedHelpers.findAndHookMethod(account, "isSuperPackageUser", continuation,
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

}
