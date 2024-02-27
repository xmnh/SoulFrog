package xmnh.soulfrog.app;

import android.content.Context;

import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;


public class Launcher2 implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {

        AppUtil.finish(context);
    }

}
