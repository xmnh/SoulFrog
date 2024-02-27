package xmnh.soulfrog.app;

import android.content.Context;

import org.json.JSONObject;

import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;

public class DailyTask implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        context.getSharedPreferences("share_data", 0)
                .edit()
                .putBoolean("show_splash_activity", false)
                .putString("activity_splash", "")
                .apply();
        Class<?> queryPayInfo = XposedHelpers.findClass("com.moyan.dailytask.data.QueryPayInfo", classLoader);
        HookUtil.gsonFromJson(classLoader, param -> {
            try {
                JSONObject jSONObject = new JSONObject(param.args[0].toString());
                if (queryPayInfo != null && param.args[1] == queryPayInfo) {
                    if (jSONObject.has("ret") && jSONObject.has("endTime")) {
                        jSONObject.put("count", 1);
                        jSONObject.put("desc", "永久会员");
                        jSONObject.put("endTime", "2099-12-31");
                        jSONObject.put("message", "已付费");
                        jSONObject.put("ret", 200);
                        param.args[0] = jSONObject.toString();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        AppUtil.finish(context);
    }

}
