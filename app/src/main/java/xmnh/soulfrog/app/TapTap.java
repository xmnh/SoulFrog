package xmnh.soulfrog.app;

import android.content.Context;

import org.json.JSONObject;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.enums.AppEnum;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class TapTap implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader) {
        Class<?> momentReview = XposedHelpers.findClass("com.taptap.common.ext.moment.library.momentv2.MomentReview", classLoader);
        if (momentReview != null) {
            XposedBridge.hookAllConstructors(momentReview, new XC_MethodHook() {
                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                    XposedHelpers.setBooleanField(param.thisObject, "isBought", true);
                }
            });
        }
        Class<?> orderDetailData = XposedHelpers.findClass("com.taptap.game.core.impl.pay.v2.bean.OrderDetailData", classLoader);
        if (orderDetailData != null) {
            XposedBridge.hookAllConstructors(orderDetailData, new XC_MethodHook() {
                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                    XposedHelpers.setBooleanField(param.thisObject, "isBought", true);
                }
            });
        }
        Class<?> b = XposedHelpers.findClass("com.taptap.game.library.impl.v3.buy.b", classLoader);
        if (b != null) {
            XposedBridge.hookAllConstructors(b, new XC_MethodHook() {
                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                    XposedHelpers.setBooleanField(param.thisObject, "b", true);
                }
            });
        }
        XposedHelpers.findAndHookMethod(JSONObject.class, "optBoolean", new Object[]{String.class, new XC_MethodHook() {
            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                String key = (String) param.args[0];
                if ("is_bought".equals(key)) {
                    param.setResult(true);
                }
            }
        }});
        AppUtil.finish(context, AppEnum.TAP_TAP.getAppName());
    }
}
