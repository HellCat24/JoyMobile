package y2k.joyreactor.image;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import y2k.joyreactor.enteties.Image;


public class JoyImageUtils {

    private static int mDisplayWidth;

    public static void init(Context context) {
        Picasso.Builder builder = new Picasso.Builder(context);
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new AuthInterceptor());
        Downloader downloader = new OkHttpDownloader(client);
        Picasso.setSingletonInstance(builder.downloader(downloader).build());

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mDisplayWidth = size.x;

        //Picasso.with(context).setIndicatorsEnabled(true);
    }

    public static void preload(Context context, final Image image) {
        String url = image.getUrl();
        if (url != null && !url.isEmpty()) {
            Picasso.with(context).load(url).resize(400, 0);
        }
    }

    public static void load(final ImageView imageView, final Image image) {
        imageView.getLayoutParams().height = (int) (mDisplayWidth / image.getAspect());
        imageView.requestLayout();
        if (image.isGif()) {
            Picasso.with(imageView.getContext())
                    .load(image.getUrl())
                    .into(imageView);
        } else {
            Picasso.with(imageView.getContext())
                    .load(image.getUrl())
                    .resize(400, 0)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imageView);
        }
    }

    private static class AuthInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request original = chain.request();
            final Request.Builder requestBuilder = original.newBuilder()
                    .header("Referer", "http://joyreactor.cc/")
                    .method(original.method(), original.body());
            return chain.proceed(requestBuilder.build());
        }
    }
}
