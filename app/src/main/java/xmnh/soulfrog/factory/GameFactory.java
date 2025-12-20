package xmnh.soulfrog.factory;

import java.util.HashMap;
import java.util.Map;

import xmnh.soulfrog.interfaces.BaseHook;

public class GameFactory {
    private static final Map<String, BaseHook> HOOKS = new HashMap<>();

    static {
//        HOOKS.put();
        // TODO 待填写需要hook的实例
    }

    public static BaseHook init(String packageName) {
        return HOOKS.getOrDefault(packageName, null);
    }

}
