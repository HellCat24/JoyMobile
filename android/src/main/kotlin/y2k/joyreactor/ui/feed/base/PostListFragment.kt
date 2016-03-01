package y2k.joyreactor.ui.feed.base

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import y2k.joyreactor.Post
import y2k.joyreactor.R
import y2k.joyreactor.ui.base.BaseFragment
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.image.JoyImageUtils
import y2k.joyreactor.presenters.PostListPresenter
import y2k.joyreactor.ui.adapter.EndlessRecyclerOnScrollListener
import y2k.joyreactor.ui.adapter.PostAdapter

/**
 * Created by y2k on 9/26/15.
 */
abstract class PostListFragment() : BaseFragment(), PostListPresenter.View {

    lateinit var adapter: PostAdapter
    lateinit var presenter: PostListPresenter
    lateinit var list: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        list = view.findViewById(R.id.list) as RecyclerView
        list.layoutManager = PreLoadLayoutManager(activity)
        list.addOnScrollListener(LoadMoreListener(list.layoutManager as LinearLayoutManager))

        presenter = ServiceLocator.resolve(lifeCycleService, this)

        adapter = PostAdapter(presenter)
        list.adapter = adapter

        return view
    }

    fun refresh() {
        presenter.reloadFirstPage()
    }

    override fun updatePostRating(post: Post) {
        adapter.updatePostRating(post)
    }

    override fun setLikesDislikesEnable() {
        adapter.setLikesDislikesEnable()
    }

    override fun addNewPosts(posts: List<Post>) {
        preloadPostImages(posts)
        adapter.addData(posts)
    }

    private fun preloadPostImages(posts: List<Post>) {
        for (post in posts) {
            if (post.image != null && !post.image!!.isCoub) {
                JoyImageUtils.preload(activity, post.image)
            }
        }
    }

    override fun setBusy(isBusy: Boolean) {
        //refreshLayout.isRefreshing = isBusy
    }

    override fun reloadPosts(posts: List<Post>, divider: Int?) {
        adapter.reloadData(posts)
    }

    override fun setHasNewPosts(hasNewPosts: Boolean) {

    }

    inner class LoadMoreListener(val linearLayoutManager: LinearLayoutManager) : EndlessRecyclerOnScrollListener(linearLayoutManager) {

        override fun onLoadMore(current_page: Int) {
            presenter.loadMore()
        }
    }

    inner class PreLoadLayoutManager(context: Context) : LinearLayoutManager(context) {

        // To pre-load next picture in the feed
        override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
            return 300
        }
    }

    override fun getCurrentTag(): String? {
        return null
    }

    override fun getCurrentUserName(): String? {
        return null
    }

    override fun getPostType(): String? {
        return null
    }
}