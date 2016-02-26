package y2k.joyreactor.ui.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import y2k.joyreactor.view.AuthInterceptor;

/**
 * Created by Oleg on 24.02.2016.
 */
public class PicassoUtils {

    private static Picasso picasso;

    public static void init(Context context) {
        if (picasso == null) {
            Picasso.Builder builder = new Picasso.Builder(context);

            OkHttpClient client = new OkHttpClient();
            client.interceptors().add(new AuthInterceptor());

            Downloader downloader = new OkHttpDownloader(client);

            picasso = builder.downloader(downloader).build();

            Picasso.setSingletonInstance(picasso);
        }
    }

    public static void load(ImageView imageView, String url){
        Picasso.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }
}
