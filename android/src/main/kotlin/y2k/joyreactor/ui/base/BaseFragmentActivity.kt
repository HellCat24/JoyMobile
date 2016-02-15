package y2k.joyreactor.ui.base

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by Oleg on 14.02.2016.
 */
abstract class BaseFragmentActivity : AppCompatActivity() {

    fun addFragment(f: Fragment) {
        supportFragmentManager.beginTransaction().add(fragmentContentId, f).addToBackStack(f.javaClass.name).commit()
    }

    fun replaceFragment(f: Fragment) {
        var f = f

        val fragment = supportFragmentManager.findFragmentByTag(f.javaClass.name)
        if (fragment != null) {
            f = fragment
            f.onResume()
        }

        supportFragmentManager.beginTransaction().replace(fragmentContentId, f, f.javaClass.name).commit()
    }

    protected fun clearBackStack() {
        val fm = supportFragmentManager
        for (i in 0..fm.backStackEntryCount - 1) {
            fm.popBackStack()
        }
    }

    override fun onBackPressed() {
        val count = fragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            fragmentManager.popBackStack()
        }
    }

    protected abstract val fragmentContentId: Int
}
