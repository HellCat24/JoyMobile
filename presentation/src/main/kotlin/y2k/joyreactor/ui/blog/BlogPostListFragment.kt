package y2k.joyreactor.ui.blog

import android.os.Bundle
import android.view.View
import y2k.joyreactor.ui.feed.base.PostListFragment

/**
 * Created by Oleg on 01.03.2016.
 */
class BlogPostListFragment : PostListFragment() {

    companion object {

        var BUNDLE_TAG = "tag"
        var BUNDLE_TITLE = "title"

        fun create(url: String?, title : String?): BlogPostListFragment {
            val data = Bundle()
            data.putString(BUNDLE_TAG, url);
            data.putString(BUNDLE_TITLE, title);
            var fragment = BlogPostListFragment()
            fragment.arguments = data;
            return fragment;
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.title = arguments.getString(BUNDLE_TITLE)
    }

    override fun getCurrentTag(): String {
        return arguments.getString(BUNDLE_TAG)
    }
}