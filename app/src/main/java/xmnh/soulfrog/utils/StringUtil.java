package xmnh.soulfrog.utils;

import java.util.Random;

public class StringUtil {

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str != null && searchStr != null) {
            int len = searchStr.length();
            int max = str.length() - len;
            for (int i = 0; i <= max; ++i) {
                if (str.regionMatches(true, i, searchStr, 0, len)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        for (int i = 0; i < cs.length(); ++i) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param size      生成多少位
     * @param lowercase true为小写 , false为大写
     * @return String
     */
    public static String randomUUID(int size, boolean lowercase) {
        String text = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            sb.append(text.charAt(random.nextInt(text.length())));
        }
        String result = sb.toString();
        return lowercase ? result.toLowerCase() : result;
    }

    public static String randomText(int size, boolean lowercase) {
        String text = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            sb.append(text.charAt(random.nextInt(text.length())));
        }
        String result = sb.toString();
        return lowercase ? result.toLowerCase() : result;
    }

    public static boolean isMD5(String str) {
        String regex = "^[a-fA-F\\d]{32}$";
        return str.matches(regex);
    }

}
