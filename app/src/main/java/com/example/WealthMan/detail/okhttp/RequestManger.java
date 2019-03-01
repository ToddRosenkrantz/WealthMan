
package com.example.WealthMan.detail.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.WealthMan.detail.JsonUtils;
import com.example.WealthMan.detail.bean.DetailLineBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RequestManger {
    private static final String TAG = "RequestManger";
    private static RequestManger requestManger;
    public OkHttpClient mOkHttpClient;
    private final Handler mHandler;
    public static RequstCallBack mCallback;

    private RequestManger() {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)//设置超时时间
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
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
            public void onResponse(Call call, Response response) {

                Gson gson = new Gson();
                try {
//                    t = gson.fromJson(response.body().string(), tClass);
                    int code = response.code();
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        String string = body.string();
                        Log.e(TAG, "onResponse: " + string);
                        gsonSpecial(string);
                    }

                    //最后就可以通过刚刚得到的key值去解析后面的json了

                } catch (Exception e) {
                    e.printStackTrace();

                    mCallback.onError("fail");
                }

//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mCallback.onSuccess(t);
//                    }
//                });
            }
        });
    }

    private void gsonSpecial(String jsonData) {

        JSONObject jsonObject = null;
        try {
            Log.i(TAG, "gsonSpecial: " + jsonData);
            jsonObject = new JSONObject(jsonData);
            Log.i(TAG, "gsonSpecial:jsonObject " + jsonObject.toString());
            //通过迭代器获取这段json当中所有的key值
            Iterator keys = jsonObject.keys();
            //然后通过一个循环取出所有的key值
            while (keys.hasNext()) {
                String key = String.valueOf(keys.next());
                JSONObject newJson = jsonObject.getJSONObject(key);
                final DetailLineBean.Detaildate detaildate = new Gson().fromJson(newJson.toString(), DetailLineBean.Detaildate.class);
                Log.i(TAG, "gsonSpecial: detailDate = " + detaildate.quote.close);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onSuccess(detaildate);
                    }
                });
                break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

