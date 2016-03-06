package y2k.joyreactor.ui.user

import android.os.Bundle
import y2k.joyreactor.ui.blog.BlogPostListFragment
import y2k.joyreactor.ui.feed.base.PostListFragment

/**
 * Created by Oleg on 01.03.2016.
 */
class UserPostListFragment : PostListFragment() {

    companion object {

        var BUNDLE_USERNAME = "username"

        fun create(url: String?): UserPostListFragment {
            val data = Bundle()
            data.putString(BUNDLE_USERNAME, url);
            var fragment = UserPostListFragment()
            fragment.arguments = data;
            return fragment;
        }
    }

    override fun getCurrentUserName(): String {
        return arguments.getString(BUNDLE_USERNAME)
    }
}