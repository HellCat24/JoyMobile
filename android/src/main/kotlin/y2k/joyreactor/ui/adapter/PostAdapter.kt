package y2k.joyreactor.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import org.ocpsoft.prettytime.PrettyTime
import y2k.joyreactor.Post
import y2k.joyreactor.R
import y2k.joyreactor.common.ComplexViewHolder
import y2k.joyreactor.presenters.PostListPresenter
import y2k.joyreactor.ui.utils.PostUtils
import y2k.joyreactor.view.FixedAspectPanel
import y2k.joyreactor.view.WebImageView
import java.util.*

/**
 * Created by y2k on 1/31/16.
 */
class PostAdapter(private val presenter: PostListPresenter) : RecyclerView.Adapter<ComplexViewHolder>() {

    private val prettyTime = PrettyTime()
    private val posts = ArrayList<Post?>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, itemType: Int): ComplexViewHolder {
        return PostViewHolder(viewGroup)
    }

    override fun onBindViewHolder(h: ComplexViewHolder, position: Int) {
        h.bind()
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun reloadData(posts: List<Post>, divider: Int?) {
        this.posts.clear()
        this.posts.addAll(posts)
        //divider?.let { this.posts.add(it, null) }
        notifyDataSetChanged()
    }

    inner class PostViewHolder(parent: ViewGroup) :
            ComplexViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)) {

        val imagePanel: FixedAspectPanel
        val image: WebImageView
        val userImage: WebImageView
        val videoMark: View
        val commentCount: TextView
        val time: TextView
        val userName: TextView
        val coubPlayer: WebView

        init {
            image = itemView.findViewById(R.id.image) as WebImageView
            imagePanel = itemView.findViewById(R.id.imagePanel) as FixedAspectPanel
            userImage = itemView.findViewById(R.id.userImage) as WebImageView
            videoMark = itemView.findViewById(R.id.videoMark)
            commentCount = itemView.findViewById(R.id.commentCount) as TextView
            time = itemView.findViewById(R.id.time) as TextView
            userName = itemView.findViewById(R.id.userName) as TextView
            coubPlayer = itemView.findViewById(R.id.coub_view) as WebView

            itemView.findViewById(R.id.card).setOnClickListener { presenter.postClicked(posts[adapterPosition]!!) }
            itemView.findViewById(R.id.videoMark).setOnClickListener { presenter.playClicked(posts[adapterPosition]!!) }
        }

        override fun bind() {
            val i = posts[adapterPosition]!!

            if (i.image == null) {
                imagePanel.visibility = View.GONE
            } else {
                if (i.image!!.isCoub) {
                    coubPlayer.visibility = View.VISIBLE
                    imagePanel.visibility = View.GONE
                    PostUtils.loadCoub(coubPlayer, i.image?.url)
                } else {
                    coubPlayer.visibility = View.INVISIBLE
                    imagePanel.visibility = View.VISIBLE
                    imagePanel.setAspect(i.image!!.getAspect(0.5f))
                    image.setImage(i.image)
                }
            }

            userImage.setImage(i.getUserImage2().toImage())
            userName.text = i.userName
            videoMark.visibility = if (i.image?.isAnimated ?: false) View.VISIBLE else View.GONE

            commentCount.text = "" + i.commentCount
            time.text = prettyTime.format(i.created)
        }
    }
}