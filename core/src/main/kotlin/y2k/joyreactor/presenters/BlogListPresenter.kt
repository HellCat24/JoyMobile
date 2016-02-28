package y2k.joyreactor.presenters

import y2k.joyreactor.Blog
import y2k.joyreactor.common.subscribeOnMain
import y2k.joyreactor.platform.Navigation
import y2k.joyreactor.services.requests.BlogListRequest
import java.util.*

/**
 * Created by Oleg on 28.02.2016.
 */
class BlogListPresenter(
        private val view: BlogListPresenter.View,
        private val service: BlogListRequest,
        private val navigation: Navigation) {

    init {
        getBlog()
    }

    fun getBlog() {
        service.requestAsync(view.getBlogUrl())
                .subscribeOnMain { it ->
                    var postTitleList = ArrayList<String>()
                    for(blog in it){
                        postTitleList.add(blog.title)
                    }
                    view.addNewBlog(postTitleList)
                }
    }

    interface View {

        fun setBusy(isBusy: Boolean)

        fun addNewBlog(blogList: List<String>)

        fun getBlogUrl(): String

    }
}