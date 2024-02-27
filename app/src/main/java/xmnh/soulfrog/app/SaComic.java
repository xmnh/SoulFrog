package xmnh.soulfrog.app;

import android.content.Context;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.HookUtil;

public class SaComic implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        Class<?> vipUserInfo = XposedHelpers.findClass("com.comic.isaman.mine.vip.bean.vipUserInfo", classLoader);
        if (vipUserInfo != null){
            HookUtil.booleReplace(vipUserInfo, true);
        }
        Class<?> chapterListItemBean = XposedHelpers.findClass(
                "com.comic.isaman.icartoon.model.ChapterListItemBean", classLoader);
        if (chapterListItemBean != null){
            XposedHelpers.findAndHookMethod(chapterListItemBean, "isAdvanceChapter",
                    XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod(chapterListItemBean, "isNeedBuy",
                    XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod(chapterListItemBean, "isVipFree",
                    XC_MethodReplacement.returnConstant(false));
        }
        Class<?> comicCartoonEpisodeBean = XposedHelpers.findClass(
                "ccom.comic.isaman.cartoon_video.bean.ComicCartoonEpisodeBean", classLoader);
        if (comicCartoonEpisodeBean != null){
            XposedHelpers.findAndHookMethod(comicCartoonEpisodeBean, "isVipFree",
                    XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod(comicCartoonEpisodeBean, "needBuy",
                    XC_MethodReplacement.returnConstant(false));
        }
    }

}
