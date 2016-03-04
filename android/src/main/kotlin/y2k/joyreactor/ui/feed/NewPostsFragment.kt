package y2k.joyreactor.ui.feed

import y2k.joyreactor.ui.base.BaseFragment
import y2k.joyreactor.ui.feed.base.PostListFragment

/**
 * Created by Oleg on 14.02.2016.
 */
class NewPostsFragment : PostListFragment() {
    override fun getPostType(): String {
        return "/all"
    }
}