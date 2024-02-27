package xmnh.soulfrog;

import android.app.Application;
import android.content.Context;

import java.util.function.BiConsumer;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import xmnh.soulfrog.context.XpContext;
import xmnh.soulfrog.factory.AppFactory;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.KillAdsUtil;
import xmnh.soulfrog.utils.ObjectionUtil;

public class Jump implements IXposedHookLoadPackage {

    /**
     * 加固以及多dex处理方式
     *
     * @param consumer context 上下文 , ClassLoader 类加载器
     */
    private void hookAttach(BiConsumer<Context, ClassLoader> consumer) {
        XposedHelpers.findAndHookMethod(Application.class, "attach",
                Context.class,
                new XC_MethodHook() {
                    public void afterHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            // 获取context上下文
                            Context context = XpContext.getContext((Context) param.args[0]);
                            // 获取classLoader类加载器
                            XpContext.classLoader = context.getClassLoader();
                            if (XpContext.classLoader != null) {
                                consumer.accept(context, XpContext.classLoader);
                            }
                        } catch (Throwable e) {
                            XposedBridge.log(e);
                        }
                    }
                });
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        try {
            ObjectionUtil.hideAll();
//            KillAdsUtil.assets();
            KillAdsUtil.killAds(XpContext.classLoader);
//            KillAdsUtil.killAdPrecise(XpContext.classLoader);
            // 实例化工厂
            BaseHook baseHook = AppFactory.init(param.processName);
            if (baseHook != null) {
                // 调用hook方法
                hookAttach(baseHook::hook);
            }
        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

}
