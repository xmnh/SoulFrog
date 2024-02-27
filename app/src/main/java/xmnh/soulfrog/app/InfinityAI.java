package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;

public class InfinityAI implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader) {
        Class<?> tradeUserInfo = XposedHelpers.findClass("com.blockmeta.bbs.businesslibrary.TradeUserInfo", classLoader);
        if (tradeUserInfo != null) {
            XposedHelpers.findAndHookMethod(tradeUserInfo, "isVip",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(tradeUserInfo, "isInVipWhiteList",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(tradeUserInfo, "isInSquareWhiteList",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(tradeUserInfo, "isInAiWhiteList",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(tradeUserInfo, "isInWhiteList",
                    XC_MethodReplacement.returnConstant(true));
        }
        XposedHelpers.findAndHookMethod("com.blockmeta.bbs.businesslibrary.pojo.PublishOption", classLoader, "getVipOption",
                XC_MethodReplacement.returnConstant(false));
    }
}
