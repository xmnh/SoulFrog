package xmnh.soulfrog.app;

import android.content.Context;

import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class Termius implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        String defaultSpName = AppUtil.getDefaultSpName(context);
        context.getSharedPreferences(defaultSpName, Context.MODE_PRIVATE)
                .edit()
                .putBoolean("key_is_pro_mode_inactive", false)
                .putBoolean("key_account_has_personal_subscription", true)
                .putBoolean("key_is_user_profile_received", true)
                .putString("key_current_plan_type", "Pro")
                .apply();
//        Class<?> cls = XposedHelpers.findClass("com.server.auditor.ssh.client.app.w", classLoader);
//        if (cls != null) {
//            XposedHelpers.findAndHookMethod(cls, "m0", XC_MethodReplacement.returnConstant(true));
//            XposedHelpers.findAndHookMethod(cls, "r0", XC_MethodReplacement.returnConstant(true));
//        }
        AppUtil.finish(context);
    }
}
