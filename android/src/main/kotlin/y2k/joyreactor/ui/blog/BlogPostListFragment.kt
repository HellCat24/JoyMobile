package y2k.joyreactor.ui.blog

import android.os.Bundle
import y2k.joyreactor.ui.feed.base.PostListFragment

/**
 * Created by Oleg on 01.03.2016.
 */
class BlogPostListFragment : PostListFragment() {

    companion object {

        var BUNDLE_TAG = "tag"

        fun create(url: String?): BlogPostListFragment {
            val data = Bundle()
            data.putString(BUNDLE_TAG, url);
            var fragment = BlogPostListFragment()
            fragment.arguments = data;
            return fragment;
        }
    }

    override fun getCurrentTag(): String {
        return arguments.getString(BUNDLE_TAG)
    }
}