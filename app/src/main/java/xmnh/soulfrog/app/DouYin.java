package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class DouYin implements BaseHook {
    @Override
    public void hook(Context context, ClassLoader classLoader) {
        Class<?> verticalViewPager = XposedHelpers.findClassIfExists("com.ss.android.ugc.aweme.common.widget.VerticalViewPager", classLoader);
        if (verticalViewPager != null) {
            XposedHelpers.findAndHookMethod(verticalViewPager, "LJJJJI",
                    String.class, boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.args[0] = "PlayletAdBottomPresenter#handleBottomBar: not ad";
                            param.args[1] = false;
                        }
                    });
        }
        AppUtil.finish(context);
    }
}
