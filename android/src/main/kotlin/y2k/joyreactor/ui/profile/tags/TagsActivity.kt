package y2k.joyreactor.ui.profile.tags

import android.os.Bundle
import y2k.joyreactor.MenuFragment
import y2k.joyreactor.R
import y2k.joyreactor.ui.base.ToolBarActivity

/**
 * Created by y2k on 11/12/15.
 */
class TagsActivity : ToolBarActivity() {

    override val fragmentContentId: Int
        get() = R.id.container

    override val layoutId: Int
        get() = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(MenuFragment())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true);
    }
}