package xmnh.soulfrog.enums;

public enum GameEnum {


    // TODO 待填写需要hook的枚举
    ;

    private final String appName;
    private final String packageName;

    GameEnum(String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public static GameEnum getAppEnum(String packageName) {
        for (GameEnum appEnum : values()) {
            if (appEnum.getPackageName().equals(packageName)) {
                return appEnum;
            }
        }
        return null;
    }

}
