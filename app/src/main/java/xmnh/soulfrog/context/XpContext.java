package xmnh.soulfrog.context;

import android.content.Context;

import java.lang.ref.WeakReference;

public abstract class XpContext {
    private static WeakReference<Context> context;
    public static ClassLoader classLoader;

    public static Context getContext(Context c){
        if (context == null) {
            context = new WeakReference<>(c);
        }
        return context.get();
    }
}
