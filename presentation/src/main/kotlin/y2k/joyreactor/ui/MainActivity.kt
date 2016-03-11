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

class MainActivity : ToolBarActivity() {

    lateinit var adapter: ViewPagerAdapter
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var tabs = findViewById(R.id.tabs) as TabLayout
        setupViewPager()
        tabs.setupWithViewPager(viewPager)
    }

    fun setupViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager);

        adapter.addFragment(NewPostsFragment(), getString(R.string.new_));
        adapter.addFragment(GoodPostsFragment(), getString(R.string.good));
        adapter.addFragment(BestPostsFragment(), getString(R.string.best));
        adapter.addFragment(ProfileFragment(), getString(R.string.profile));

        viewPager = findViewById(R.id.viewpager) as ViewPager
        viewPager.adapter = adapter;
        viewPager.offscreenPageLimit = adapter.count
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
                (adapter.getItem(viewPager.currentItem) as PostListFragment).refresh()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override val layoutId: Int
        get() = R.layout.activity_main

}