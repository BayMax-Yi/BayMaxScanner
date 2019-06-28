package com.yt.android.zxing.callback;

import com.google.zxing.Result;

/**
 * @author BayMax·Yi
 * @time 2019/6/18 11:49
 * @modiffy
 * @modiffyTime 2019/6/18 11:49
 * @describe 类描述
 */
public interface OnDecodedResultListener {
    void onDecodedResult(Result rawResult);
}
