package com.fqxyi.social.library.pay;

/**
 * 支付结果回调
 */
public interface IPayCallback {

    void onSuccess(int socialType, String msg);
    void onError(int socialType, String msg);
    void onCancel(int socialType);

}
