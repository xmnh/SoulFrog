package xmnh.soulfrog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import xmnh.soulfrog.application.SoulFrog;

public class AppUtil {
    private static Toast toast;
    public static SharedPreferences sp;

    /**
     * 获取app版本名称
     *
     * @return 返回版本名称
     */
    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), Context.MODE_PRIVATE)
                    .versionName;
        } catch (Exception e) {
            Log.e(SoulFrog.TAG, "getAppVersionName error" + e);
        }
        return "";
    }

    /**
     * 获取app版本号
     *
     * @return 返回版本号
     */
    public static int getAppVersionCode(Context context) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), Context.MODE_PRIVATE)
                    .versionCode;
        } catch (Exception e) {
            Log.e(SoulFrog.TAG, "getAppVersionCode error" + e);
        }
        return 0;
    }

    /**
     * 吐司弹窗
     *
     * @param str 内容
     */
    public static void finish(Context context, String str) {
        if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else {
            toast.setText(str);
        }
        toast.show();
    }

    public static void finish(Context context) {
        String text = context.getPackageName() + " : ~ start running ~";
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 检查sp
     *
     * @param deviceId 设备id
     * @param time     加减时间
     * @return true/false
     */
    public static boolean checkSp(String deviceId, long time) {
        // 默认加时间为40分钟
        long expireTime = 2_400_000;
        long currentTime = System.currentTimeMillis();
        // 不包含键或当前时间大于等于过期时间，则进行put操作
        if (!sp.contains("expire_time") || currentTime >= sp.getLong("expire_time", 0)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("device_id", deviceId);
            editor.putLong("expire_time", currentTime + expireTime + time);
            editor.apply();
            return true;
        }
        return false;
    }

    public static void resetApp(String packageName) {
        try {
            Runtime.getRuntime().exec("pm clear " + packageName);
        } catch (IOException e) {
            Log.e(SoulFrog.TAG, "resetApp error" + e);
        }

    }

    public static void resetDataDir(Context context, String packageName) {
        if (StringUtil.isEmpty(packageName)) return;
        File dataDir = context.getDataDir();
        File filesDir = context.getFilesDir();
//        File externalFilesDir = context.getExternalFilesDir("");
//        File cacheDir = context.getCacheDir();
        if (packageName.equals(context.getPackageName())) {
            deleteDirectory(dataDir);
            deleteDirectory(filesDir);
//            deleteDirectory(externalFilesDir);
//            deleteDirectory(cacheDir);
        }
    }

    private static void deleteDirectory(File file) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    deleteDirectory(subFile);
                }
            }
        }
        boolean delete = file.delete();
        Log.d(SoulFrog.TAG, delete ? "删除成功" : "删除失败");
    }

}
