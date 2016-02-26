package y2k.joyreactor.ui.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import y2k.joyreactor.Image;

/**
 * Created by Oleg on 24.02.2016.
 */
public class GlideUtils {

    public static void load(ImageView imageView, Image image) {
        String url = image.getUrl();
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Referer", "http://joyreactor.cc/")
                .build());

        Glide.with(imageView.getContext())
                .load(glideUrl)
                .asBitmap()
                .thumbnail( 0.1f )
                .into(imageView);
    }
}
