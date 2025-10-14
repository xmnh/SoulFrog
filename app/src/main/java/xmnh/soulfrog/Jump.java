package xmnh.soulfrog;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.function.BiConsumer;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import xmnh.soulfrog.context.XpContext;
import xmnh.soulfrog.factory.AppFactory;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.HookUtil;
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
                        // 获取context上下文
                        Context context = XpContext.getContext((Context) param.args[0]);
                        try {
                            // 获取classLoader类加载器
                            XpContext.classLoader = context.getClassLoader();
                        } catch (XposedHelpers.ClassNotFoundError e) {
                            XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
                                public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
                                    Class<?> result = (Class<?>) param.getResult();
                                    Log.d("SoulFrog", "loadClass => " + result);
                                    if (result == null) {
                                        return;
                                    }
                                    XpContext.classLoader = result.getClassLoader();
                                }
                            });
                        } finally {
                            if (XpContext.classLoader != null) {
                                consumer.accept(XpContext.getContext(context), XpContext.classLoader);
                            }
                        }
                    }
                });
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam param) throws Throwable {
        try {
            ObjectionUtil.hideAll();
            // 实例化工厂
            BaseHook baseHook = AppFactory.init(param.processName);
            // 调用hook方法
            hookAttach((context, classLoader) -> {
                if (baseHook != null) {
                    baseHook.hook(context, classLoader);
                }
                HookUtil.tapTap(context, classLoader);
            });

        } catch (Throwable e) {
            XposedBridge.log(e);
        }
    }

}
