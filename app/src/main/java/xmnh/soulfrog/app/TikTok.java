package xmnh.soulfrog.app;

import android.content.Context;

import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.ObjectionUtil;

public class TikTok implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader) {
        ObjectionUtil.ChangeRegion(classLoader);
        AppUtil.finish(context);
    }
}
