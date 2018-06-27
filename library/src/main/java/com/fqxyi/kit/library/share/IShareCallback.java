package com.fqxyi.kit.library.share;

/**
 * 分享结果回调
 */
public interface IShareCallback {

    void onSuccess();
    void onError(String msg);
    void onCancel();

}
