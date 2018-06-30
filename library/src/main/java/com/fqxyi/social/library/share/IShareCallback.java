package com.fqxyi.social.library.share;

/**
 * 分享结果回调
 */
public interface IShareCallback {

    void onSuccess(String msg, String response);
    void onError(String msg);
    void onCancel(String msg);

}
