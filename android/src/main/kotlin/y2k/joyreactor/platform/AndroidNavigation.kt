package y2k.joyreactor.platform

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import y2k.joyreactor.*
import y2k.joyreactor.common.ActivityLifecycleCallbacksAdapter
import y2k.joyreactor.common.startActivity
import y2k.joyreactor.ui.*
import y2k.joyreactor.ui.comments.CreateCommentActivity
import y2k.joyreactor.ui.post.PostActivity
import y2k.joyreactor.ui.post.GalleryActivity
import y2k.joyreactor.ui.post.VideoActivity
import y2k.joyreactor.ui.profile.tags.AddTagDialogFragment
import y2k.joyreactor.ui.profile.LoginActivity
import y2k.joyreactor.ui.profile.message.DialogsActivity
import y2k.joyreactor.ui.profile.message.MessagesActivity
import y2k.joyreactor.ui.profile.tags.TagsActivity

/**
 * Created by y2k on 10/19/15.
 */
open class AndroidNavigation(app: Application) : Navigation {



    internal var currentActivity: Activity? = null

    init {
        app.registerActivityLifecycleCallbacks(MyActivityLifecycleCallbacks())
    }

    override fun switchProfileToLogin() {
        currentActivity?.startActivity(LoginActivity::class)
        currentActivity?.finish()
    }

    //FIXME Add Action To Activity To Open Pofile Screen
    @Deprecated("Profile Activity is now Fragment")
    override fun switchLoginToProfile() {
        currentActivity?.startActivity(MainActivity::class)
        currentActivity?.finish()
    }

    override fun closeCreateComment() {
        currentActivity!!.finish()
    }

    override fun closeAddTag() {
        AddTagDialogFragment.dismiss(currentActivity as AppCompatActivity)
    }

    override fun openPostGallery() {
        currentActivity?.startActivity(GalleryActivity::class)
    }

    override fun openPost(postId: String) {
        sPostIdArgument = postId
        currentActivity?.startActivity(PostActivity::class)
        currentActivity?.overridePendingTransition(0, 0);
    }

    override val argumentPostId: String
        get() = sPostIdArgument

    override fun openBrowser(url: String) {
        currentActivity!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun openVideo(postId: String) {
        sPostIdArgument = postId // TODO:
        currentActivity?.startActivity(VideoActivity::class)
        currentActivity?.overridePendingTransition(0, 0);
    }

    override fun openImageView(post: Post) {
        // TODO:
    }

    override fun openCreateComment() {
        currentActivity?.startActivity(CreateCommentActivity::class)
    }

    //New transitions

    override fun openTags() {
        currentActivity?.startActivity(TagsActivity::class)
    }

    override fun openDialogs() {
        currentActivity?.startActivity(DialogsActivity::class)
    }

    override fun openLogin() {
        currentActivity?.startActivity(LoginActivity::class)
    }

    override fun openMessages(dialog : Message) {
        MessagesActivity.startActivity(currentActivity, dialog)
    }

    override fun openVideo(post: Post) {
        VideoActivity.startActivity(currentActivity, post)
        currentActivity?.overridePendingTransition(0, 0);
    }

    private inner class MyActivityLifecycleCallbacks : ActivityLifecycleCallbacksAdapter() {

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            currentActivity = activity
        }

        override fun onActivityResumed(activity: Activity?) {
            currentActivity = activity
        }

        override fun onActivityPaused(activity: Activity?) {
            if (currentActivity == activity) currentActivity = null
        }
    }

    companion object {

        internal lateinit var sPostArgument: Post // FIXME:
        internal var sPostIdArgument = "2294127" // FIXME:
    }
}