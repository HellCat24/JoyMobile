package y2k.joyreactor.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

import y2k.joyreactor.R

class BoundedFrameLayout : FrameLayout {

    private var mMaxWidth = 0;

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            mMaxWidth = context.resources.getDimension(R.dimen.max_layout_width).toInt()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        val measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        if (mMaxWidth != 0 && mMaxWidth < measuredWidth) {
            val measureMode = View.MeasureSpec.getMode(widthMeasureSpec)
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mMaxWidth, measureMode)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}