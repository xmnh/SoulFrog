package xmnh.soulfrog.factory;

import java.util.HashMap;
import java.util.Map;

import xmnh.soulfrog.app.APKPure;
import xmnh.soulfrog.app.AbcVPN;
import xmnh.soulfrog.app.AiClips;
import xmnh.soulfrog.app.AiQiYi;
import xmnh.soulfrog.app.AudioClips;
import xmnh.soulfrog.app.BearVCR;
import xmnh.soulfrog.app.BrainWave;
import xmnh.soulfrog.app.RRVideo;
import xmnh.soulfrog.app.DailyTask;
import xmnh.soulfrog.app.DevCheck;
import xmnh.soulfrog.app.ESBrowse;
import xmnh.soulfrog.app.FakeLocation;
import xmnh.soulfrog.app.FastLinkVPN;
import xmnh.soulfrog.app.InfinityAI;
import xmnh.soulfrog.app.Japanese;
import xmnh.soulfrog.app.JuiceSSH;
import xmnh.soulfrog.app.KuWo;
import xmnh.soulfrog.app.Launcher3;
import xmnh.soulfrog.app.NowMuse;
import xmnh.soulfrog.app.QQLive;
import xmnh.soulfrog.app.SDMaid;
import xmnh.soulfrog.app.SaComic;
import xmnh.soulfrog.app.TapTap;
import xmnh.soulfrog.app.Tarot;
import xmnh.soulfrog.app.Termius;
import xmnh.soulfrog.app.Tick;
import xmnh.soulfrog.app.TikTok;
import xmnh.soulfrog.enums.AppEnum;
import xmnh.soulfrog.interfaces.BaseHook;

public class AppFactory {
    private static final Map<String, BaseHook> HOOKS = new HashMap<>();

    static {
        HOOKS.put(AppEnum.FAKE_LOCATION.getPackageName(), new FakeLocation());
        HOOKS.put(AppEnum.DAILY_TASK.getPackageName(), new DailyTask());
        HOOKS.put(AppEnum.KU_WO.getPackageName(), new KuWo());
        HOOKS.put(AppEnum.AI_CLIPS.getPackageName(), new AiClips());
        HOOKS.put(AppEnum.JUICE_SSH.getPackageName(), new JuiceSSH());
        HOOKS.put(AppEnum.QQ_LIVE.getPackageName(), new QQLive());
        HOOKS.put(AppEnum.DEV_CHECK.getPackageName(), new DevCheck());
        HOOKS.put(AppEnum.BRAIN_WAVE.getPackageName(), new BrainWave());
        HOOKS.put(AppEnum.ES_BROWSE.getPackageName(), new ESBrowse());
        HOOKS.put(AppEnum.NOW_MUSE.getPackageName(), new NowMuse());
        HOOKS.put(AppEnum.SD_MAID.getPackageName(), new SDMaid());
        HOOKS.put(AppEnum.SD_MAID_SE.getPackageName(), new SDMaid.SDMaidSe());
        HOOKS.put(AppEnum.TAROT.getPackageName(), new Tarot());
        HOOKS.put(AppEnum.AUDIO_CLIPS.getPackageName(), new AudioClips());
        HOOKS.put(AppEnum.BEAR_VCR.getPackageName(), new BearVCR());
        HOOKS.put(AppEnum.TICK.getPackageName(), new Tick());
        HOOKS.put(AppEnum.AI_QI_YI.getPackageName(), new AiQiYi());
        HOOKS.put(AppEnum.RR_VIDEO.getPackageName(), new RRVideo());
        HOOKS.put(AppEnum.LAUNCHER3.getPackageName(), new Launcher3());
//        HOOKS.put(AppEnum.LAUNCHER2.getPackageName(), new Launcher2());
        HOOKS.put(AppEnum.JAPANESE.getPackageName(), new Japanese());
        HOOKS.put(AppEnum.ABC_VPN.getPackageName(), new AbcVPN());
        HOOKS.put(AppEnum.TERMIUS.getPackageName(), new Termius());
        HOOKS.put(AppEnum.FAST_LINK_VPN.getPackageName(), new FastLinkVPN());
        HOOKS.put(AppEnum.SA_COMIC.getPackageName(), new SaComic());
        HOOKS.put(AppEnum.APK_PURE.getPackageName(), new APKPure());
        HOOKS.put(AppEnum.INFINITY_AI.getPackageName(), new InfinityAI());
        HOOKS.put(AppEnum.TIKTOK.getPackageName(), new TikTok());
        HOOKS.put(AppEnum.TAP_TAP.getPackageName(), new TapTap());

        // TODO 待填写需要hook的实例
    }

    public static BaseHook init(String packageName) {
        return HOOKS.getOrDefault(packageName, null);
    }

}
