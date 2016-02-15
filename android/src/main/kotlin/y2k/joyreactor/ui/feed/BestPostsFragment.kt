package y2k.joyreactor.ui.feed

import y2k.joyreactor.ui.feed.base.PostListFragment

/**
 * Created by Oleg on 14.02.2016.
 */
class BestPostsFragment : PostListFragment() {

    override val postType: String
        get() = "best"

}