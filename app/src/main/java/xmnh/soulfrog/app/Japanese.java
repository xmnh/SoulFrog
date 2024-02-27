package xmnh.soulfrog.app;

import android.content.Context;

import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class Japanese implements BaseHook {

    @Override
    public void hook(Context context, ClassLoader classLoader)  {
        context.getSharedPreferences("onlineconfig_agent_online_setting_com.easysay.japanese", 0).edit()
                .putString("goods1_price", "0.01")
                .putString("goods2_price", "0.01")
                .putString("goods3_price", "0.01")
                .putString("goods4_price", "0.01")
                .putString("goods5_price", "0.01")
                .putString("goods1_price_new", "0.01")
                .putString("goods2_price_new", "0.01")
                .putString("goods3_price_new", "0.01")
                .putString("goods4_price_new", "0.01")
                .putString("goods5_price_new", "0.01")
                .putString("wangzifu_price_388", "0.01")
                .putString("wangzifu_price_398", "0.01")
                .apply();
        AppUtil.finish(context);
    }
}
