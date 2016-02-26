package y2k.joyreactor.ui.utils;

import android.content.Context;
import android.util.LruCache;
import android.widget.ImageView;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import y2k.joyreactor.Image;
import y2k.joyreactor.view.AuthInterceptor;

/**
 * Created by Oleg on 24.02.2016.
 */
public class PicassoUtils {

    public static void init(Context context) {
        Picasso.Builder builder = new Picasso.Builder(context);
        //builder.memoryCache(new Cache());
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new AuthInterceptor());
        Downloader downloader = new OkHttpDownloader(client);
        Picasso.setSingletonInstance(builder.downloader(downloader).build());
    }

    public static void load(ImageView imageView, Image image) {
        if (image.isAnimated()) {
            Picasso.with(imageView.getContext())
                    .load(image.getUrl())
                    .into(imageView);
        } else {
            Picasso.with(imageView.getContext())
                    .load(image.getUrl())
                    .resize(400, 0)
                    .into(imageView);
        }
    }
}
