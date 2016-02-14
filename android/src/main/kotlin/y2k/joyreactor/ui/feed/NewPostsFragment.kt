package y2k.joyreactor.ui.feed

import y2k.joyreactor.common.BaseFragment

/**
 * Created by Oleg on 14.02.2016.
 */
class NewPostsFragment : PostListFragment() {

    override val postType: String
        get() = "all"

}