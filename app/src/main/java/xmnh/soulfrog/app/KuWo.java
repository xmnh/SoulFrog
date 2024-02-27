package xmnh.soulfrog.app;

import android.content.Context;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;
import xmnh.soulfrog.utils.HookUtil;

public class KuWo implements BaseHook {

    private int appVersionCode;

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        appVersionCode = AppUtil.getAppVersionCode(context);
//        Class<?> appInfo = XposedHelpers.findClass("cn.kuwo.base.utils.AppInfo", classLoader);
//        if (appInfo != null) {
//            Field field = XposedHelpers.findField(appInfo, "INSTALL_CHANNEL");
//            Log.e(SoulFrog.TAG, field.toString());
////            XposedHelpers.setStaticObjectField(appInfo,"INSTALL_CHANNEL", "tunknown");
//        } else {
//            Class<?> cls = XposedHelpers.findClass("cn.kuwo.base.utils.b", classLoader);
//            if (cls != null) {
//                Field field = XposedHelpers.findField(cls, "h");
//                Log.e(SoulFrog.TAG, field.toString());
//                XposedHelpers.setStaticObjectField(cls, "h", "tunknown");
//            }
//        }
//        if (appVersionCode >= 10050) {
//            book(classLoader);
//        }
////        vip();
//        download(classLoader);
        theme(classLoader);
        ad(classLoader);
////        privilege();
////        config();
////        pay();
        menu(classLoader);
        ui(classLoader);
//        kuWo(classLoader);
////        HookUtil.okhttp();

        AppUtil.finish(context);
    }

    private void download(ClassLoader classLoader) {
        Class<?> vipConfigMgr = XposedHelpers.findClass("cn.kuwo.peculiar.specialdialogconfig.VipConfigMgr", classLoader);
        if (vipConfigMgr != null) {
            XposedHelpers.findAndHookMethod(vipConfigMgr, "isEncryptDownOpen", XC_MethodReplacement.returnConstant(false));
        }
    }

    private void book(ClassLoader classLoader) {
        // 看小说
        Class<?> bookAdMgr = XposedHelpers.findClass("cn.kuwo.ui.book.ad.BookAdMgr", classLoader);
        if (bookAdMgr != null) {
            HookUtil.booleAndVoidReplace(bookAdMgr, false);
        }
    }

    private void kuWo(ClassLoader classLoader) {
        Class<?> specialRealInfo = XposedHelpers.findClass("cn.kuwo.peculiar.specialinfo.SpecialRealInfo", classLoader);
        if (specialRealInfo != null) {
            XposedHelpers.findAndHookMethod(specialRealInfo, "getState", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.setIntField(param.thisObject, "state", 1);
                    XposedHelpers.setLongField(param.thisObject, "leftDays", 666666L);
                    XposedHelpers.setLongField(param.thisObject, "endTime", 218330035688000L);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.setIntField(param.thisObject, "state", 1);
                    XposedHelpers.setLongField(param.thisObject, "leftDays", 666666L);
                    XposedHelpers.setLongField(param.thisObject, "endTime", 218330035688000L);
                    param.setResult(1);
                }
            });
        }
        Class<?> specialInfoUtil = XposedHelpers.findClass("cn.kuwo.peculiar.specialinfo.SpecialInfoUtil", classLoader);
        if (specialInfoUtil != null) {
            XposedHelpers.findAndHookMethod(specialInfoUtil, "isSuperVipUser", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(specialInfoUtil, "isTingshuVipUser", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(specialInfoUtil, "isVipUser", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(specialInfoUtil, "isLuxuryVipUser", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(specialInfoUtil, "isMusicPayUser", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(specialInfoUtil, "isLuxuryVipInvalid", XC_MethodReplacement.returnConstant(false));
        }
        Class<?> music = XposedHelpers.findClass("cn.kuwo.base.bean.Music", classLoader);
        if (music != null) {
            XposedHelpers.findAndHookMethod(music, "isSpPrivilege", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.setIntField(param.thisObject, "spPrivilege", 1);
                    XposedHelpers.setBooleanField(param.thisObject, "disable", false);
                    XposedHelpers.setBooleanField(param.thisObject, "isNewPay", false);
                    XposedHelpers.setBooleanField(param.thisObject, "canDownload", true);
                    XposedHelpers.setBooleanField(param.thisObject, "canOnlinePlay", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isLimitFree", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isVipPay", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isSongPay", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isAlbumPay", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isTingshuVipPay", true);
                    param.setResult(true);
                }
            });
//            XposedHelpers.findAndHookMethod(music, "getSpPrivilege", XC_MethodReplacement.returnConstant(1));
            XposedHelpers.findAndHookMethod(music, "isListenVip", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(music, "vaild", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(music, "isDownloadVip", XC_MethodReplacement.returnConstant(true));
            Class<?> payInfoBean = XposedHelpers.findClass("cn.kuwo.base.bean.Music$PayInfoBean", classLoader);
            if (payInfoBean != null) {
                XposedHelpers.findAndHookMethod(payInfoBean, "getCannotOnlinePlay", XC_MethodReplacement.returnConstant(0));
                XposedHelpers.findAndHookMethod(payInfoBean, "getCannotDownload", XC_MethodReplacement.returnConstant(0));
            }
            Class<?> musicChargeManager = XposedHelpers.findClass("cn.kuwo.peculiar.speciallogic.MusicChargeManager", classLoader);
            if (musicChargeManager != null) {
                XposedHelpers.findAndHookMethod(musicChargeManager, "checkMusicBeforeSingleBuy", music,
                        XC_MethodReplacement.returnConstant(true));
            }
        }
        Class<?> consumptionQueryUtil = XposedHelpers.findClass("cn.kuwo.peculiar.speciallogic.ConsumptionQueryUtil", classLoader);
        if (consumptionQueryUtil != null) {
            XposedHelpers.findAndHookMethod(consumptionQueryUtil, "onwLimited", long.class, List.class,
                    XC_MethodReplacement.returnConstant(true));
        }
    }

    private void vip(ClassLoader classLoader) {
        Class<?> specialInfoUtil = XposedHelpers.findClass("cn.kuwo.peculiar.specialinfo.SpecialInfoUtil", classLoader);
        if (specialInfoUtil != null) {
            for (Method method : specialInfoUtil.getDeclaredMethods()) {
                if (method.getReturnType() == int.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(1));
                }
                if (method.getReturnType() == boolean.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(true));
                }
            }
            if (appVersionCode >= 10200) {
                XposedHelpers.findAndHookMethod(specialInfoUtil, "getSuperVipGrade", XC_MethodReplacement.returnConstant(7));
                XposedHelpers.findAndHookMethod(specialInfoUtil, "getVipGrade", XC_MethodReplacement.returnConstant(7));
                XposedHelpers.findAndHookMethod(specialInfoUtil, "isLuxuryVipInvalid", XC_MethodReplacement.returnConstant(false));
            }
//            XposedHelpers.findAndHookMethod(specialInfoUtil, "isSuperVipUser", XC_MethodReplacement.returnConstant(true));
//            XposedHelpers.findAndHookMethod(specialInfoUtil, "isTingshuVipUser", XC_MethodReplacement.returnConstant(true));
//            XposedHelpers.findAndHookMethod(specialInfoUtil, "isVipUser", XC_MethodReplacement.returnConstant(true));
//            XposedHelpers.findAndHookMethod(specialInfoUtil, "isLuxuryVipUser", XC_MethodReplacement.returnConstant(true));
//            XposedHelpers.findAndHookMethod(specialInfoUtil, "isMusicPayUser", XC_MethodReplacement.returnConstant(true));
        }
        Class<?> music = XposedHelpers.findClass("cn.kuwo.base.bean.Music", classLoader);
        if (music != null) {
            XposedHelpers.findAndHookMethod(music, "hasAd", int.class, XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod(music, "isSpPrivilege", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.setIntField(param.thisObject, "spPrivilege", 1);
                    XposedHelpers.setBooleanField(param.thisObject, "disable", false);
                    XposedHelpers.setBooleanField(param.thisObject, "isNewPay", false);
                    XposedHelpers.setBooleanField(param.thisObject, "canDownload", true);
                    XposedHelpers.setBooleanField(param.thisObject, "canOnlinePlay", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isLimitFree", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isVipPay", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isSongPay", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isAlbumPay", true);
                    XposedHelpers.setBooleanField(param.thisObject, "isTingshuVipPay", true);
                    param.setResult(true);
                }
            });
            XposedHelpers.findAndHookMethod(music, "getSpPrivilege", XC_MethodReplacement.returnConstant(1));
            XposedHelpers.findAndHookMethod(music, "isListenVip", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(music, "vaild", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(music, "isDownloadVip", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(music, "isPlayFree", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(music, "isOverseasPlayFree", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(music, "isOverseasDownloadFree", XC_MethodReplacement.returnConstant(true));
        }
        Class<?> userBgResItem = XposedHelpers.findClass("cn.kuwo.ui.mine.usercenter.UserBgResItem", classLoader);
        if (userBgResItem != null) {
            XposedHelpers.findAndHookMethod(userBgResItem, "isSuperVipRight", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(userBgResItem, "isVipRight", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(userBgResItem, "isVipRenewRight", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(userBgResItem, "isLimitFree", XC_MethodReplacement.returnConstant(true));
        }
//        Class<?> loginListUtils = XposedHelpers.findClass("cn.kuwo.ui.userinfo.utils.LoginListUtils", classLoader);
//        if (userBgResItem != null) {
//            XposedHelpers.findAndHookMethod(loginListUtils, "getVipTypeIcon", int.class, new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    param.args[0] = 7;
//                }
//            });
//            XposedHelpers.findAndHookMethod(loginListUtils, "getVipTypeString", int.class, new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    param.args[0] = 7;
//                }
//            });
//        }
        Class<?> vipInfo = XposedHelpers.findClass("cn.kuwo.base.bean.VipInfo", classLoader);
        if (vipInfo != null) {
            XposedHelpers.findAndHookMethod(vipInfo, "getVipType", XC_MethodReplacement.returnConstant(2));
            XposedHelpers.findAndHookMethod(vipInfo, "getVipExpire", XC_MethodReplacement.returnConstant(Integer.MAX_VALUE));
        }
    }

    private void theme(ClassLoader classLoader) {
        Class<?> starTheme = XposedHelpers.findClass("cn.kuwo.mod.theme.bean.star.StarTheme", classLoader);
        Class<?> starThemeDetailPresenter;
        // 主题
        try {
            starThemeDetailPresenter = XposedHelpers.findClass("cn.kuwo.mod.theme.detail.star.StarThemeDetailPresenter", classLoader);
        } catch (Exception e) {
            starThemeDetailPresenter = XposedHelpers.findClass("cn.kuwo.mod.theme.detail.star.c", classLoader);
        }
        if (starThemeDetailPresenter != null) {
            for (Method method : starThemeDetailPresenter.getDeclaredMethods()) {
                if (method.getModifiers() == Modifier.PRIVATE && method.getReturnType() == boolean.class
                        && method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == starTheme) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(true));
                }
            }
//            XposedHelpers.findAndHookMethod(starThemeDetailPresenter, "checkStarThemeFree",
//                    XposedHelpers.findClass("cn.kuwo.mod.theme.bean.star.StarTheme", classLoader),
//                    XC_MethodReplacement.returnConstant(true));
        }
    }

    private void ad(ClassLoader classLoader) {
        // 开屏ad
        Class<?> entryActivity = XposedHelpers.findClass("cn.kuwo.player.activities.EntryActivity", classLoader);
        if (entryActivity != null) {
            for (Method method : entryActivity.getDeclaredMethods()) {
                if (method.getModifiers() == Modifier.PRIVATE && method.getReturnType() == boolean.class
                        && method.getParameterTypes().length == 0) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(true));
                }
            }
        }
        Class<?> screenAdUtils = XposedHelpers.findClass("cn.kuwo.player.screen.ScreenAdUtils", classLoader);
        if (screenAdUtils != null) {
            HookUtil.booleReplace(screenAdUtils, false);
        }
        Class<?> competitiveAdMgr = XposedHelpers.findClass("cn.kuwo.mod.mobilead.tmead.competitive.CompetitiveAdMgr", classLoader);
        if (competitiveAdMgr != null) {
            HookUtil.booleAndVoidReplace(competitiveAdMgr, false);
        }
        Class<?> kuWoAdUrl = XposedHelpers.findClass("cn.kuwo.mod.mobilead.KuwoAdUrl", classLoader);
        if (kuWoAdUrl != null) {
            for (Method method : kuWoAdUrl.getDeclaredMethods()) {
                if (method.getReturnType() == String.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(""));
                }
            }
            XposedHelpers.findAndHookMethod("cn.kuwo.mod.mobilead.KuwoAdUrl$AdUrlDef", classLoader,
                    "getUrl", String.class, XC_MethodReplacement.returnConstant(""));
        }
    }

    private void privilege(ClassLoader classLoader) {
        Class<?> specialRealInfo = XposedHelpers.findClass("cn.kuwo.peculiar.specialinfo.SpecialRealInfo", classLoader);
        if (specialRealInfo != null) {
            for (Method method : specialRealInfo.getDeclaredMethods()) {
                if (method.getReturnType() == int.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(1));
                }
                if (method.getReturnType() == long.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(218330035688000L));
                }
            }
        }
        Class<?> songPrivilegeHelper = XposedHelpers.findClass("com.kugou.fanxing.allinone.watch.mobilelive.user.helper.SongPrivilegeHelper", classLoader);
        if (songPrivilegeHelper != null) {
            XposedHelpers.findAndHookMethod(songPrivilegeHelper, "verifyPrivilege", int.class,
                    XC_MethodReplacement.returnConstant(1));
        }
        Class<?> consumptionQueryUtil = XposedHelpers.findClass("cn.kuwo.peculiar.speciallogic.ConsumptionQueryUtil", classLoader);
        if (consumptionQueryUtil != null) {
            XposedHelpers.findAndHookMethod(consumptionQueryUtil, "onwLimited", long.class, List.class,
                    XC_MethodReplacement.returnConstant(true));
        }
        Class<?> privilegeManager = XposedHelpers.findClass("cn.kuwo.peculiar.speciallogic.PrivilegeManager", classLoader);
        if (privilegeManager != null) {
            for (Method method : privilegeManager.getDeclaredMethods()) {
                if (method.getReturnType() == int.class) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(1));
                }
                if (method.getReturnType() == boolean.class) {
                    XposedBridge.hookMethod(method, new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                            XposedHelpers.setIntField(param.thisObject, "searchLimit", 666666);
                            return false;
                        }
                    });
                }
            }
        }
        Class<?> digitAlbum = XposedHelpers.findClass("cn.kuwo.mod.search.DigitAlbum", classLoader);
        if (digitAlbum != null) {
            XposedHelpers.findAndHookMethod(digitAlbum, "isHadBought", XC_MethodReplacement.returnConstant(true));
        }
    }

    private void config(ClassLoader classLoader) {
        Class<?> vipConfigMgr = XposedHelpers.findClass("cn.kuwo.peculiar.specialdialogconfig.VipConfigMgr", classLoader);
        if (vipConfigMgr != null) {
            XposedHelpers.findAndHookMethod(vipConfigMgr, "isShowClickListen30Dialog", XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod(vipConfigMgr, "isZhizhenOnTrial", XC_MethodReplacement.returnConstant(true));

        }
        Class<?> confMgr = XposedHelpers.findClass("cn.kuwo.base.config.ConfMgr", classLoader);
        if (confMgr != null) {
            Class<?> specialInfoMgr = XposedHelpers.findClass("cn.kuwo.peculiar.specialinfo.SpecialInfoMgr", classLoader);
            if (specialInfoMgr != null) {
/*                XposedHelpers.findAndHookMethod(specialInfoMgr, "getVipIconUrl", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("vipUrl: " + param.getResult().toString());
                    }
                });*/
            }
        }
    }

    private void pay(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod(JSONObject.class, "optString", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String str = param.args[0].toString();
                JSONObject jsonObject = (JSONObject) param.thisObject;
                if (jsonObject.has("playright")) {
                    jsonObject.put("playright", "1");
                }
                if ("playright".equals(str) || "downright".equals(str)) {
                    param.setResult("1");
                }
//                if ("vip".equals(str) || "song".equals(str) || "album".equals(str) || "bookvip".equals(str)) {
//                    param.setResult("0");
//                }
            }
        });
    }

    private void ui(ClassLoader classLoader) {
        Class<?> mainController = XposedHelpers.findClass("cn.kuwo.ui.fragment.MainController", classLoader);
        if (mainController != null) {
            XposedHelpers.findAndHookMethod(mainController, "onCreate",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ViewGroup viewGroup = (ViewGroup) XposedHelpers.getObjectField(param.thisObject, "mBottomTab");
                            viewGroup.removeViewAt(1);// 视频
                            viewGroup.removeViewAt(2);// 会员
                        }
                    });
        }
        Class<?> app = XposedHelpers.findClass("cn.kuwo.player.App", classLoader);
        if (app != null) {
            XposedHelpers.findAndHookMethod(app, "initKuGou", XC_MethodReplacement.returnConstant(false));
        }
    }

    private void menu(ClassLoader classLoader) {
        Class<?> menuController = XposedHelpers.findClass("cn.kuwo.ui.fragment.menu.MenuController", classLoader);
        if (menuController != null) {
            XposedHelpers.findAndHookMethod(menuController, "refreshFlowItem", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedHelpers.callMethod(param.thisObject, "hideMenuItem", 7);
                    XposedHelpers.callMethod(param.thisObject, "hideMenuItem", 12);
                    XposedHelpers.callMethod(param.thisObject, "hideMenuItem", 13);
                    XposedHelpers.callMethod(param.thisObject, "hideMenuItem", 20);
                    XposedHelpers.callMethod(param.thisObject, "hideMenuItem", 32);
                    XposedHelpers.callMethod(param.thisObject, "hideMenuItem", 33);
                    XposedHelpers.callMethod(param.thisObject, "hideMenuItem", 35);
                }
            });
        }
    }

}
