package y2k.joyreactor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class BoundedLinearLayout extends LinearLayout {

    public BoundedLinearLayout(Context context) {
        super(context);
    }

    public BoundedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoundedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Adjust width as necessary
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (800 < measuredWidth) {
            int measureMode = MeasureSpec.getMode(widthMeasureSpec);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(800, measureMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}