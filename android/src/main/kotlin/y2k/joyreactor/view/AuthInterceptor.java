package y2k.joyreactor.view;


import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import y2k.joyreactor.http.HttpClient;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request original = chain.request();
        String cookie = HttpClient.Companion.getInstance().getCookie();
        final Request.Builder requestBuilder = original.newBuilder()
                .header("Cookie", cookie)
                .header("Accept-Encoding", "gzip")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko")
                .method(original.method(), original.body());
        return chain.proceed(requestBuilder.build());
    }
}