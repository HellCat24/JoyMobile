package y2k.joyreactor.ui.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import y2k.joyreactor.R
import y2k.joyreactor.common.ComplexViewHolder
import y2k.joyreactor.enteties.Image
import y2k.joyreactor.enteties.Post
import y2k.joyreactor.image.JoyImageUtils
import y2k.joyreactor.presenters.PostListPresenter
import java.util.*

/**
 * Created by y2k on 1/31/16.
 */
class PostListAdapter(private val presenter: PostListPresenter) : RecyclerView.Adapter<ComplexViewHolder>() {

    private val posts = ArrayList<Post?>()
    private var isLikesDislikesEnabled = false;

    override fun onCreateViewHolder(viewGroup: ViewGroup, itemType: Int): ComplexViewHolder {
        return PostViewHolder(viewGroup)
    }

    override fun onBindViewHolder(h: ComplexViewHolder, position: Int) {
        h.bind()
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun addData(posts: List<Post>) {
        var startRange = this.posts.size
        var endRange = startRange + posts.size
        this.posts.addAll(posts)
        notifyItemRangeChanged(startRange, endRange)
    }

    fun reloadData(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
    }

    fun setLikesDislikesEnable() {
        isLikesDislikesEnabled = true;
        notifyDataSetChanged()
    }

    fun updatePostRating(post: Post) {
        var index = posts.indexOf(post)
        posts[index] = post
        notifyItemChanged(index)
    }

    inner class PostViewHolder(parent: ViewGroup) : ComplexViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)) {

        val baseView: CardView
        val image: ImageView
        val userImage: ImageView
        val videoMark: View
        val commentCount: TextView
        val userName: TextView
        val rating: TextView
        val textContent: TextView
        val likeDislikeHolder: View
        val imageContainer: FrameLayout
        val btnExpand: TextView

        init {
            baseView = itemView.findViewById(R.id.cardview) as CardView
            image = itemView.findViewById(R.id.image) as ImageView
            userImage = itemView.findViewById(R.id.userImage) as ImageView
            videoMark = itemView.findViewById(R.id.videoMark)
            commentCount = itemView.findViewById(R.id.commentCount) as TextView
            userName = itemView.findViewById(R.id.userName) as TextView
            rating = itemView.findViewById(R.id.txt_rating) as TextView
            textContent = itemView.findViewById(R.id.text_content) as TextView
            btnExpand = itemView.findViewById(R.id.btn_expand) as TextView
            likeDislikeHolder = itemView.findViewById(R.id.like_dislike_holder) as View
            imageContainer = itemView.findViewById(R.id.image_container) as FrameLayout

            itemView.findViewById(R.id.cardview).setOnClickListener { presenter.postClicked(posts[adapterPosition]!!) }
            itemView.findViewById(R.id.videoMark).setOnClickListener { presenter.playClicked(posts[adapterPosition]!!) }
            itemView.findViewById(R.id.btn_post_like).setOnClickListener { presenter.like(posts[adapterPosition]!!) }
            itemView.findViewById(R.id.btn_post_dislike).setOnClickListener { presenter.disLike(posts[adapterPosition]!!) }
            itemView.findViewById(R.id.btn_post_favorite).setOnClickListener { presenter.addToFavorite(posts[adapterPosition]!!) }
            btnExpand.setOnClickListener { presenter.showLongPost(posts[adapterPosition]!!) }
        }

        override fun bind() {
            val post = posts[adapterPosition]!!

            userName.setOnClickListener { presenter.showUserPosts(post.userName) }

            userName.text = post.userName
            rating.text = post.rating.toString()
            //userImage.setImage(post.getUserImage2().toImage())
            commentCount.text = post.commentCount.toString() + " comments"
            videoMark.visibility = if (post.image?.isPlaylable ?: false) View.VISIBLE else View.GONE

            processPostImage(post)
            processLikesDislike(post)
            processPostTitle(post)
        }

        private fun processPostTitle(post: Post) {
            if (!post.title.isEmpty()) {
                textContent.visibility = View.VISIBLE
                textContent.text = post.title
            } else {
                textContent.visibility = View.GONE
            }
        }

        private fun processPostImage(post: Post) {
            if (post.image == null || post.image?.url?.isEmpty()!!) {
                imageContainer.visibility = View.GONE
            } else {
                btnExpand.visibility = if (post.images.size > 1) View.VISIBLE else View.GONE
                loadImage(post.image as Image)
            }
        }

        private fun processLikesDislike(post: Post) {
            if (isLikesDislikesEnabled) {
                if (post.isLiked) {
                    likeDislikeHolder.visibility = View.GONE
                    rating.visibility = View.VISIBLE
                } else {
                    likeDislikeHolder.visibility = View.VISIBLE
                    rating.visibility = View.GONE
                }
            } else {
                likeDislikeHolder.visibility = View.GONE
                rating.visibility = View.VISIBLE
            }
        }

        private fun loadImage(i: Image) {
            JoyImageUtils.load(image, i)
        }
    }
}