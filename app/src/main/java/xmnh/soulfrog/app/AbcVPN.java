package xmnh.soulfrog.app;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.UUID;

import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;

public class AbcVPN implements BaseHook {
    private static final long EXPIRE_TIME = 2_400_000;
//    private SharedPreferences sp;

    @Override
    public void hook(Context context, ClassLoader classLoader) {
        AppUtil.sp = context.getSharedPreferences("alcedo", MODE_PRIVATE);
        if (AppUtil.checkSp(UUID.randomUUID().toString(), 0)) {
            AppUtil.finish(context);
        }
//        vip(classLoader);
    }

//    private void check() {
//        long currentTime = System.currentTimeMillis();
//        if (!sp.contains("expire_time") || currentTime >= sp.getLong("expire_time", 0)) {
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("device_id", UUID.randomUUID().toString());
//            editor.putLong("expire_time", currentTime + EXPIRE_TIME);
//            editor.apply();
//        }
//    }
}
