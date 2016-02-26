package y2k.joyreactor.ui

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import y2k.joyreactor.R
import y2k.joyreactor.ui.adapter.ViewPagerAdapter
import y2k.joyreactor.ui.base.ToolBarActivity
import y2k.joyreactor.ui.feed.BestPostsFragment
import y2k.joyreactor.ui.feed.GoodPostsFragment
import y2k.joyreactor.ui.feed.NewPostsFragment
import y2k.joyreactor.ui.feed.base.PostListFragment
import y2k.joyreactor.ui.profile.ProfileFragment
import y2k.joyreactor.ui.utils.PicassoUtils

class MainActivity : ToolBarActivity() {

    lateinit var adapter: ViewPagerAdapter
    lateinit var viewPager: ViewPager

    override val fragmentContentId: Int
        get() = throw UnsupportedOperationException()
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var tabs = findViewById(R.id.tabs) as TabLayout

        viewPager = findViewById(R.id.viewpager) as ViewPager
        setupViewPager(viewPager)
        tabs.setupWithViewPager(viewPager)

        PicassoUtils.init(this)
    }

    fun setupViewPager(viewPager: ViewPager) {
        adapter = ViewPagerAdapter(supportFragmentManager);
        adapter.addFragment(NewPostsFragment(), "New");
        adapter.addFragment(GoodPostsFragment(), "Good");
        adapter.addFragment(BestPostsFragment(), "Best");
        adapter.addFragment(ProfileFragment(), "Profile");
        viewPager.adapter = adapter;
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.updates -> {
                UpdateFragment.show(this)
                return true;
            }
            R.id.reload -> {
                (adapter.getItem(viewPager.getCurrentItem()) as PostListFragment).refresh()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}