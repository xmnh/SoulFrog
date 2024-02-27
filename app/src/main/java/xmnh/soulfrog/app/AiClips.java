package xmnh.soulfrog.app;

import android.content.Context;

import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class AiClips implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        context.getSharedPreferences("Pay", 0).edit().putBoolean("type", true).apply();
        AppUtil.finish(context);
    }

}
