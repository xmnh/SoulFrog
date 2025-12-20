package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.enums.AppEnum;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class Reader implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader) {
        context.getSharedPreferences("com.originatorkids.EndlessAlphabet.MainActivity", 0)
                .edit()
                .putBoolean("is_purchased_reader_packs_all", true)
                .apply();
        Class<?> endlessReaderIAPFacade = XposedHelpers.findClass("com.originatorkids.EndlessAlphabet.EndlessReaderIAPFacade", classLoader);
        if (endlessReaderIAPFacade != null) {
            XposedHelpers.findAndHookMethod(endlessReaderIAPFacade, "getBuyAllIAPId",
                    XC_MethodReplacement.returnConstant("reader_packs_all"));
        }
        Class<?> iAPInfo = XposedHelpers.findClass("com.originatorkids.psdk.IAPFacade$IAPInfo", classLoader);
        if (iAPInfo != null) {
            XposedBridge.hookAllConstructors(iAPInfo, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object thisObject = param.thisObject;
                    XposedHelpers.setBooleanField(thisObject, "hasBeenPurchased", true);
                }
            });
        }
        AppUtil.finish(context);
    }
}
