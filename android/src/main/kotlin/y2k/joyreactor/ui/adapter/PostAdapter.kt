package y2k.joyreactor.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import org.ocpsoft.prettytime.PrettyTime
import pl.droidsonroids.gif.GifTextureView
import pl.droidsonroids.gif.InputSource
import y2k.joyreactor.ui.utils.GlideUtils
import y2k.joyreactor.Image
import y2k.joyreactor.Post
import y2k.joyreactor.R
import y2k.joyreactor.common.ComplexViewHolder
import y2k.joyreactor.presenters.PostListPresenter
import y2k.joyreactor.ui.utils.PicassoUtils
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Created by y2k on 1/31/16.
 */
class PostAdapter(private val presenter: PostListPresenter) : RecyclerView.Adapter<ComplexViewHolder>() {

    private val prettyTime = PrettyTime()
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
        this.posts.addAll(posts)
        notifyDataSetChanged()
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

    inner class PostViewHolder(parent: ViewGroup) :
            ComplexViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)) {

        val image: ImageView
        val gifView: GifTextureView
        val userImage: ImageView
        val videoMark: View
        val commentCount: TextView
        val time: TextView
        val userName: TextView
        val rating: TextView
        val textContent: TextView
        val likeDislikeHolder: View
        val imageContainer: RelativeLayout
        val btnExpand: TextView

        init {
            image = itemView.findViewById(R.id.image) as ImageView
            gifView = itemView.findViewById(R.id.gif_view) as GifTextureView
            userImage = itemView.findViewById(R.id.userImage) as ImageView
            videoMark = itemView.findViewById(R.id.videoMark)
            commentCount = itemView.findViewById(R.id.commentCount) as TextView
            time = itemView.findViewById(R.id.time) as TextView
            userName = itemView.findViewById(R.id.userName) as TextView
            rating = itemView.findViewById(R.id.txt_rating) as TextView
            textContent = itemView.findViewById(R.id.text_content) as TextView
            btnExpand = itemView.findViewById(R.id.btn_expand) as TextView
            likeDislikeHolder = itemView.findViewById(R.id.like_dislike_holder) as View
            imageContainer = itemView.findViewById(R.id.image_container) as RelativeLayout

            itemView.findViewById(R.id.card).setOnClickListener { presenter.postClicked(posts[adapterPosition]!!) }
            itemView.findViewById(R.id.videoMark).setOnClickListener { presenter.playClicked(posts[adapterPosition]!!) }
            itemView.findViewById(R.id.btn_post_like).setOnClickListener { presenter.like(posts[adapterPosition]!!) }
            itemView.findViewById(R.id.btn_post_dislike).setOnClickListener { presenter.disLike(posts[adapterPosition]!!) }
        }

        override fun bind() {
            val post = posts[adapterPosition]!!

            userName.text = post.userName
            rating.text = post.rating.toString()
            time.text = prettyTime.format(post.created)
            commentCount.text = post.commentCount.toString()
            //userImage.setImage(post.getUserImage2().toImage())
            videoMark.visibility = if (post.image?.isAnimated ?: false) View.VISIBLE else View.GONE

            processPostImage(post)
            processLikesDislike(post)
            setExpandListener(post)
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
                if (post.image!!.isCoub) {
                    image.visibility = View.GONE
                } else {
                    loadImage(post.image as Image)
                }
            }
        }

        private fun setExpandListener(post: Post) {
            btnExpand.setOnClickListener {
                var images = post.images.subList(1, post.images.size)
                for (img in images) {
                    var imgView: ImageView = ImageView(image.context, null)
                    loadImage(img);
                    imageContainer.addView(imgView)
                    notifyItemChanged(adapterPosition)
                }
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
            if (i.isAnimated) {

                val assManager = image.context.getAssets()
                var inputStream: InputStream? = null
                try {
                    inputStream = assManager.open("tes_gif.gif")
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val caInput = BufferedInputStream(inputStream)

                image.visibility = View.GONE
                gifView.visibility = View.VISIBLE
                gifView.setInputSource(InputSource.InputStreamSource(caInput))
                //GlideUtils.load(image, i)
            } else {
                image.adjustViewBounds = true
                gifView.visibility = View.GONE
                PicassoUtils.load(image, i)
            }
        }
    }
}