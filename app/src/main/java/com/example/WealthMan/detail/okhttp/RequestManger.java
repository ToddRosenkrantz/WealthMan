
package com.example.WealthMan.detail.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.WealthMan.detail.JsonUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestManger {
    private static final String TAG = "RequestManger";
    private static RequestManger requestManger;
    public OkHttpClient mOkHttpClient;
    private final Handler mHandler;
    public static RequstCallBack mCallback;

    private RequestManger() {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        mHandler = new Handler(Looper.getMainLooper());

    }

    public static RequestManger getInstance(RequstCallBack callback) {
        mCallback = callback;
        if (requestManger != null) {
            return requestManger;
        } else {
            return new RequestManger();
        }
    }

    public <T> void requestDate(String url, Map params, final Class<T> tClass) {

        Request request = new Request.Builder()
                .url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            private Object t;

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
//                mCallback.onError("fail");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onError("fail");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Gson gson = new Gson();
                try {
                    t = gson.fromJson(response.body().string(), tClass);
                } catch (Exception e) {
                    e.printStackTrace();

                    mCallback.onError("fail");
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onSuccess(t);
                    }
                });
            }
        });
    }

    public <T extends Type> void requestDate(String url, Map params, final T type) {

        Request request = new Request.Builder()
                .url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Gson gson = new Gson();
                final List<?> objects = JsonUtils.parseArray(response.body().string(), type);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onSuccess(objects);
                    }
                });
            }
        });
    }
}

