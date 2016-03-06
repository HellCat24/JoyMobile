package y2k.joyreactor.platform

import y2k.joyreactor.enteties.Message
import y2k.joyreactor.enteties.Post

/**
 * Created by y2k on 02/10/15.
 */
interface Navigation {

    fun switchProfileToLogin()

    fun switchLoginToProfile()

    fun closeCreateComment()

    fun closeAddTag()

    fun openPost(postId: String)

    fun openBrowser(url: String)

    fun openVideo(postId: String)

    fun openVideo(post: Post)

    fun openYouTube(url: String)

    fun openLongPost(post: Post): Unit

    fun openImageView(post: Post)

    fun showBlogPostList(tag: String, title : String)

    fun openPostListForBlog(url : String)

    fun openCreateComment(postId : String, parentId : Long?)

    fun openTags()

    fun openUserPosts(username : String)

    fun openDialogs()

    fun openLogin()

    fun openMessages(dialog : Message)

    val argumentPostId: String

    companion object {

        val instance: Navigation
            get() = Platform.instance.navigator
    }
}