package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class Tick implements BaseHook {
    boolean flag = true;
    @Override
    public void hook(Context context, ClassLoader classLoader) {
        if (flag) {
            Class<?> generalApi = XposedHelpers.findClass("com.ticktick.task.sync.network.GeneralApi", classLoader);
            Class<?> signUserInfo = XposedHelpers.findClass("com.ticktick.task.network.sync.entity.SignUserInfo", classLoader);
            if (generalApi != null){
                XposedHelpers.findAndHookMethod(generalApi, "getUserStatus", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Object result = param.getResult();
                        XposedHelpers.setBooleanField(result,"teamPro",true);
                        XposedHelpers.setBooleanField(result,"pro",true);
                        XposedHelpers.setBooleanField(result,"needSubscribe",false);
                        param.setResult(result);
                    }
                });
            }

            AppUtil.finish(context);
            flag = false;
        }
    }

}
