package y2k.joyreactor.ui.blog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import y2k.joyreactor.R
import y2k.joyreactor.ui.base.ToolBarActivity
import y2k.joyreactor.ui.profile.message.MessageFragment

/**
 * Created by Oleg on 28.02.2016.
 */
class BlogPostListActivity : ToolBarActivity() {

    companion object {

        var BUNDLE_BLOG_URL = "blog_url"

        fun startActivity(activity: Activity?, url : String) {
            val intent = Intent(activity, BlogPostListActivity::class.java)
            intent.putExtra(BUNDLE_BLOG_URL, url);
            activity!!.startActivity(intent)
        }
    }

    override val fragmentContentId: Int
        get() = R.id.container
    override val layoutId: Int
        get() = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var blogUrl = intent.getSerializableExtra(BUNDLE_BLOG_URL) as String?
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true);
        //TODO Add Fragment
    }
}