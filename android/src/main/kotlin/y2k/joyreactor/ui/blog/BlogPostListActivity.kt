package y2k.joyreactor.ui.blog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import y2k.joyreactor.R
import y2k.joyreactor.ui.base.ToolBarActivity

/**
 * Created by Oleg on 28.02.2016.
 */
class BlogPostListActivity : ToolBarActivity(), FragmentManager.OnBackStackChangedListener {

    companion object {

        var BUNDLE_BLOG_URL = "blog_url"

        fun startBlogPostActivity(activity: Activity?, url: String) {
            val intent = Intent(activity, BlogPostListActivity::class.java)
            intent.putExtra(BUNDLE_BLOG_URL, url);
            activity!!.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var blogUrl = intent.getSerializableExtra(BUNDLE_BLOG_URL) as String?
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true);
        replaceFragment(BaseBlogListFragment.create(blogUrl))
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onBackStackChanged() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            supportActionBar?.title = getString(R.string.blog)
        }
    }


    override val fragmentContentId: Int
        get() = R.id.container
    override val layoutId: Int
        get() = R.layout.activity_container
}