package y2k.joyreactor.ui.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import y2k.joyreactor.Image;
import y2k.joyreactor.http.HttpClient;

/**
 * Created by Oleg on 24.02.2016.
 */
public class GlideUtils {

    public static void load(ImageView imageView, Image image) {
        String cookie = HttpClient.Companion.getInstance().getCookie();
        String url = image.getUrl();
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Cookie", cookie)
                .addHeader("Accept-Encoding", "gzip")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko")
                .build());
        if (url.contains("gif")) {
            Glide.with(imageView.getContext())
                    .load(glideUrl)
                    .asGif()
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(glideUrl)
                    .asBitmap()
                    .transform(new MyTransformation(imageView.getContext()))
                    .into(imageView);
        }
    }

}
