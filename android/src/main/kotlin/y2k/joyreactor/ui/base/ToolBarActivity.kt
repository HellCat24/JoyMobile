package y2k.joyreactor.ui.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import y2k.joyreactor.R

/**
 * Created by Oleg on 11.09.2015.
 */
abstract class ToolBarActivity : BaseFragmentActivity() {

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    protected abstract val layoutId: Int

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0);
    }

    override val fragmentContentId: Int
        get() = throw UnsupportedOperationException()
}
