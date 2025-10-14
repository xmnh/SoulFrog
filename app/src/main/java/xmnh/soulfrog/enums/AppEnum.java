package xmnh.soulfrog.enums;

public enum AppEnum {
    KU_WO("酷我音乐", "cn.kuwo.player"),
    QQ_LIVE("腾讯视频", "com.tencent.qqlive"),
    DAILY_TASK("DailyTask", "com.moyan.dailytask"),
    FAKE_LOCATION("FakeLocation", "com.lerist.fakelocation"),
    AI_CLIPS("爱剪辑", "com.shineyie.aijianji"),
    JUICE_SSH("JuiceSSH", "com.sonelli.juicessh"),
    DEV_CHECK("DevCheck", "flar2.devcheck"),
    BRAIN_WAVE("神奇脑波", "imoblife.brainwavestus"),
    NOW_MUSE("Now冥想", "com.imoblife.now"),
    ES_BROWSE("ES文件浏览器", "com.estrongs.android.pop"),
    SD_MAID("SdMaid", "eu.thedarken.sdm"),
    SD_MAID_SE("SdMaidSE", "eu.darken.sdmse"),
    TAROT("塔罗牌占卜", "taluo.jumeng.com.tarot"),
    AUDIO_CLIPS("音频剪辑大师", "com.lixiangdong.songcutter"),
    BEAR_VCR("小熊录屏", "com.duapps.recorder"),
    TICK("滴答清单", "cn.ticktick.task"),
    AI_QI_YI("爱奇艺", "com.qiyi.video"),
    RR_VIDEO("人人视频", "com.example.pptv"),
    LAUNCHER3("雷电launcher3", "com.android.launcher3"),
    ABC_VPN("ABC加速器","com.zfast.xyz"),
    JAPANESE("日语U学院", "com.easysay.japanese"),
    FAST_LINK_VPN("快连加速器","world.letsgo.booster.android.pro"),
    TERMIUS("Termius","com.server.auditor.ssh.client"),
    SA_COMIC("飒漫画","com.comic.isaman"),
    APK_PURE("APKPure","com.apkpure.aegon"),
    INFINITY_AI("无界AI","com.blockmeta.trade"),
    LAUNCHER2("逍遥launcher2", "com.microvirt.launcher2"),
    YOUTUBE("YouTube", "com.google.android.youtube"),
    TIKTOK("TikTok", "com.zhiliaoapp.musically"),

    GUI_GU("鬼谷八荒", "com.guigugame.guigubahuang"),
    TAP_TAP("TapTap", "com.taptap"),

    // TODO 待填写需要hook的枚举
    ;

    private final String appName;
    private final String packageName;

    AppEnum(String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public static AppEnum getAppEnum(String packageName) {
        for (AppEnum appEnum : values()) {
            if (appEnum.getPackageName().equals(packageName)) {
                return appEnum;
            }
        }
        return null;
    }

}
