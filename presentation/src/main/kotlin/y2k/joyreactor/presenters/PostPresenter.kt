package y2k.joyreactor.presenters

import joy.reactor.data.common.subscribeOnMain
import joy.reactor.data.enteties.Comment
import joy.reactor.data.enteties.CommentGroup
import joy.reactor.data.enteties.Post
import joy.reactor.domain.services.PostService
import joy.reactor.domain.services.ProfileService
import y2k.joyreactor.platform.Navigation
import y2k.joyreactor.platform.Platform
import java.io.File

/**
 * Created by y2k on 28/09/15.
 */
class PostPresenter(
        private val view: PostPresenter.View,
        private val service: PostService,
        private val userService: ProfileService,
        private val navigation: Navigation) {

    private lateinit var post: Post

    init {
        view.setIsBusy(true)
        service
                .synchronizePostAsync(argumentPostId)
                .subscribeOnMain { post ->

                    this.post = post;

                    view.updatePostInformation(post)

                    service
                            .getCommentsAsync(post.id, 0)
                            .subscribeOnMain {
                                view.updateComments(it)
                                view.setIsBusy(false)
                            }

                    service
                            .mainImagePartial(post.serverId)
                            .subscribeOnMain { partial ->
                                if (partial.result == null) {
                                    view.updateImageDownloadProgress(partial.progress, partial.max)
                                } else {
                                    view.updatePostImage(partial.result as File)
                                }
                            }

                    userService
                            .isAuthorized()
                            .subscribeOnMain { if (it) view.setEnableCreateComments() }
                }
    }

    fun selectComment(commentId: Long) {
        service
                .getFromCache(argumentPostId)
                .flatMap { post -> service.getCommentsAsync(post.id, commentId) }
                .subscribeOnMain { view.updateComments(it) }
    }

    fun openPostInBrowser() {
        Navigation.instance.openBrowser("http://joyreactor.cc/post/" + argumentPostId)
    }

    fun saveImageToGallery() {
        view.setIsBusy(true)
        service.getFromCache(argumentPostId)
                .flatMap { post -> service.mainImage(post.serverId!!) }
                .flatMap { imageFile -> Platform.instance.saveToGallery(imageFile) }
                .subscribeOnMain {
                    view.showImageSuccessSavedToGallery()
                    view.setIsBusy(false)
                }
    }

    private val argumentPostId: String
        get() = post.serverId

    fun replyToComment(post: Post?, comment: Comment) {
        navigation.openCreateComment(post!!.serverId, comment.id)
    }

    fun replyToPost(post: Post) {
        navigation.openCreateComment(post!!.serverId, null)
    }

    interface View {

        fun updateComments(comments: CommentGroup)

        @Deprecated("")
        fun updatePostInformation(post: Post)

        fun setIsBusy(isBusy: Boolean)

        fun showImageSuccessSavedToGallery()

        fun updatePostImage(image: File)

        fun updateImageDownloadProgress(progress: Int, maxProgress: Int)

        fun setEnableCreateComments()
    }
}