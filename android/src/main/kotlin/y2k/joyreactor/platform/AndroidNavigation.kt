package y2k.joyreactor.platform

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import y2k.joyreactor.common.ActivityLifecycleCallbacksAdapter
import y2k.joyreactor.common.startActivity
import y2k.joyreactor.enteties.Message
import y2k.joyreactor.enteties.Post
import y2k.joyreactor.ui.MainActivity
import y2k.joyreactor.ui.base.BaseFragmentActivity
import y2k.joyreactor.ui.blog.BlogPostListActivity
import y2k.joyreactor.ui.blog.BlogPostListFragment
import y2k.joyreactor.ui.comments.CreateCommentActivity
import y2k.joyreactor.ui.post.PostActivity
import y2k.joyreactor.ui.post.VideoActivity
import y2k.joyreactor.ui.profile.LoginActivity
import y2k.joyreactor.ui.user.UserPostActivity
import y2k.joyreactor.ui.profile.message.DialogsActivity
import y2k.joyreactor.ui.profile.message.MessagesActivity
import y2k.joyreactor.ui.profile.tags.AddTagDialogFragment
import y2k.joyreactor.ui.profile.tags.TagsActivity

/**
 * Created by y2k on 10/19/15.
 */
open class AndroidNavigation(app: Application) : Navigation {

    override fun openYouTube(url: String) {
        currentActivity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    internal var currentActivity: BaseFragmentActivity? = null

    init {
        app.registerActivityLifecycleCallbacks(MyActivityLifecycleCallbacks())
    }

    override fun switchProfileToLogin() {
        currentActivity?.startActivity(MainActivity::class)
        currentActivity?.finish()
    }

    @Deprecated("Profile Activity is now Fragment")
    override fun switchLoginToProfile() {
        currentActivity?.finish()
    }

    override fun closeCreateComment() {
        currentActivity!!.finish()
    }

    override fun closeAddTag() {
        AddTagDialogFragment.dismiss(currentActivity as AppCompatActivity)
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
        currentActivity?.overridePendingTransition(0, 0)
    }

    override fun openImageView(post: Post) {
        // TODO:
    }

    override fun openCreateComment(postId: String, parentId: Long?) {
        CreateCommentActivity.startActivity(currentActivity, postId, parentId)
    }

    //New transitions

    override fun openTags() {
        currentActivity?.startActivity(TagsActivity::class)
        currentActivity?.overridePendingTransition(0, 0);
    }

    override fun openPostListForBlog(url: String) {
        BlogPostListActivity.startBlogPostActivity(currentActivity, url)
        currentActivity?.overridePendingTransition(0, 0)
    }

    override fun openUserPosts(username: String) {
        UserPostActivity.startActivity(currentActivity, username)
        currentActivity?.overridePendingTransition(0, 0)
    }

    override fun openDialogs() {
        currentActivity?.startActivity(DialogsActivity::class)
        currentActivity?.overridePendingTransition(0, 0);
    }

    override fun openLogin() {
        currentActivity?.startActivity(LoginActivity::class)
        currentActivity?.overridePendingTransition(0, 0)
    }

    override fun showBlogPostList(tag: String, title: String) {
        currentActivity?.addFragment(BlogPostListFragment.create(tag, title))
    }

    override fun openMessages(dialog: Message) {
        MessagesActivity.startActivity(currentActivity, dialog)
        currentActivity?.overridePendingTransition(0, 0);
    }

    override fun openVideo(post: Post) {
        VideoActivity.startActivity(currentActivity, post)
        currentActivity?.overridePendingTransition(0, 0);
    }

    private inner class MyActivityLifecycleCallbacks : ActivityLifecycleCallbacksAdapter() {

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            currentActivity = activity as BaseFragmentActivity?
        }

        override fun onActivityResumed(activity: Activity?) {
            currentActivity = activity as BaseFragmentActivity?
        }

        override fun onActivityPaused(activity: Activity?) {
            if (currentActivity == activity) currentActivity = null
        }
    }

    companion object {

        internal var sPostIdArgument = "2294127" // FIXME:
    }
}