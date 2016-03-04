package y2k.joyreactor.ui.post

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import y2k.joyreactor.R
import y2k.joyreactor.common.ServiceInjector
import y2k.joyreactor.common.compatAnimate
import y2k.joyreactor.common.find
import y2k.joyreactor.common.isVisible
import y2k.joyreactor.enteties.Comment
import y2k.joyreactor.enteties.CommentGroup
import y2k.joyreactor.enteties.Image
import y2k.joyreactor.enteties.Post
import y2k.joyreactor.presenters.PostPresenter
import y2k.joyreactor.ui.adapter.PostAdapter
import y2k.joyreactor.ui.base.ToolBarActivity
import y2k.joyreactor.ui.comments.CreateCommentActivity
import java.io.File

class PostActivity : ToolBarActivity() {

    lateinit var presenter: PostPresenter
    lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val list = find<RecyclerView>(R.id.list)

        val createComment = findViewById(R.id.createComment)

        presenter = ServiceInjector.resolve(object : PostPresenter.View {

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

            override fun updatePostImage(image: File) {
                adapter.setImageFile(image)
                val imageProgress = find<ProgressBar>(R.id.imageProgress)
                imageProgress.visibility = View.GONE
            }

            override fun updateImageDownloadProgress(progress: Int, maxProgress: Int) {
                val imageProgress = find<ProgressBar>(R.id.imageProgress)
                imageProgress.progress = progress
                imageProgress.max = maxProgress
            }
        })

        adapter = PostAdapter(presenter)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateCommentActivity.ACTION_CREATE_COMMENT && resultCode == RESULT_OK) {
            var userComment = data?.getSerializableExtra("comment") as Comment
            adapter.addUserComment(userComment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.openInBrowser)
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

    override val layoutId: Int
        get() = R.layout.activity_post
}