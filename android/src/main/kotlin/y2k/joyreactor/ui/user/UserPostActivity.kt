package y2k.joyreactor.ui.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import y2k.joyreactor.R
import y2k.joyreactor.ui.base.ToolBarActivity

/**
 * Created by Oleg on 01.03.2016.
 */
class UserPostActivity : ToolBarActivity() {

    companion object {

        var BUNDLE_USERNAME = "username"

        fun startActivity(activity: Activity?, username: String) {
            val intent = Intent(activity, UserPostActivity::class.java)
            intent.putExtra(BUNDLE_USERNAME, username);
            activity!!.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var username = intent.getSerializableExtra(BUNDLE_USERNAME) as String?
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true);
        replaceFragment(UserPostListFragment.create(username))
        title = username
    }

    override val fragmentContentId: Int
        get() = R.id.container
    override val layoutId: Int
        get() = R.layout.activity_container
}