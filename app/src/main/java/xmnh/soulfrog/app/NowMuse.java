package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class NowMuse implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> vipNowRight = XposedHelpers.findClass("com.imoblife.now.bean.VipNowRight", classLoader);
        if (vipNowRight != null) {
            XposedHelpers.findAndHookMethod(vipNowRight, "isIs_vip",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(vipNowRight, "isVip_forever",
                    XC_MethodReplacement.returnConstant(true));
        }
        Class<?> vipYogaRight = XposedHelpers.findClass("com.imoblife.now.bean.VipYogaRight", classLoader);
        if (vipYogaRight != null) {
            XposedHelpers.findAndHookMethod(vipYogaRight, "getIs_vip",
                    XC_MethodReplacement.returnConstant(true));
        }
        Class<?> nowLog = XposedHelpers.findClass("com.imoblife.now.bean.NowLog", classLoader);
        if (nowLog != null) {
            XposedHelpers.findAndHookMethod(nowLog, "isVipUser",
                    XC_MethodReplacement.returnConstant(true));
        }
        Class<?> vipExpandRight = XposedHelpers.findClass("com.imoblife.now.bean.VipExpandRight", classLoader);
        if (vipExpandRight != null) {
            XposedHelpers.findAndHookMethod(vipExpandRight, "getIs_vip",
                    XC_MethodReplacement.returnConstant(true));
        }
        Class<?> course = XposedHelpers.findClass("com.imoblife.now.bean.Course", classLoader);
        if (course != null) {
            XposedHelpers.findAndHookMethod(course, "isVip",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(course, "isPurchased",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(course, "isSubscribe",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(course, "isFreeCourse",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(course, "isFutureFreeCourse",
                    XC_MethodReplacement.returnConstant(true));
        }
        Class<?> track = XposedHelpers.findClass("com.imoblife.now.bean.Track", classLoader);
        if (track != null) {
            XposedHelpers.findAndHookMethod(track, "isFree",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(track, "isUnLock",
                    XC_MethodReplacement.returnConstant(true));
        }
        AppUtil.finish(context);
    }

}
