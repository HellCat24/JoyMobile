package y2k.joyreactor.ui.tags

/**
 * Created by Oleg on 14.02.2016.
 */
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import joy.reactor.data.enteties.Tag
import y2k.joyreactor.R
import y2k.joyreactor.ServiceInjector
import y2k.joyreactor.presenters.TagListPresenter
import y2k.joyreactor.ui.adapter.TagsAdapter
import y2k.joyreactor.ui.base.BaseFragment
import y2k.joyreactor.ui.profile.tags.AddTagDialogFragment

/**
 * Created by y2k on 11/12/15.
 */
class TagsFragment : BaseFragment() {

    lateinit var presenter: TagListPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tag, container, false)
        val list = view.findViewById(R.id.list) as RecyclerView
        list.layoutManager = LinearLayoutManager(context)

        view.findViewById(R.id.action_add_tag).setOnClickListener { AddTagDialogFragment.show(activity.supportFragmentManager) }

        val adapter = TagsAdapter()
        list.adapter = adapter

        presenter = ServiceInjector.inject(object : TagListPresenter.View {

            override fun reloadData(tags: List<Tag>) {
                adapter.updateData(tags)
            }
        })

        return view
    }
}