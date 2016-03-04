package y2k.joyreactor.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import y2k.joyreactor.enteties.Image;


/**
 * Created by Oleg on 24.02.2016.
 */
public class JoyGlide {

    public static void load(ImageView imageView, Image image) {
        String url = image.getUrl();

        GlideUrl glideUrl = null;

        if (url.contains("joy")) {
            glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader("Referer", "http://joyreactor.cc/")
                    .build());
        } else {
            glideUrl = new GlideUrl(url, new LazyHeaders.Builder().build());
        }

        if (image.isGif()) {
            Glide.with(imageView.getContext())
                    .load(glideUrl)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(glideUrl)
                    .asBitmap()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
    }
}
