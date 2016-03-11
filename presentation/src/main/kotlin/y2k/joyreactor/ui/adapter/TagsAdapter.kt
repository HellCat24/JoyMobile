package y2k.joyreactor.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import joy.reactor.data.enteties.Tag
import y2k.joyreactor.R
import y2k.joyreactor.view.WebImageView

/**
 * Created by Oleg on 10.03.2016.
 */
class TagsAdapter : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

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