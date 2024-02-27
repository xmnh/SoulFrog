package xmnh.soulfrog.app;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;

public class Tick implements BaseHook {
    boolean flag = true;
    @Override
    public void hook(Context context, ClassLoader classLoader) {
/*        Class<?> user = XposedHelpers.findClass("com.ticktick.task.data.User", classLoader);
        if (user != null) {
            XposedHelpers.findAndHookMethod(user, "isPro",
                    XC_MethodReplacement.returnConstant(true));
        }*/
        if (flag) {
/*            HookUtil.findOkHttpResponseBody(classLoader, param -> {
                String result = (String) param.getResult();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("teamPro") && jsonObject.has("pro")
                            && jsonObject.has("proEndDate") && jsonObject.has("needSubscribe")) {
                        jsonObject.put("teamPro", true);
                        jsonObject.put("pro", true);
                        jsonObject.put("proEndDate", "2099-12-31T23:59:59.999+9999");
                        jsonObject.put("needSubscribe", false);
                        param.setResult(jsonObject.toString());
                    }
                } catch (Throwable e) {
                    XposedBridge.log(SoulFrog.TAG + "findOkHttpResponseBody ==> " + e.getMessage());
                }
            });*/
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
