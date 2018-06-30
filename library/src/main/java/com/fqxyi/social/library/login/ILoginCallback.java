package com.fqxyi.social.library.login;

/**
 * 登录结果回调
 */
public interface ILoginCallback {

    void onSuccess(String msg, String response);
    void onError(String msg);
    void onCancel(String msg);

}
