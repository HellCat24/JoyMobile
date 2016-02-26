package y2k.joyreactor.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Oleg on 25.02.2016.
 */
public class LimitSizeRecyclerView extends RecyclerView {

    public LimitSizeRecyclerView(Context context) {
        super(context);
    }

    public LimitSizeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LimitSizeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Adjust width as necessary
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        if(200 < measuredWidth) {
            int measureMode = MeasureSpec.getMode(widthMeasureSpec);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(200, measureMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
