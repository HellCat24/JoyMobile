package y2k.joyreactor.presenters

import joy.reactor.data.common.subscribeOnMain
import joy.reactor.data.enteties.Comment
import joy.reactor.data.enteties.Profile
import joy.reactor.domain.services.CommentService
import joy.reactor.domain.services.ProfileService
import y2k.joyreactor.platform.Navigation

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

    fun create(postId: String, commentText: String, parentId: Long?) {
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