package y2k.joyreactor.ui.post

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import y2k.joyreactor.*
import y2k.joyreactor.common.*
import y2k.joyreactor.presenters.PostPresenter
import y2k.joyreactor.ui.base.ToolBarActivity
import y2k.joyreactor.ui.comments.CreateCommentActivity
import y2k.joyreactor.view.LargeImageView
import y2k.joyreactor.view.WebImageView
import java.io.File

class PostActivity : ToolBarActivity() {

    override val fragmentContentId: Int
        get() = throw UnsupportedOperationException()

    override val layoutId: Int
        get() = R.layout.activity_post

    lateinit var presenter: PostPresenter
    val adapter = Adapter()

    var imagePath: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val list = find<RecyclerView>(R.id.list)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        val createComment = findViewById(R.id.createComment)

        presenter = ServiceLocator.resolve(object : PostPresenter.View {

            override fun setEnableCreateComments() {
                createComment.compatAnimate().scaleX(1f).scaleY(1f).setInterpolator(AccelerateInterpolator())
            }

            override fun updateComments(comments: CommentGroup) {
                adapter.updatePostComments(comments)
            }

            override fun updatePostInformation(post: Post) {
                adapter.updatePostDetails(post)
                createComment.setOnClickListener { presenter.replyToPost(post) }
            }

            override fun setIsBusy(isBusy: Boolean) {
                findViewById(R.id.progress).isVisible = isBusy
            }

            override fun showImageSuccessSavedToGallery() {
                Toast.makeText(applicationContext, R.string.image_saved_to_gallery, Toast.LENGTH_LONG).show()
            }

            override fun updatePostImages(images: List<Image>) {
                adapter.updatePostImages(images)
            }

            override fun updateSimilarPosts(similarPosts: List<SimilarPost>) {
            }

            override fun updatePostImage(image: File) {
                this@PostActivity.imagePath = image
                adapter.notifyItemChanged(0)
                val imageProgress = find<ProgressBar>(R.id.imageProgress)
                imageProgress.visibility = View.GONE
            }

            override fun updateImageDownloadProgress(progress: Int, maxProgress: Int) {
                val imageProgress = find<ProgressBar>(R.id.imageProgress)
                imageProgress.progress = progress
                imageProgress.max = maxProgress
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==  CreateCommentActivity.ACTION_CREATE_COMMENT && resultCode == RESULT_OK){
            var userComment = data?.getSerializableExtra("comment") as Comment
            adapter.addUserComment(userComment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.reply)
        //presenter.replyToPost()
        else if (item.itemId == R.id.openInBrowser)
            presenter.openPostInBrowser()
        else if (item.itemId == R.id.saveImageToGallery)
            saveImageToGallery()
        else if (item.itemId == android.R.id.home)
            onBackPressed()
        else
            super.onOptionsItemSelected(item)
        return true
    }

    private fun saveImageToGallery() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            presenter.saveImageToGallery()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    inner class Adapter : RecyclerView.Adapter<ComplexViewHolder>() {

        private var comments: CommentGroup? = null
        private var post: Post? = null
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

        fun addUserComment(comment: Comment?) {
            comments?.add(comment)
        }

        fun updatePostDetails(post: Post) {
            this.post = post
            notifyItemChanged(0)
        }

        fun updatePostImages(images: List<Image>) {
            this.images = images
            notifyItemChanged(0)
        }

        internal inner class HeaderViewHolder(parent: ViewGroup) :
                ComplexViewHolder(parent.inflate(R.layout.layout_post)) {

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
                            presenter.replyToComment(comments!!.get(realPosition))
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
}