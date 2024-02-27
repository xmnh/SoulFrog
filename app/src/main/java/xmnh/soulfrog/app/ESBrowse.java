package xmnh.soulfrog.app;

import android.content.Context;

import org.json.JSONObject;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;

public class ESBrowse implements BaseHook {
    boolean flag = true;

    @Override
    public void hook(Context context, ClassLoader classLoader) {
        if (flag) {
            Class<?> accountInfo = XposedHelpers.findClass("com.estrongs.android.pop.app.account.model.AccountInfo", classLoader);
            HookUtil.gsonFromJson(classLoader, param -> {
                try {
                    JSONObject jsonObject = new JSONObject((String) param.args[0]);
                    if (accountInfo != null && param.args[1] == accountInfo) {
                        if (jsonObject.has("isVip") && jsonObject.has("vipFinishAt")) {
                            jsonObject.put("isVip", true);
                            jsonObject.put("vipFinishAt", 218330035688000L);
                            param.args[0] = jsonObject.toString();
                        }
                    }
                } catch (Throwable e) {
                    XposedBridge.log(SoulFrog.TAG + " ==> " + e.getMessage());
                }
            });
            AppUtil.finish(context);
            flag = false;
        }
    }

}
