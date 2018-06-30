package com.fqxyi.social.library.auth;

/**
 * 授权结果回调
 */
public interface IAuthCallback {

    void onSuccess(String msg, String response);
    void onError(String msg);
    void onCancel(String msg);

}
