package xmnh.soulfrog.app;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import xmnh.soulfrog.application.SoulFrog;
import xmnh.soulfrog.interfaces.BaseHook;
import xmnh.soulfrog.utils.AppUtil;

public class DevCheck implements BaseHook {

    private String androidId;
    private String tag;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String KEY_ALGORITHM = "AES";
    private byte[] encryptionKey;
    private byte[] hmacKey;

    @Override
    public void hook(Context context, ClassLoader classLoader) {

        // flar2.devcheck_preferences
        XposedHelpers.findAndHookMethod(Settings.Secure.class, "getString",
                ContentResolver.class, String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        String name = (String) param.args[1];
                        if ("android_id".equals(name)) {
                            androidId = (String) param.getResult();
                            tag = androidId + "prefCPUInfo";
                            Log.d(SoulFrog.TAG, "DevCheck tag => " + tag);
                        }
                    }
                });

        
        XposedHelpers.findAndHookMethod("flar2.devcheck.b.B0", classLoader, "t",
                XC_MethodReplacement.returnConstant(true));
        AppUtil.finish(context);
    }

    // 初始化加密密钥
    private void initKeys(String password, byte[] salt) {
        try {
            // PBKDF2实现简化版 - 在实际应用中应该使用标准的PBKDF2实现
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] key = md.digest((password + new String(salt)).getBytes("UTF-8"));
            
            // 从派生的密钥中提取AES密钥(前16字节)和HMAC密钥(后16字节)
            encryptionKey = Arrays.copyOfRange(key, 0, 16);
            hmacKey = Arrays.copyOfRange(key, 16, 32);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // i(str) 对键进行 SHA-256 哈希
    private String encryptKey(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // e(Boolean.toString(z)) 对值进行 AES 加密
    private String encryptValue(String value) {
        try {
            if (encryptionKey == null || hmacKey == null) {
                // 使用默认密码和盐值初始化密钥
                initKeys("defaultPassword", "defaultSalt".getBytes());
            }
            
            // 生成随机IV
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            
            // AES加密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey, KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
            
            // HMAC签名
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec hmacKeySpec = new SecretKeySpec(hmacKey, HMAC_ALGORITHM);
            mac.init(hmacKeySpec);
            byte[] hmac = mac.doFinal(encrypted);
            
            // 组合IV、加密数据和HMAC
            byte[] result = new byte[iv.length + encrypted.length + hmac.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);
            System.arraycopy(hmac, 0, result, iv.length + encrypted.length, hmac.length);
            
            // Base64编码返回
            return android.util.Base64.encodeToString(result, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    // k("prefHardwareDB", true) 方法的实现
    public boolean k(String str, boolean z) {
        // 对键进行SHA-256哈希
        String hashedKey = encryptKey(str);
        // 对值进行AES加密
        String encryptedValue = encryptValue(Boolean.toString(z));
        
        // 在实际应用中，这里会将加密后的键值对存储到SharedPreferences中
        // 由于这是Xposed模块，我们可能需要Hook SharedPreferences的相关方法来实现存储
        
        Log.d(SoulFrog.TAG, "Encrypted key: " + hashedKey);
        Log.d(SoulFrog.TAG, "Encrypted value: " + encryptedValue);
        
        return z; // 返回原始值
    }
}