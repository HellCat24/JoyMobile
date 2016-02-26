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
        final Request.Builder requestBuilder = original.newBuilder()
                .header("Referer", "http://joyreactor.cc/")
                .method(original.method(), original.body());
        return chain.proceed(requestBuilder.build());
    }
}