package y2k.joyreactor.presenters

import y2k.joyreactor.enteties.Comment
import y2k.joyreactor.common.subscribeOnMain
import y2k.joyreactor.enteties.Profile
import y2k.joyreactor.platform.Navigation
import y2k.joyreactor.services.CommentService
import y2k.joyreactor.services.ProfileService

/**
 * Created by y2k on 10/4/15.
 */
class CreateCommentPresenter(
        private val view: CreateCommentPresenter.View,
        private val profileService: ProfileService,
        private val service: CommentService) {

    init {
        profileService
                .getProfile()
                .subscribeOnMain { view.setUser(it) }
    }

    fun create(postId: String, commentText: String, parentId : Long?) {
        view.setIsBusy(true)
        service
                .createComment(postId, commentText, parentId)
                .subscribeOnMain {
                    view.setIsBusy(false)
                    view.addComment(Comment(commentText, null, 0, 0, 0.toFloat()))
                    Navigation.instance.closeCreateComment()
                }
    }

    interface View {

        fun setIsBusy(isBusy: Boolean)

        fun setUser(profile: Profile)

        fun addComment(comment: Comment)
    }
}