package y2k.joyreactor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.view.View;

import java.io.InputStream;

public class GifView extends View {

    Movie movie;
    InputStream is = null, is1 = null;
    long moviestart;

    public GifView(Context context) {
        super(context);
        movie = Movie.decodeStream(is);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if (moviestart == 0) {
            moviestart = now;
        }
        int relTime = (int) ((now - moviestart) % movie.duration());
        movie.setTime(relTime);
        movie.draw(canvas, this.getWidth() / 2 - 20, this.getHeight() / 2 - 40);
        this.invalidate();
    }
}