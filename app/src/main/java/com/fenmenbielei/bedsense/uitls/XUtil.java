//package com.fenmenbielei.bedsense.uitls;
//
//import android.util.Log;
//
//import org.xutils.common.Callback;
//import org.xutils.http.RequestParams;
//import org.xutils.x;
//
//import java.util.Map;
//
///**
// * Created by d on 2016/10/12.
// * <p>
// * fd
// */
//public class XUtil {
//    /**
//     * 发送get请求
//     *
//     * @param <T>
//     */
//    public static <T> Callback.Cancelable Get(String url, Map<String, String> map, Callback.CommonCallback<T> callback) {
//        RequestParams params = new RequestParams(url);
//        if (null != map) {
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                params.addQueryStringParameter(entry.getKey(), entry.getValue());
//            }
//        }
//        Callback.Cancelable cancelable = x.http().get(params, callback);
//        return cancelable;
//    }
//
//    /**
//     * 发送get请求
//     *
//     * @param <T>
//     */
//    public static <T> Callback.Cancelable Get(String url, Callback.CommonCallback<T> callback) {
//        RequestParams params = new RequestParams(url);
//
//        Callback.Cancelable cancelable = x.http().get(params, callback);
//        return cancelable;
//    }
//
//    /**
//     * 发送post请求
//     *
//     * @param <T>
//     */
//    public static <T> Callback.Cancelable Post(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
//        RequestParams params = new RequestParams(url);
//        if (null != map) {
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                params.addParameter(entry.getKey(), entry.getValue());
//            }
//        }
//        Callback.Cancelable cancelable = x.http().post(params, callback);
//        Log.e("=======url===", "=======url====" + params);
//        return cancelable;
//    }
//
//    /**
//     * 发送post请求
//     *
//     * @param <T>
//     */
//    public static <T> Callback.Cancelable Post(String url, Callback.CommonCallback<T> callback) {
//        RequestParams params = new RequestParams(url);
//
//        Callback.Cancelable cancelable = x.http().post(params, callback);
//        return cancelable;
//    }
//
//
//    /**
//     * 下载文件
//     *
//     * @param <T>
//     */
//    public static <T> Callback.Cancelable DownLoadFile(String url, String filepath, Callback.CommonCallback<T> callback) {
//        RequestParams params = new RequestParams(url);
//        //设置断点续传
//        params.setAutoResume(true);
//        params.setSaveFilePath(filepath);
//        Callback.Cancelable cancelable = x.http().get(params, callback);
//        return cancelable;
//    }
//
//
//}
//
//
//
//
//
