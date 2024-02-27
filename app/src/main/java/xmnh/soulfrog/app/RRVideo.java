package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;


public class RRVideo implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> appCommonUtils = XposedHelpers.findClass("com.pptv.common.utils.AppCommonUtils", classLoader);
        if (appCommonUtils != null) {
            HookUtil.booleReplace(appCommonUtils, true);
        }
//        Class<?> vipInfo = XposedHelpers.findClass("com.pptv.common.data.bean.vipInfo", classLoader);
//        if (vipInfo != null) {
//            XposedHelpers.findAndHookMethod(vipInfo, "getValid", XC_MethodReplacement.returnConstant(true));
//        }
//        Class<?> baseControllerViewModel = XposedHelpers.findClass("com.pptv.drama.data.vm.base.baseControllerViewModel", classLoader);
//        if (baseControllerViewModel != null) {
//            XposedHelpers.findAndHookMethod(baseControllerViewModel, "hasVIPPrivilege", XC_MethodReplacement.returnConstant(true));
//            XposedHelpers.findAndHookMethod(baseControllerViewModel, "hasVODPrivilege", XC_MethodReplacement.returnConstant(true));
//        }
        AppUtil.finish(context);
    }

}
