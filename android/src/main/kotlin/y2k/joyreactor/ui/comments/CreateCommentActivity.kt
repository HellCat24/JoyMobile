package y2k.joyreactor.ui.comments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import y2k.joyreactor.Comment
import y2k.joyreactor.Post
import y2k.joyreactor.Profile
import y2k.joyreactor.R
import y2k.joyreactor.common.ServiceLocator
import y2k.joyreactor.common.compatAnimate
import y2k.joyreactor.presenters.CreateCommentPresenter
import y2k.joyreactor.ui.post.VideoActivity
import y2k.joyreactor.view.WebImageView

class CreateCommentActivity : AppCompatActivity() {

    companion object {

        var BUNDLE_POST = "post"
        var ACTION_CREATE_COMMENT = 1

        fun startActivity(activity: Activity?, post: Post) {
            val intent = Intent(activity, CreateCommentActivity::class.java)
            intent.putExtra(BUNDLE_POST, post);
            activity!!.startActivityForResult(intent, ACTION_CREATE_COMMENT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_comment)

        val nameView = findViewById(R.id.userName) as TextView
        val textView = findViewById(R.id.text) as TextView
        val sendButton = findViewById(R.id.send)
        val progress = findViewById(R.id.progress)

        val post = intent.getSerializableExtra(VideoActivity.BUNDLE_POST) as Post

        val presenter = ServiceLocator.resolve(
                object : CreateCommentPresenter.View {

                    override fun addComment(comment: Comment) {
                        intent = Intent();
                        intent.putExtra("comment", comment);
                        setResult(RESULT_OK, intent);
                    }

                    override fun setIsBusy(isBusy: Boolean) {
                        // TODO
                        if (isBusy) {
                            progress.visibility = View.VISIBLE
                            progress.alpha = 0f
                            progress.compatAnimate().alpha(1f)

                            sendButton.compatAnimate().alpha(0f).withEndAction { sendButton.visibility = View.INVISIBLE }
                        } else {
                            sendButton.visibility = View.VISIBLE
                            sendButton.alpha = 0f
                            sendButton.compatAnimate().alpha(1f)

                            progress.compatAnimate().alpha(0f).withEndAction { progress.visibility = View.GONE }
                        }
                    }

                    override fun setUser(profile: Profile) {
                        (findViewById(R.id.userImage) as WebImageView).setImage(profile.userImage)

                        nameView.text = profile.userName
                        nameView.alpha = 0f
                        nameView.compatAnimate().alpha(1f)
                    }
                })

        sendButton.setOnClickListener { v -> presenter.create(post.serverId, textView.text.toString()) }
    }
}