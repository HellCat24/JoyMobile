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
import y2k.joyreactor.ui.profile.ProfileFragment

class MainActivity : ToolBarActivity() {

    override val fragmentContentId: Int
        get() = throw UnsupportedOperationException()
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var viewPager = findViewById(R.id.viewpager) as ViewPager
        var tabs = findViewById(R.id.tabs) as TabLayout

        setupViewPager(viewPager)
        tabs.setupWithViewPager(viewPager)
    }

    fun setupViewPager(viewPager: ViewPager) {
        var adapter = ViewPagerAdapter(supportFragmentManager);
        //adapter.addFragment(NewPostsFragment(), "New");
        adapter.addFragment(GoodPostsFragment(), "Good");
        //adapter.addFragment(BestPostsFragment(), "Best");
        adapter.addFragment(ProfileFragment(), "Profile");
        viewPager.adapter = adapter;
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.updates -> UpdateFragment.show(this)
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}