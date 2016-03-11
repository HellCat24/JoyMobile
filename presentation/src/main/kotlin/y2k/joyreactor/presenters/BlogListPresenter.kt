package y2k.joyreactor.presenters

import joy.reactor.data.common.subscribeOnMain
import joy.reactor.data.enteties.Blog
import joy.reactor.data.requests.BlogListRequest
import y2k.joyreactor.platform.Navigation
import java.util.*

/**
 * Created by Oleg on 28.02.2016.
 */
class BlogListPresenter(
        private val view: BlogListPresenter.View,
        private val service: BlogListRequest,
        private val navigation: Navigation) {

    var blogList = ArrayList<Blog>()

    init {
        getBlog()
    }

    fun getBlog() {
        service.requestAsync(view.getBlogListUrl())
                .subscribeOnMain { it ->
                    blogList.addAll(it)
                    var postTitleList = ArrayList<String>()
                    for (blog in it) {
                        postTitleList.add(blog.title)
                    }
                    view.addNewBlog(postTitleList)
                }
    }

    fun showBlogPostList(position: Int) {
        val blog = blogList[position]
        navigation.showBlogPostList(blog.tagUrl, blog.title)
    }

    interface View {

        fun setBusy(isBusy: Boolean)

        fun addNewBlog(blogList: List<String>)

        fun getBlogListUrl(): String

    }
}