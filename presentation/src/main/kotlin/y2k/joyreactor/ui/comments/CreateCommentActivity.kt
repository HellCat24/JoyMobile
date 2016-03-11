package y2k.joyreactor.ui.comments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import joy.reactor.data.enteties.Comment
import joy.reactor.data.enteties.Profile
import y2k.joyreactor.R
import y2k.joyreactor.ServiceInjector
import y2k.joyreactor.common.compatAnimate
import y2k.joyreactor.presenters.CreateCommentPresenter
import y2k.joyreactor.ui.base.ToolBarActivity
import y2k.joyreactor.ui.post.VideoActivity
import y2k.joyreactor.view.WebImageView

class CreateCommentActivity : ToolBarActivity() {

    companion object {

        var BUNDLE_POST = "post"
        var BUNDLE_PARENT_ID = "parent_id"
        var ACTION_CREATE_COMMENT = 1

        fun startActivity(activity: Activity?, serverId: String, parentId: Long?) {
            val intent = Intent(activity, CreateCommentActivity::class.java)
            intent.putExtra(BUNDLE_POST, serverId);
            intent.putExtra(BUNDLE_PARENT_ID, parentId);
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

        val serverId = intent.getStringExtra(VideoActivity.BUNDLE_POST)
        val parentId = intent.getLongExtra(BUNDLE_PARENT_ID, 0)

        val presenter = ServiceInjector.inject(
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

        sendButton.setOnClickListener { v -> presenter.create(serverId, textView.text.toString(), parentId) }
    }

    override val fragmentContentId: Int
        get() = R.id.container
    override val layoutId: Int
        get() = R.layout.activity_create_comment
}