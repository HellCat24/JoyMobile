package y2k.joyreactor.ui.feed.base

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import y2k.joyreactor.Post
import y2k.joyreactor.R
import y2k.joyreactor.common.BaseFragment
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.presenters.PostListPresenter
import y2k.joyreactor.ui.adapter.EndlessRecyclerOnScrollListener
import y2k.joyreactor.ui.adapter.PostAdapter
import y2k.joyreactor.view.ReloadButton

/**
 * Created by y2k on 9/26/15.
 */
abstract class PostListFragment() : BaseFragment() {

    var type = postType;

    lateinit var adapter: PostAdapter
    lateinit var presenter: PostListPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        view.findViewById(R.id.error).visibility = View.GONE

        val refreshLayout = view.findViewById(R.id.refresher) as SwipeRefreshLayout
        val list = view.findViewById(R.id.list) as RecyclerView
        list.layoutManager = PreLoadLayoutManager(activity)
        list.addOnScrollListener(LoadMoreListener(list.layoutManager as LinearLayoutManager))

        presenter = ServiceLocator.resolve(lifeCycleService,
                object : PostListPresenter.View {

                    override fun addNewPosts(posts: List<Post>) {
                        adapter.addData(posts)
                    }

                    override fun getPostType(): String {
                        return type;
                    }

                    override fun setBusy(isBusy: Boolean) {
                        refreshLayout.isRefreshing = isBusy
                    }

                    override fun reloadPosts(posts: List<Post>, divider: Int?) {
                        adapter.reloadData(posts, divider)
                    }

                    override fun setHasNewPosts(hasNewPosts: Boolean) {
                        (view.findViewById(R.id.apply) as ReloadButton).setVisibility(hasNewPosts)
                    }
                })

        adapter = PostAdapter(presenter); list.adapter = adapter
        view.findViewById(R.id.apply).setOnClickListener {
            presenter.applyNew()
            list.smoothScrollToPosition(0)
        }
        refreshLayout.setOnRefreshListener { presenter.reloadFirstPage() }
        return view
    }

    inner class LoadMoreListener(val linearLayoutManager: LinearLayoutManager) : EndlessRecyclerOnScrollListener(linearLayoutManager) {

        override fun onLoadMore(current_page: Int) {
            presenter.loadMore()
        }
    }

    inner class PreLoadLayoutManager(context : Context) : LinearLayoutManager(context) {

        // To pre-load next picture in the feed
        override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
            return 300
        }
    }

    protected abstract val postType: String
}