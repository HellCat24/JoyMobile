package y2k.joyreactor.ui.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import y2k.joyreactor.Blog
import y2k.joyreactor.R
import y2k.joyreactor.common.BaseFragment
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.presenters.BlogListPresenter
import java.util.*

/**
 * Created by Oleg on 28.02.2016.
 */
class BaseBlogListFragment() : BaseFragment(), BlogListPresenter.View {

    lateinit var adapter: ArrayAdapter<String>
    lateinit var presenter: BlogListPresenter
    lateinit var list: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_blog, container, false)

        list = view.findViewById(R.id.blog_list) as ListView
        presenter = ServiceLocator.resolve(this)
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, ArrayList())
        list.adapter = adapter

        return view
    }

    override fun addNewBlog(blogList: List<String>) {
        adapter.addAll(blogList)
    }

    override fun getBlogUrl(): String {
        return "http://joyreactor.cc/tag/%25D1%2581%25D0%25B5%25D0%25BA%25D1%2580%25D0%25B5%25D1%2582%25D0%25BD%25D1%258B%25D0%25B5%2B%25D1%2580%25D0%25B0%25D0%25B7%25D0%25B4%25D0%25B5%25D0%25BB%25D1%258B/rating"
    }

    override fun setBusy(isBusy: Boolean) {

    }
}