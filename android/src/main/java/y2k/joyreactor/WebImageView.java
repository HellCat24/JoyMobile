package y2k.joyreactor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import kotlin.Unit;
import y2k.joyreactor.platform.ImageRequest;

/**
 * Created by y2k on 9/26/15.
 */
public class WebImageView extends ImageView {

    private Image image;

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImage(Image image) {
        if (this.image == image || (image != null && image.equals(this.image))) return;

        this.image = image;

        new ImageRequest()
                .setSize(getSize(), getSize())
                .setUrl(image)
                .to(this, bitmap -> {
                    setImageBitmap(bitmap);
                    return Unit.INSTANCE;
                });

    }

    private int getSize() {
        return (int) (getLayoutParams().width * getResources().getDisplayMetrics().density);
    }
}