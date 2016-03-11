package y2k.joyreactor.ui.adapter

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import joy.reactor.data.enteties.Comment
import joy.reactor.data.enteties.CommentGroup
import joy.reactor.data.enteties.Image
import joy.reactor.data.enteties.Post
import y2k.joyreactor.R
import y2k.joyreactor.common.ComplexViewHolder
import y2k.joyreactor.common.inflate
import y2k.joyreactor.presenters.PostPresenter
import y2k.joyreactor.view.LargeImageView
import y2k.joyreactor.view.WebImageView
import java.io.File

/**
 * Created by Oleg on 04.03.2016.
 */
class PostAdapter(var presenter: PostPresenter) : RecyclerView.Adapter<ComplexViewHolder>() {

    private var comments: CommentGroup? = null
    private var post: Post? = null
    private var imagePath: File? = null
    private var images: List<Image> = emptyList()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return if (position == 0) 0 else comments!!.get(position - 1).id
    }

    override fun getItemViewType(position: Int): Int {
        return Math.min(1, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplexViewHolder {
        if (viewType == 0) return HeaderViewHolder(parent)
        return CommentViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ComplexViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1 + (if (comments == null) 0 else comments!!.size())
    }

    fun updatePostComments(comments: CommentGroup) {
        this.comments = comments
        notifyDataSetChanged()
    }

    fun addUserComment(comment: Comment) {
        if (comments == null) {
            var commentList = listOf(comment).toMutableList()
            comments = CommentGroup.TwoLevel(commentList)
            notifyItemChanged(1)
            return
        }
        comments?.add(comment)
        var commentCount = 0
        if (comments != null) {
            commentCount = (comments as CommentGroup).size()
        }
        notifyItemInserted(1 + commentCount)
    }

    fun updatePostDetails(post: Post) {
        this.post = post
        notifyItemChanged(0)
    }

    fun updatePostImages(images: List<Image>) {
        this.images = images
        notifyItemChanged(0)
    }

    fun setImageFile(file: File) {
        imagePath = file
        notifyItemChanged(0)
    }

    internal inner class HeaderViewHolder(parent: ViewGroup) : ComplexViewHolder(parent.inflate(R.layout.layout_post)) {

        var image: LargeImageView

        init {
            image = itemView.findViewById(R.id.image) as LargeImageView
        }

        override fun bind() {
            imagePath?.let { image.setImage(it) }
        }
    }

    internal inner class CommentViewHolder(parent: ViewGroup) :
            ComplexViewHolder(parent.inflate(R.layout.item_comment)) {

        var rating: TextView
        var text: TextView
        var replies: TextView
        var avatar: WebImageView
        var attachment: WebImageView

        init {
            rating = itemView.findViewById(R.id.rating) as TextView
            text = itemView.findViewById(R.id.text) as TextView
            avatar = itemView.findViewById(R.id.avatar) as WebImageView
            replies = itemView.findViewById(R.id.replies) as TextView
            attachment = itemView.findViewById(R.id.attachment) as WebImageView

            //TODO Open in another activity
            itemView.findViewById(R.id.action).setOnClickListener { v -> presenter.selectComment(comments!!.getId(realPosition)) }

            val commentButton = itemView.findViewById(R.id.commentMenu)
            commentButton.setOnClickListener { v ->
                val menu = PopupMenu(parent.context, commentButton)
                menu.setOnMenuItemClickListener { menuItem ->
                    if (menuItem.itemId == R.id.reply)
                        presenter.replyToComment(post, comments!!.get(realPosition))
                    true
                }
                menu.inflate(R.menu.comment)
                menu.show()
            }
        }

        override fun bind() {
            (itemView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = if (comments!!.isChild(realPosition)) toPx(64) else toPx(8)

            val c = comments!!.get(realPosition)
            text.text = c.text
            avatar.setImage(c.userImageObject.toImage())
            rating.text = "" + c.rating
            replies.text = "" + c.replies

            attachment.visibility = if (c.attachmentObject == null) View.GONE else View.VISIBLE
            attachment.setImage(c.attachmentObject)
        }

        private val realPosition: Int
            get() = adapterPosition - 1

        private fun toPx(dip: Int): Int {
            return (dip * itemView.resources.displayMetrics.density).toInt()
        }
    }
}