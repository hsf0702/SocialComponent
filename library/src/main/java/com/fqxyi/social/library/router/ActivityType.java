package com.fqxyi.social.library.router;

/**
 * 枚举路由Activity的path值
 */
public enum ActivityType {

    SHARE("share"),
    AUTH("auth")
    ;

    private final String text;

    ActivityType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
