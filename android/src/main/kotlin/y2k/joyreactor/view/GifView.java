package y2k.joyreactor.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GifView extends View {

    private Movie mMovie;
    private long mMovieStart;
    private boolean isPlaying = false;

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public GifView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        AssetManager assManager = context.getAssets();
        InputStream is = null;
        try {
            is = assManager.open("tes_gif.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream caInput = new BufferedInputStream(is);
        play(caInput);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        super.onDraw(canvas);
        if (isPlaying) {
            long now = android.os.SystemClock.uptimeMillis();
            if (mMovieStart == 0) {
                mMovieStart = now;
            }
            int relTime = (int) ((now - mMovieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            try {
                mMovie.draw(canvas, this.getWidth() / 2 - 20, this.getHeight() / 2 - 40);
            }catch (Exception e){
                e.getCause();
            }
            this.invalidate();
        }
    }

    public void play(InputStream gifStream) {
        isPlaying = true;
        mMovie = Movie.decodeStream(gifStream);
    }

    public void pause() {
        isPlaying = false;
    }
}