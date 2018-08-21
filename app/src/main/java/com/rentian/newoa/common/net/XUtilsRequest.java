package com.rentian.newoa.common.net;//package com.rentian.rentianoa.common.net;
//
//import org.xutils.common.Callback;
//import org.xutils.ex.HttpException;
//import org.xutils.http.RequestParams;
//import org.xutils.x;
//
///**
// * Created by rentianjituan on 2016/8/2.
// */
//public class XUtilsRequest {
//    public RequestNet mListener;
//
//    public interface RequestNet{
//        public void requestSuccess(String result);
//        public void requestFail(String result);
//
//    }
//
//    public  void requestByNet(String url, String sendContent
//    , final RequestNet mListener){
//       this.mListener = mListener;
//        RequestParams params = new RequestParams(url+sendContent);
//// 默认缓存存活时间, 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
//        params.setCacheMaxAge(1000 * 60);
//        Callback.Cancelable cancelable
//                // 使用CacheCallback, xUtils将为该请求缓存数据.
//                = x.http().get(params, new Callback.CacheCallback<String>() {
//
//            private boolean hasError = false;
//            private String result = null;
//
//            @Override
//            public boolean onCache(String result) {
//                // 得到缓存数据, 缓存过期后不会进入这个方法.
//                // 如果服务端没有返回过期时间, 参考params.setCacheMaxAge(maxAge)方法.
//                //
//                // * 客户端会根据服务端返回的 header 中 max-age 或 expires 来确定本地缓存是否给 onCache 方法.
//                //   如果服务端没有返回 max-age 或 expires, 那么缓存将一直保存, 除非这里自己定义了返回false的
//                //   逻辑, 那么xUtils将请求新数据, 来覆盖它.
//                //
//                // * 如果信任该缓存返回 true, 将不再请求网络;
//                //   返回 false 继续请求网络, 但会在请求头中加上ETag, Last-Modified等信息,
//                //   如果服务端返回304, 则表示数据没有更新, 不继续加载数据.
//                //
////                Toast.makeText(MainActivity.this, "缓存" + result, Toast.LENGTH_SHORT).show();
//                mListener.requestSuccess(result);
//
//                return true; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
//                if (result != null) {
//                    this.result = result;
//
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                hasError = true;
//                mListener.requestFail(ex.getMessage());
////                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
//                if (ex instanceof HttpException) { // 网络错误
//                    HttpException httpEx = (HttpException) ex;
//                    int responseCode = httpEx.getCode();
//                    String responseMsg = httpEx.getMessage();
//                    String errorResult = httpEx.getResult();
//                    // ...
//                } else { // 其他错误
//                    // ...
//                }
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
////                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFinished() {
//                if (!hasError && result != null) {
//                    // 成功获取数据
////                    Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
//                    mListener.requestSuccess(result);
//                }
//            }
//        });
//
//    }
//
//}
