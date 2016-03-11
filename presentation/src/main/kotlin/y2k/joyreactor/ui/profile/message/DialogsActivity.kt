package y2k.joyreactor.ui.profile.message

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import y2k.joyreactor.R
import y2k.joyreactor.ui.profile.message.DialogsFragment
import y2k.joyreactor.ui.base.ToolBarActivity

/**
 * Created by y2k on 11/13/15.
 */
class DialogsActivity : ToolBarActivity() {

    override val fragmentContentId: Int
        get() = R.id.container

    override val layoutId: Int
        get() = R.layout.activity_container

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(DialogsFragment())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true);
    }
}