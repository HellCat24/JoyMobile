package y2k.joyreactor

/**
 * Created by Oleg on 14.02.2016.
 */
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import y2k.joyreactor.ui.base.BaseFragment
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.presenters.TagListPresenter
import y2k.joyreactor.ui.profile.tags.AddTagDialogFragment
import y2k.joyreactor.view.WebImageView

/**
 * Created by y2k on 11/12/15.
 */
class MenuFragment : BaseFragment() {

    lateinit var presenter: TagListPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tag, container, false)
        val list = view.findViewById(R.id.list) as RecyclerView
        list.layoutManager = LinearLayoutManager(context)

        view.findViewById(R.id.action_add_tag).setOnClickListener { AddTagDialogFragment.show(activity.supportFragmentManager) }

        val adapter = TagsAdapter()
        list.adapter = adapter

        presenter = ServiceLocator.resolve(lifeCycleService, object : TagListPresenter.View {

            override fun reloadData(tags: List<Tag>) {
                adapter.updateData(tags)
            }
        })

        return view
    }

    inner class TagsAdapter : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

        private var tags: List<Tag>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(vh: ViewHolder, position: Int) {
            if (vh.title != null && vh.icon != null) {
                val item = tags!![position]
                vh.title.text = item.title
                vh.icon.setImage(item.image)
            }
        }

        override fun getItemCount(): Int {
            return (if (tags == null) 0 else tags!!.size)
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.item_subscription
        }

        fun updateData(tags: List<Tag>) {
            this.tags = tags
            notifyDataSetChanged()
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val title = view.findViewById(R.id.title) as TextView?
            val icon = view.findViewById(R.id.icon) as WebImageView?
        }
    }
}