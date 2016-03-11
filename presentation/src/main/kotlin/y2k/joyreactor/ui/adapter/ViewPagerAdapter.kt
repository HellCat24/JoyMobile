package y2k.joyreactor.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

/**
 * Created by Oleg on 14.02.2016.
 */
class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val mFragments = ArrayList<Fragment>()
    private val mFragmentTitles = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position];
    }

    override fun getCount(): Int {
        return mFragments.size;
    }

    @Override
    override fun getPageTitle(position: Int): String {
        return mFragmentTitles[position];
    }
}