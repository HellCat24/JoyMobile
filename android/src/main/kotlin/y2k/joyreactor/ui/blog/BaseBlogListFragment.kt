package y2k.joyreactor.ui.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import y2k.joyreactor.R
import y2k.joyreactor.common.ServiceInjector
import y2k.joyreactor.presenters.BlogListPresenter
import y2k.joyreactor.ui.base.BaseFragment
import java.util.*

/**
 * Created by Oleg on 28.02.2016.
 */
class BaseBlogListFragment() : BaseFragment(), BlogListPresenter.View {

    companion object {

        var BUNDLE_BLOG_URL = "blog_list_url"

        fun create(url: String?): BaseBlogListFragment {
            val data = Bundle()
            data.putString(BUNDLE_BLOG_URL, url);
            var fragment = BaseBlogListFragment()
            fragment.arguments = data;
            return fragment;
        }
    }

    lateinit var adapter: ArrayAdapter<String>
    lateinit var presenter: BlogListPresenter
    lateinit var list: ListView
    lateinit var blogUrl: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_blog, container, false)

        blogUrl = arguments.getString(BUNDLE_BLOG_URL)

        list = view.findViewById(R.id.blog_list) as ListView
        presenter = ServiceInjector.resolve(this)
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, ArrayList())
        list.adapter = adapter
        list.onItemClickListener = AdapterView.OnItemClickListener { view, parent, i, l -> presenter.showBlogPostList(i) }

        return view
    }

    override fun addNewBlog(blogList: List<String>) {
        adapter.addAll(blogList)
    }

    override fun getBlogListUrl(): String {
        return blogUrl
    }

    override fun setBusy(isBusy: Boolean) {

    }
}